package com.sprint.project.findex.service;

import com.sprint.project.findex.dto.SyncJobDto;
import com.sprint.project.findex.entity.IndexInfo;
import com.sprint.project.findex.entity.SyncJob;
import com.sprint.project.findex.global.entity.JobType;
import com.sprint.project.findex.global.entity.ResultType;
import com.sprint.project.findex.global.exception.BusinessLogicException;
import com.sprint.project.findex.global.exception.ExceptionCode;
import com.sprint.project.findex.indexinfo.external.dto.StockMarketIndexAPIResponse;
import com.sprint.project.findex.mapper.SyncJobMapper;
import com.sprint.project.findex.repository.IndexInfoRepository;
import com.sprint.project.findex.repository.SyncJobRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class SyncJobService {

  private final IndexInfoRepository indexInfoRepository;
  private final SyncJobRepository syncJobRepository;
  private final WebClient webClient;

  private final SyncJobMapper syncJobMapper;

  @Value("${LOCAL_INDEX_API_KEY}")
  private String apiKey;

  // DB에 저장된 지수 정보를 바탕으로 실제 값을 Open API로부터 조회하고 기록합니다.
  public List<SyncJobDto> syncIndexInfos(HttpServletRequest request) {
    // DB로부터 지수 정보를 불러온다.
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();

    List<SyncJobDto> syncJobDtos = new ArrayList<>(); // 컨트롤러 응답
    String requestIpAddr = request.getRemoteAddr();

    // 모든 지수 데이터에 대해 작업을 반복한다.
    for (IndexInfo indexInfo : indexInfos) {
      // 지수 정보 가져오기.

      ResultType resultType = ResultType.FAIL;

      try {
        // todo: 아래의 WebClient 코드는 임의로 작성함. 나중에 분리 및 교체할 예정.
        StockMarketIndexAPIResponse apiResponse = webClient.get()
            .uri(urlBuilder -> urlBuilder
                .queryParam("serviceKey", apiKey)
                .queryParam("resultType", "json")
                .queryParam("idxNm", indexInfo.getIndexName())
                .queryParam("beginEpyItmsCnt", indexInfo.getEmployedItemsCount())
                .queryParam("basDt",
                    indexInfo.getBasePointInTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build()
            )
            .retrieve() // response body를 추출
            .bodyToMono(StockMarketIndexAPIResponse.class)
            .block(Duration.ofSeconds(5));

        // 지수 데이터 갱신
        if (apiResponse.response().header().resultCode().equals("00")
            && !apiResponse.response().bodyDto().items().item()
            .isEmpty()) {
          indexInfo.updateByOpenAPI(apiResponse.response().bodyDto().items().item().get(0)); // 갱신
          resultType = ResultType.SUCCESS;
        }
      } catch (Exception e) {
        new BusinessLogicException(ExceptionCode.OPEN_API_REQUEST_FAILED, e.getMessage());
      } finally {
        // SyncJob 히스토리 등록
        SyncJob syncJob = new SyncJob(indexInfo, JobType.INDEX_INFO, null, requestIpAddr,
            resultType);
        syncJobRepository.save(syncJob);

        syncJobDtos.add(syncJobMapper.toDto(syncJob));
      }
    }

    return syncJobDtos;
  }
}

package com.sprint.project.findex.service;

import com.sprint.project.findex.entity.AutoSyncConfig;
import com.sprint.project.findex.entity.IndexInfo;
import com.sprint.project.findex.entity.SourceType;
import com.sprint.project.findex.repository.AutoSyncConfigRepository;
import com.sprint.project.findex.service.openapi.OpenApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoSyncSchedulerService {

  private final AutoSyncConfigRepository autoSyncConfigRepository;
  private final OpenApiService openApiService;

  public void syncEnabledIndices() {
    log.info("[AutoSync] 자동 연동 활성화 지수 조회 시작");

    List<AutoSyncConfig> enabledConfigs =
        autoSyncConfigRepository.findByEnabledTrue();

    int success = 0;
    int fail = 0;

    log.info("[AutoSync] 조회된 자동 연동 대상 개수: {}", enabledConfigs.size());

    for (AutoSyncConfig config : enabledConfigs) {

      try {
        syncIndex(config);
        success++;
      } catch (Exception e) {

        fail++;

        log.error("[AutoSync] 지수 동기화 실패: configId = {}", config.getId(), e);
      }
    }

    log.info(
        "[AutoSync] 배치 실행 결과: total = {}, success = {}, fail = {}",
        enabledConfigs.size(),
        success,
        fail
    );
  }

  private void syncIndex(AutoSyncConfig config) {

    IndexInfo index = config.getIndexInfo();

    log.info("[AutoSync] 지수 동기화 실행: configId = {}", config.getId());

    if (index.getSourceType() == SourceType.USER) {
      log.info("[AutoSync] USER 데이터 - 스킵 indexId = {}", index.getId());
      return;
    }

    openApiService.fetchAndSaveByAutoSync(List.of(config));

    log.info("[AutoSync] 지수 동기화 완료: indexId = {}", index.getId());
  }
}

package com.sprint.project.findex.service;

import com.sprint.project.findex.entity.DeletedStatus;
import com.sprint.project.findex.dto.dashboard.IndexPerformanceDto;
import com.sprint.project.findex.repository.DashboardRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService {

  private final DashboardRepository dashboardRepository;

  public List<IndexPerformanceDto> findFavoriteIndexPerformance(String periodType) {

    LocalDate targetDate = calculateTargetDate(LocalDate.now(), periodType);

    return dashboardRepository.findChangedFavoriteIndexPerformance(targetDate, DeletedStatus.ACTIVE)
        .stream()
        .map(dto -> {
          double currentPrice = dto.currentPrice();
          double beforePrice = dto.beforePrice();
          double versus = currentPrice - beforePrice;
          double fluctuationRate = versus * 100.0 / beforePrice;

          return new IndexPerformanceDto(
              dto.indexInfoId(),
              dto.indexClassification(),
              dto.indexName(),
              versus,
              fluctuationRate,
              currentPrice,
              beforePrice
          );
        })
        .toList();
  }

  private LocalDate calculateTargetDate(LocalDate currentDate, String periodType) {

    return switch (periodType) {
      case "DAILY" -> currentDate.minusDays(1);
      case "WEEKLY" -> currentDate.minusWeeks(1);
      case "MONTHLY" -> currentDate.minusMonths(1);
      default -> throw new IllegalArgumentException("잘못된 periodType 입니다.");
    };
  }
}

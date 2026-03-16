package com.sprint.project.findex.controller;

import com.sprint.project.findex.dto.dashboard.IndexPerformanceDto;
import com.sprint.project.findex.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/index-data"))
public class IndexDataController {

  private final DashboardService dashboardService;

  @GetMapping(value = "/performance/favorite")
  public ResponseEntity getIndexPerformance(
      @RequestParam("periodType") String periodType
  ) {
    List<IndexPerformanceDto> dto = dashboardService.findFavoriteIndexPerformance(periodType);

    return ResponseEntity.ok(dto);
  }
}

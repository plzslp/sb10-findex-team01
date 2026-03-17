package com.sprint.project.findex.controller;

import com.sprint.project.findex.dto.SyncJobDto;
import com.sprint.project.findex.service.SyncJobService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {

  private final SyncJobService syncJobService;

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobDto>> syndIndexInfo(HttpServletRequest request) {
    return ResponseEntity.ok(syncJobService.syncIndexInfos(request));
  }

}

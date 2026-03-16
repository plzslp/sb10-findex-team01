package com.sprint.project.findex.controller;

import com.sprint.project.findex.dto.IndexDataCreateRequest;
import com.sprint.project.findex.dto.IndexDataDto;
import com.sprint.project.findex.service.IndexDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/index-data")
@Tag(name = "지수 데이터 API")
public class IndexDataController {

  private final IndexDataService indexDataService;

  @PostMapping
  @Operation(summary = "지수 데이터 등록")
  public ResponseEntity<IndexDataDto> create(
      @Valid @RequestBody IndexDataCreateRequest request) {
    IndexDataDto indexDataDto = indexDataService.createByUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(indexDataDto);
  }
}

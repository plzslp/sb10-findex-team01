package com.sprint.project.findex.controller.advice;

import com.sprint.project.findex.dto.ErrorResponse;
import com.sprint.project.findex.exception.ApiException;
import com.sprint.project.findex.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(
      ApiException e
  ) {
    ErrorCode errorCode = e.getErrorCode();
    return ResponseEntity.status(errorCode.getStatus())
        .body(
            new ErrorResponse(
                Instant.now(),
                errorCode.getStatus(),
                errorCode.getMessage(),
                e.getDetail()
            )
        );
  }
}

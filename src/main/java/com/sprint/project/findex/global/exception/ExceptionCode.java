package com.sprint.project.findex.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
  OPEN_API_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Open API 데이터 요청에 실패하였습니다.(%s)");


  private HttpStatus statusCode;
  private String message;

  ExceptionCode(HttpStatus statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}

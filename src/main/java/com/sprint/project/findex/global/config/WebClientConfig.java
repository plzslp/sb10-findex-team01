package com.sprint.project.findex.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
//@ConfigurationProperties()
public class WebClientConfig {

  @Value("${OPEN_API_END_POINT}")
  private String apiEndPoint;

  // WebClient Bean을 등록한다.
  // todo: 명세에 맞게 쿼리 파라미터를 추가해야 함
  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder
        .baseUrl(apiEndPoint)
        .defaultHeaders(httpHeaders -> {
          httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        })
        .build();
  }
}

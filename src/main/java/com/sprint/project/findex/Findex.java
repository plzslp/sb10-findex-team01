package com.sprint.project.findex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Findex {

  public static void main(String[] args) {
    SpringApplication.run(Findex.class, args);
  }
}

package com.sprint.project.findex.repository.projection;

public interface DashboardRankingProjection {
  Long getId();
  String getIndexClassification();
  String getIndexName();
  Double getCurrentClosingPrice();
  Double getBeforeClosingPrice();
}

package com.sprint.project.findex.repository;

import com.sprint.project.findex.entity.IndexInfo;
import java.util.List;
import com.sprint.project.findex.repository.querydsl.IndexInfoQDSLRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>,
    IndexInfoQDSLRepository {

  List<IndexInfo> findByIdIn(List<Long> ids);
  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification,
      String indexName);
}

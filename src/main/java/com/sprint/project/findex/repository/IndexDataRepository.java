package com.sprint.project.findex.repository;

import com.sprint.project.findex.entity.DeletedStatus;
import com.sprint.project.findex.entity.IndexData;
import com.sprint.project.findex.entity.IndexInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexDataRepository extends JpaRepository<IndexData, Long>,
    IndexDataQDSLRepository {

  Optional<IndexData> findByIdAndIsDeleted(Long indexDataId, DeletedStatus deletedStatus);

  boolean existsByIndexInfoAndBaseDateAndIsDeleted(IndexInfo indexInfo, LocalDate baseDate,
      DeletedStatus deletedStatus);

  List<IndexData> findByIndexInfoAndBaseDateBetweenAndIsDeleted(IndexInfo indexInfo,
      LocalDate fromDate,
      LocalDate toDate,
      DeletedStatus deletedStatus);
}

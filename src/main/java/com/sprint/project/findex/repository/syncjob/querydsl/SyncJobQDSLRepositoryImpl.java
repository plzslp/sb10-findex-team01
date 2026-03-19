package com.sprint.project.findex.repository.syncjob.querydsl;

import static com.sprint.project.findex.entity.QSyncJob.syncJob;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.project.findex.dto.syncjob.SyncJobRequestQuery;
import com.sprint.project.findex.entity.SyncJob;
import com.sprint.project.findex.global.entity.JobType;
import com.sprint.project.findex.global.entity.ResultType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SyncJobQDSLRepositoryImpl implements SyncJobQDSLRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<SyncJob> search(SyncJobRequestQuery condition) {

    BooleanBuilder filterBuilder = builderFilterCondition(condition);

    boolean isDesc = "desc".equalsIgnoreCase(condition.sortDirection());

    if (condition.cursor() != null && condition.idAfter() != null) {
      BooleanBuilder cursorBuilder = new BooleanBuilder();

      if ("jobTime".equals(condition.sortField())) {
        // jobTime으로 정렬
        LocalDateTime cursorTime = LocalDateTime.parse(condition.cursor());

        if (isDesc) {
          cursorBuilder.or(syncJob.jobTime.lt(cursorTime));
          cursorBuilder.or(syncJob.jobTime.eq(cursorTime).and(syncJob.id.lt(condition.idAfter())));
        } else {
          cursorBuilder.or(syncJob.jobTime.gt(cursorTime));
          cursorBuilder.or(syncJob.jobTime.eq(cursorTime).and(syncJob.id.gt(condition.idAfter())));
        }

      } else {
        // targetDate로 정렬
        LocalDate cursorDate = LocalDate.parse(condition.cursor());

        if (isDesc) {
          cursorBuilder.or(syncJob.targetDate.lt(cursorDate));
          cursorBuilder.or(
              syncJob.targetDate.eq(cursorDate).and(syncJob.id.lt(condition.idAfter())));
        } else {
          cursorBuilder.or(syncJob.targetDate.gt(cursorDate));
          cursorBuilder.or(
              syncJob.targetDate.eq(cursorDate).and(syncJob.id.gt(condition.idAfter())));
        }

      }
      filterBuilder.and(cursorBuilder);
    }

    OrderSpecifier<?> orderSpecifier = "jobTime".equals(condition.sortField())
        ? (isDesc ? syncJob.jobTime.desc() : syncJob.jobTime.asc())
        : (isDesc ? syncJob.targetDate.desc() : syncJob.targetDate.asc());

    OrderSpecifier<Long> idOrder = isDesc
        ? syncJob.id.desc()
        : syncJob.id.asc();

    return queryFactory
        .selectFrom(syncJob)
        .where(filterBuilder)
        .orderBy(orderSpecifier, idOrder)
        .limit(condition.size() + 1)
        .fetch();
  }

  @Override
  public long countWithFilter(SyncJobRequestQuery condition) {
    BooleanBuilder filterBuilder = builderFilterCondition(condition);

    Long totalCount = queryFactory
        .select(syncJob.count())
        .from(syncJob)
        .where(filterBuilder)
        .fetchOne();

    return totalCount != null ? totalCount : 0L;
  }

  private BooleanBuilder builderFilterCondition(SyncJobRequestQuery condition) {
    BooleanBuilder filterBuilder = new BooleanBuilder();

    if (condition.jobType() != null) {
      filterBuilder.and(syncJob.jobType.eq(JobType.valueOf(condition.jobType().toString())));
    }

    if (condition.indexInfoId() != null) {
      filterBuilder.and(syncJob.indexInfo.id.eq(condition.indexInfoId()));
    }

    if (condition.baseDateFrom() != null && condition.baseDateTo() != null) {
      filterBuilder.and(
          syncJob.targetDate.between(condition.baseDateFrom(), condition.baseDateTo()));
    } else {
      if (condition.baseDateFrom() != null) {
        filterBuilder.and(syncJob.targetDate.goe(condition.baseDateFrom()));
      }
      if (condition.baseDateTo() != null) {
        filterBuilder.and(syncJob.targetDate.loe(condition.baseDateTo()));
      }
    }

    if (condition.worker() != null) {
      filterBuilder.and(syncJob.worker.contains(condition.worker()));
    }

    if (condition.jobTimeFrom() != null && condition.jobTimeTo() != null) {
      filterBuilder.and(syncJob.jobTime.between(condition.jobTimeFrom(), condition.jobTimeTo()));
    } else {
      if (condition.jobTimeFrom() != null) {
        filterBuilder.and(syncJob.jobTime.goe(condition.jobTimeFrom()));
      }
      if (condition.jobTimeTo() != null) {
        filterBuilder.and(syncJob.jobTime.loe(condition.jobTimeTo()));
      }
    }

    if (condition.status() != null) {
      filterBuilder.and(syncJob.result.eq(ResultType.valueOf(condition.status().toString())));
    }

    return filterBuilder;
  }
}

package com.sprint.project.findex.dto;

import com.sprint.project.findex.entity.IndexInfo;
import com.sprint.project.findex.entity.SourceType;
import java.math.BigInteger;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexDataDto(Long id, IndexInfo indexInfo, LocalDate baseDate,
                           SourceType sourceType, Double marketPrice, Double closingPrice,
                           Double highPrice, Double lowPrice, Double versus, Double fluctuationRate,
                           Double tradingQuantity, BigInteger tradingPrice,
                           BigInteger marketTotalAmount) {

}

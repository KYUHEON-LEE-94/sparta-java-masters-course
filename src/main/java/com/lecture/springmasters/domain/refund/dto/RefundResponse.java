package com.lecture.springmasters.domain.refund.dto;

import com.lecture.springmasters.common.constants.RefundStatus;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundResponse {

  Long id;
  BigDecimal refundAmount;
  String reason;
  RefundStatus status;

}

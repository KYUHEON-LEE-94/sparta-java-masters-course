package com.lecture.springmasters.domain.refund.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequest {

  Long userId;
  Long orderId;
  String reason;
  List<RefundOrderItemRequest> orderItems;

}

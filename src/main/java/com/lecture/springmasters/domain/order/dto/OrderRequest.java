package com.lecture.springmasters.domain.order.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

  Long userId;
  List<OrderProductRequest> products;

}

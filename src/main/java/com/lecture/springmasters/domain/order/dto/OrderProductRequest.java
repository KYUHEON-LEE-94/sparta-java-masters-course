package com.lecture.springmasters.domain.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProductRequest {

  Long id;
  Integer quantity;

}

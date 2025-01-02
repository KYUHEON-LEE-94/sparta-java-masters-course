package com.lecture.springmasters.domain.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : com.lecture.springmasters.domain.order.dto fileName       : OrderProductRequest
 * author         : LEE KYUHEON date           : 25. 1. 2. description    :
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProductRequest {

  Long id;
  Integer quantity;

}

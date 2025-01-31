package com.lecture.springmasters.domain.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDecreaseStockRequest {

  Long productId;

  Integer decreaseStock;

}

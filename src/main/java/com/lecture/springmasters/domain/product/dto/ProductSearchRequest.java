package com.lecture.springmasters.domain.product.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchRequest {

  String name;
  BigDecimal minPrice;
  BigDecimal maxPrice;
}

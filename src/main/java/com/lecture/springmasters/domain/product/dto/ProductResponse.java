package com.lecture.springmasters.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  String name;

  String description;

  BigDecimal price;

  Integer stock;

  Long categoryId;

  String categoryName;

  @QueryProjection
  public ProductResponse(
      String name,
      String description,
      BigDecimal price,
      Integer stock,
      Long categoryId,
      String categoryName
  ) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }
}

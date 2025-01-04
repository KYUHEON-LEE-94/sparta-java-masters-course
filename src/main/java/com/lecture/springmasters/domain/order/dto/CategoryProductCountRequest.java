package com.lecture.springmasters.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryProductCountRequest {

  String categoryName;
  String productName;

  @QueryProjection
  public CategoryProductCountRequest(String categoryName, String productName) {
    this.categoryName = categoryName;
    this.productName = productName;
  }
}

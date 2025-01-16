package com.lecture.springmasters.domain.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryProductCountResponse {

  String name;
  Long productCount;

  @QueryProjection
  public CategoryProductCountResponse(
      String name,
      Long productCount
  ) {
    this.name = name;
    this.productCount = productCount;
  }
}

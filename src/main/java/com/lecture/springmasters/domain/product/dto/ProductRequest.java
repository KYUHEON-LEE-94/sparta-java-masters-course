package com.lecture.springmasters.domain.product.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : com.lecture.springmasters.domain.product.dto fileName       : ProductRequest
 * author         : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

  String name;

  String description;

  BigDecimal price;

  Integer stock;

  Long CategoryId;
}

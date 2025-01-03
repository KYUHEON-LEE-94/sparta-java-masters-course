package com.lecture.springmasters.repository;

import static com.lecture.springmasters.entity.QProduct.product;

import com.lecture.springmasters.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<Product> search(String name, BigDecimal minPrice, BigDecimal maxPrice) {
    return queryFactory.selectFrom(product)
        .where(
            equalName(name), //null이 들어오면 이 조건절이 무시된
            equalPrice(minPrice, maxPrice)
        )
        .fetch();
  }

  private BooleanExpression equalName(String name) {
    return StringUtils.hasText(name) ? product.name.eq(name) : null;
  }

  private BooleanExpression equalPrice(BigDecimal minPrice, BigDecimal maxPrice) {
    return Objects.isNull(minPrice) && Objects.isNull(maxPrice)
        ? null : product.price.between(minPrice, maxPrice);
  }
}

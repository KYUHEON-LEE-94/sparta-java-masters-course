package com.lecture.springmasters.repository;

import static com.lecture.springmasters.entity.QCategory.category;
import static com.lecture.springmasters.entity.QProduct.product;

import com.lecture.springmasters.domain.product.dto.ProductResponse;
import com.lecture.springmasters.domain.product.dto.QProductResponse;
import com.lecture.springmasters.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<ProductResponse> search(String name, BigDecimal minPrice, BigDecimal maxPrice) {
    return queryFactory.select(new QProductResponse(
            product.name,
            product.description,
            product.price,
            product.stock,
            category.id,
            category.name
        ))
        .from(product)
        .innerJoin(category).on(product.category.eq(category))
        .where(
            equalName(name),
            betweenPrice(minPrice, maxPrice)
        )
        .fetch();
  }

  public Page<Product> searchPage(String name, BigDecimal minPrice, BigDecimal maxPrice,
      Pageable pageable) {
    JPAQuery<Product> query = queryFactory.selectFrom(product)
        .where(
            equalName(name), //null이 들어오면 이 조건절이 무시된
            betweenPrice(minPrice, maxPrice)
        ).orderBy(product.createdAt.desc());

    List<Product> result = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(result, pageable, query.fetchCount());
  }

  private BooleanExpression equalName(String name) {
    return StringUtils.hasText(name) ? product.name.contains(name) : null;
  }

  private BooleanExpression betweenPrice(BigDecimal minPrice, BigDecimal maxPrice) {
    return Objects.isNull(minPrice) && Objects.isNull(maxPrice)
        ? null : product.price.between(minPrice, maxPrice);
  }
}

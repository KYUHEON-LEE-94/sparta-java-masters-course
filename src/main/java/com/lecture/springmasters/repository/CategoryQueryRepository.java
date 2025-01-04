package com.lecture.springmasters.repository;

import static com.lecture.springmasters.entity.QCategory.category;
import static com.lecture.springmasters.entity.QProduct.product;

import com.lecture.springmasters.domain.order.dto.CategoryProductCountRequest;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountResponse;
import com.lecture.springmasters.domain.order.dto.QCategoryProductCountResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<CategoryProductCountResponse> productCount(CategoryProductCountRequest search) {
    return queryFactory.select(
            new QCategoryProductCountResponse(
                category.name,
                product.countDistinct()
            )
        ).from(category)
        .innerJoin(product).on(product.category.eq(category))
        .where(
            likeCategoryName(search.getCategoryName()),
            likeProductName(search.getProductName())
        )
        .groupBy(category.name)
        .fetch();
  }

  private BooleanExpression likeCategoryName(String name) {
    return StringUtils.hasText(name) ? category.name.contains(name) : null;
  }

  private BooleanExpression likeProductName(String name) {
    return StringUtils.hasText(name) ? product.name.contains(name) : null;
  }
}

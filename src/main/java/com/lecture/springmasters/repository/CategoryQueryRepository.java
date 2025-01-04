package com.lecture.springmasters.repository;

import static com.lecture.springmasters.entity.QCategory.category;
import static com.lecture.springmasters.entity.QProduct.product;

import com.lecture.springmasters.domain.order.dto.CategoryProductCountResponse;
import com.lecture.springmasters.domain.order.dto.QCategoryProductCountResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<CategoryProductCountResponse> productCount() {
    return queryFactory.select(
            new QCategoryProductCountResponse(
                category.name,
                product.countDistinct()
            )
        ).from(category)
        .innerJoin(product).on(product.category.eq(category))
        .groupBy(category.name)
        .fetch();
  }

}

package com.lecture.springmasters.domain.category.service;

import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountResponse;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.repository.CategoryQueryRepository;
import com.lecture.springmasters.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQueryRepository categoryQueryRepository;

  public Boolean save(CategoryRequest request) {
    categoryRepository.save(
        Category.builder()
            .name(request.getName())
            .build()
    );
    return true;
  }

  public List<CategoryProductCountResponse> productCounts() {
    return categoryQueryRepository.productCount();
  }
}

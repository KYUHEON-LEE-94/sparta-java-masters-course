package com.lecture.springmasters.domain.category.service;

import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Boolean save(CategoryRequest request) {
    categoryRepository.save(
        Category.builder()
            .name(request.getName())
            .build()
    );
    return true;
  }
}

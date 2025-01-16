package com.lecture.springmasters.domain.category.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.domain.category.service.CategoryService;
import com.lecture.springmasters.entity.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {


  private final CategoryService categoryService;


  @GetMapping
  public ApiResponse<List<Category>> findAll() {
    return ApiResponse.Success(categoryService.getAll());
  }


  @PostMapping("/product/count")
  public ApiResponse<Boolean> save(@RequestBody CategoryRequest request) {
    return ApiResponse.Success(categoryService.save(request));
  }


}

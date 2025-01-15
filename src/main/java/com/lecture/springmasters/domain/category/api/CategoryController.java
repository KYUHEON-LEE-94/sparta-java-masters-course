package com.lecture.springmasters.domain.category.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.common.service.RedisService;
import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.domain.category.service.CategoryService;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountRequest;
import com.lecture.springmasters.domain.order.dto.CategoryProductCountResponse;
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
  private final RedisService redisService;

  @GetMapping
  public ApiResponse<List<Category>> findAll() {
    List<Category> categories = categoryService.getALl();
    redisService.setObject("categories", categories);
    return ApiResponse.Success(
        redisService.findObject("categories", new TypeReference<>() {
        }));

  }

  @GetMapping("products/count")
  public ApiResponse<List<CategoryProductCountResponse>> productCounts(
      @RequestBody CategoryProductCountRequest search) {
    return ApiResponse.Success(categoryService.productCounts(search));
  }

  @PostMapping
  public ApiResponse<Boolean> save(@RequestBody CategoryRequest request) {
    Boolean save = categoryService.save(request);
    return ApiResponse.Success(save);
  }


}

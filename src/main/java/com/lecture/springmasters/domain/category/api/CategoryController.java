package com.lecture.springmasters.domain.category.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.category.dto.CategoryRequest;
import com.lecture.springmasters.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.lecture.springmasters.domain.category.api fileName       :
 * CategoryController author         : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ApiResponse<Boolean> save(@RequestBody CategoryRequest request) {
    Boolean save = categoryService.save(request);
    return ApiResponse.Success(save);
  }
}

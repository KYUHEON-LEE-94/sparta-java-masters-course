package com.lecture.springmasters.domain.product.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.product.dto.ProductRequest;
import com.lecture.springmasters.domain.product.dto.ProductResponse;
import com.lecture.springmasters.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.lecture.springmasters.domain.product.api fileName       : ProductController
 * author         : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ApiResponse<List<ProductResponse>> findAll() {
    List<ProductResponse> responses = productService.getAll();
    return ApiResponse.Success(responses);
  }

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> find(@PathVariable Long id) {
    ProductResponse responses = productService.getById(id);
    return ApiResponse.Success(responses);
  }


  @PostMapping
  public ApiResponse<Boolean> save(@RequestBody ProductRequest request) {
    log.info("request = {}", request);
    Boolean responses = productService.save(request);
    return ApiResponse.Success(responses);
  }


}

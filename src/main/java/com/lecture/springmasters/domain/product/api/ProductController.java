package com.lecture.springmasters.domain.product.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.product.dto.ProductDecreaseStockRequest;
import com.lecture.springmasters.domain.product.dto.ProductRequest;
import com.lecture.springmasters.domain.product.dto.ProductResponse;
import com.lecture.springmasters.domain.product.dto.ProductSearchRequest;
import com.lecture.springmasters.domain.product.service.ProductLockService;
import com.lecture.springmasters.domain.product.service.ProductService;
import com.lecture.springmasters.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
  private final ProductLockService productLockService;

  @GetMapping
  public ApiResponse<List<ProductResponse>> findAll() {
    List<ProductResponse> responses = productService.getAll();
    return ApiResponse.Success(responses);
  }

  @GetMapping("/searchPage")
  public ApiResponse<Page<Product>> searchPage(@ModelAttribute ProductSearchRequest request,
      Pageable pageable) {
    Page<Product> response = productService.searchPage(request, pageable);
    return ApiResponse.Success(response);
  }

  @GetMapping("/search")
  public ApiResponse<List<ProductResponse>> search(@ModelAttribute ProductSearchRequest request) {
    List<ProductResponse> response = productService.search(request);
    return ApiResponse.Success(response);
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

  @PatchMapping("/{id}/decrease")
  public ApiResponse<JSONObject> decreaseStock(@RequestBody ProductDecreaseStockRequest request) {
    productLockService.updateStockWithPessimisticLock(request.getProductId(),
        request.getDecreaseStock());
    return ApiResponse.Success(new JSONObject());
  }

  public ApiResponse<ProductResponse> update(@PathVariable Long id,
      @RequestBody ProductRequest request) {
    ProductResponse responses = productService.updateWriteBack(id, request);
    productService.asyncUpdateWriteBack(id, request);

    return ApiResponse.Success(responses);
  }
}

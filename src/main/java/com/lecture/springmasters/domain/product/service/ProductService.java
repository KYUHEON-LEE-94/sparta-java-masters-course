package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.product.dto.ProductRequest;
import com.lecture.springmasters.domain.product.dto.ProductResponse;
import com.lecture.springmasters.domain.product.dto.ProductSearchRequest;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.CategoryRepository;
import com.lecture.springmasters.repository.ProductQueryRepository;
import com.lecture.springmasters.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductQueryRepository productQueryRepository;
  private final CategoryRepository categoryRepository;

  public Boolean save(ProductRequest request) {
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));

    productRepository.save(
        Product.builder()
            .name(request.getName())
            .price(request.getPrice())
            .stock(request.getStock())
            .category(category)
            .build()
    );

    return true;
  }

  public ProductResponse getById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    return ProductResponse.builder()
        .name(product.getName())
        .price(product.getPrice())
        .stock(product.getStock())
        .description(product.getDescription())
        .build();
  }

  public List<ProductResponse> getAll() {
    List<Product> products = productRepository.findAll();
    return products.stream()
        .map(product -> ProductResponse.builder()
            .name(product.getName())
            .price(product.getPrice())
            .stock(product.getStock())
            .description(product.getDescription())
            .build())
        .toList();
  }

  public Page<Product> searchPage(ProductSearchRequest request, Pageable pageable) {
    return productQueryRepository.searchPage(request.getName(), request.getMinPrice(),
        request.getMaxPrice(),
        pageable);
  }

  public List
      <ProductResponse> search(ProductSearchRequest request) {
    return productQueryRepository.search(request.getName(), request.getMinPrice(),
        request.getMaxPrice());
  }
}

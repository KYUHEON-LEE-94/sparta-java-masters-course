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
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


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

  //인자로 받는 id를 key로 하겠다.
  @Cacheable(value = "productCache", key = "#id")
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

  @Transactional
  @CachePut(value = "productCache", key = "#id")
  public ProductResponse updateWriteThrough(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setDescription(request.getDescription());

    Product productRequest = productRepository.save(product);

    return ProductResponse.builder()
        .name(productRequest.getName())
        .price(productRequest.getPrice())
        .description(productRequest.getDescription())
        .build();
  }

  @CachePut(value = "productCache", key = "#id")
  public ProductResponse updateWriteBack(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setDescription(request.getDescription());

    return ProductResponse.builder()
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .build();
  }

  @Async
  @Transactional
  public void asyncUpdateWriteBack(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setDescription(request.getDescription());

    productRepository.save(product);
  }

  //Dirty Read
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public int getStockWithDirtyRead(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    return product.getStock();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void updateStockWithoutCommit(Long productId, int newStock) {
    Product product = productRepository.findById(productId).orElseThrow();
    product.setStock(newStock);
    productRepository.flush();

    try {
      // Dirty Read가 실행될 때까지 대기
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // 롤백 강제
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }

  // Non-Repeatable Read

  @Transactional(isolation = Isolation.READ_COMMITTED) // Non-Repeatable Read 허용
  public int getStockWithReadCommitted(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
    return product.getStock();
  }

  @Transactional
  public void updateStock(Long productId, int newStock) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
    product.setStock(newStock);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED) // Phantom Read 허용
  public List<Product> getProductsWithReadCommitted() {
    return productRepository.findAllByStockGreaterThan(90);
  }

  @Transactional
  public void insertNewProduct(String name, int stock) {
    Category category = categoryRepository.findById(1L)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    Product product = Product.builder()
        .name(name)
        .price(BigDecimal.ONE)
        .stock(stock)
        .category(category)
        .build();
    productRepository.save(product);
  }

  @Transactional
  public void updateStockWithoutLock(Long productId, int decrement) {
    // 동일 데이터 동시 접근으로 인한 Lost Update 발생 가능
    Product product = productRepository.findById(productId).orElseThrow();
    product.setStock(product.getStock() - decrement);
    productRepository.save(product);
  }
}


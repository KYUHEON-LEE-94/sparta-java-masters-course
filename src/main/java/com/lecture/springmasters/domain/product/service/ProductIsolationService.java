package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.entity.Category;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.CategoryRepository;
import com.lecture.springmasters.repository.ProductQueryRepository;
import com.lecture.springmasters.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@RequiredArgsConstructor
public class ProductIsolationService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductQueryRepository productQueryRepository;

  /**
   * Dirty Read
   **/
  @Transactional
  public void updateStockWithoutCommit(Long productId, Integer newStock) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT));

    product.setStock(newStock);
    productRepository.flush();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // 롤백 강제
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }

  // Dirty Read 방지
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public int getStockWithReadCommitted(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    return product.getStock();
  }

  /**
   * Non-Repeatable Read
   **/
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public int getStockWithRepeatableRead(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    System.out.println("First Read (Transaction A): Stock = " + product.getStock());

    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    Product result = productRepository.findById(productId).orElseThrow();
    return result.getStock();
  }

  @Transactional
  public void updateStock(Long productId, int newStock) {
    Product product = productRepository.findById(productId).orElseThrow();
    product.setStock(newStock);
  }

  /**
   * Phantom Read
   **/
  @Transactional(isolation = Isolation.SERIALIZABLE) // Phantom Read 허용
  public List<Product> getProductsWithSerializable() {
    List<Product> products = productRepository.findAllByStockGreaterThan(5);
    System.out.println("First Read (Transaction A): " + products.size() + " products");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return productRepository.findAllByStockGreaterThan(5);
  }

  @Transactional
  public void insertNewProduct(String name, int stock) {
    Category category = categoryRepository.findById(1L)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));

    Product product = Product.builder()
        .name(name)
        .price(BigDecimal.ONE)
        .stock(stock)
        .category(category)
        .build();
    productRepository.save(product);
  }

}

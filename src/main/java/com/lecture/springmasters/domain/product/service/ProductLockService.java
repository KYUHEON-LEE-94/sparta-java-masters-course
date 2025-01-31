package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLockService {

  private final ProductRepository productRepository;

  @Transactional
  public void updateStockWithPessimisticLock(Long productId, Integer quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }

  @Transactional
  public void updateStockWithOptimisticLock(String name, int quantity) {
    Product product = productRepository.findFirstByNameOrderById(name)
        .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }
}

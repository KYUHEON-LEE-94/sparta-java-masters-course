package com.lecture.springmasters.domain.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductIsolationServiceTest {

  @Autowired
  private ProductIsolationService productIsolationService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void testDirtyReadPrevented() throws InterruptedException {

    //given
    Product firstProduct = productRepository.findAll().get(0);
    Long productId = firstProduct.getId();

    Thread threadA = new Thread(() -> {
      productIsolationService.updateStockWithoutCommit(productId, 50);
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(1000);
        int stock = productIsolationService.getStockWithReadCommitted(productId);
        System.out.println("Read Committed: Stock = " + stock);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    // When
    threadA.start();
    threadB.start();

    threadA.join();
    threadB.join();

    // Then
    Product product = productRepository.findById(productId).orElseThrow();
    assertEquals(firstProduct.getStock(), product.getStock(),
        "Dirty Read 방지로 원래 값이 유지되어야 한다.");
  }

  @Test
  void testNonRepeatableReadPrevented() throws InterruptedException {
    // Given
    Product firstProduct = productRepository.findAll().get(0);
    Long productId = firstProduct.getId();

    Thread threadA = new Thread(() -> {
      // 트랜잭션 A: 데이터 조회
      int stock = productIsolationService.getStockWithRepeatableRead(productId);
      System.out.println("Second Read (Transaction A): Stock = " + stock);
    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(1000); // 트랜잭션 A의 첫 번째 읽기 이후 실행
        // 트랜잭션 B: 데이터 수정 및 커밋
        productIsolationService.updateStock(productId, 5);
        System.out.println("Transaction B: Updated Stock to 5");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    // When
    threadA.start();
    threadB.start();

    threadA.join();
    threadB.join();

    // Then
    Product product = productRepository.findById(productId).orElseThrow();
    assertEquals(5, product.getStock(),
        "트랜잭션 B의 변경이 최종적으로 반영되어야 함");
  }

  @Test
  void testPhantomReadPrevented() throws InterruptedException {
    // Given
    List<Product> firstProducts = productRepository.findAllByStockGreaterThan(5);

    Thread threadA = new Thread(() -> {
      // 트랜잭션 A: 조건에 맞는 데이터 조회
      List<Product> products = productIsolationService.getProductsWithSerializable();
      System.out.println("Second Read (Transaction A): " + products.size() + " products");
    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(1000); // 트랜잭션 A의 첫 번째 읽기 이후 실행
        // 트랜잭션 B: 새로운 데이터 삽입 시도
        productIsolationService.insertNewProduct("New Product", 10);
        System.out.println("Transaction B: Inserted New Product");
      } catch (Exception e) {
        Thread.currentThread().interrupt();
      }
    });

    // When
    threadA.start();
    threadB.start();

    threadA.join();
    threadB.join();

    // Then
    List<Product> finalProducts = productRepository.findAllByStockGreaterThan(5);
    System.out.println("final products : " + finalProducts.size() + " products");
    Assertions.assertEquals(firstProducts.size() + 1, finalProducts.size(),
        "트랜잭션 B의 삽입이 SERIALIZABLE로 차단되어야 함");
  }
}
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
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void testDirtyReadAllowed() throws InterruptedException {
// Given
    Product firstProduct = productRepository.findById(5L).get();
    Long productId = productRepository.findById(5L).get().getId();

    Thread threadA = new Thread(() -> {
      productService.updateStockWithoutCommit(productId, 10);

      try {
        Thread.sleep(1000); // 대기 후 롤백 예정
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(3000); // 트랜잭션 A 이후 실행
        // 트랜잭션 B: Dirty Read로 데이터를 읽음
        int stock = productService.getStockWithDirtyRead(productId);
        System.out.println("Dirty Read 발생: Stock = " + stock);
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
    assertEquals(firstProduct.getStock(), product.getStock(), "Dirty Read로 읽었지만 최종 값은 롤백되어야 함");
  }

  @Test
  void testNonRepeatableReadAllowed() throws InterruptedException {
    // Given
    Long productId = productRepository.findById(1L).get().getId();

    Thread threadA = new Thread(() -> {
      // 트랜잭션 A: 데이터 조회 (첫 번째 읽기)
      int stock = productService.getStockWithReadCommitted(productId);
      System.out.println("First Read (Transaction A): Stock = " + stock);

      try {
        Thread.sleep(3000); // 트랜잭션 B의 변경 대기
        // 트랜잭션 A: 데이터 조회 (두 번째 읽기)
        stock = productService.getStockWithReadCommitted(productId);
        System.out.println("Second Read (Transaction A): Stock = " + stock);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(1000); // 트랜잭션 A의 첫 번째 읽기 이후 실행
        // 트랜잭션 B: 데이터 수정 및 커밋
        productService.updateStock(productId, 5);
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
    Assertions.assertEquals(5, product.getStock(), "트랜잭션 B의 변경이 최종적으로 반영되어야 함");
  }

  @Test
  void testPhantomReadAllowed() throws InterruptedException {
    // Given
    List<Product> firstProducts = productRepository.findAllByStockGreaterThan(90);

    Thread threadA = new Thread(() -> {
      // 트랜잭션 A: 조건에 맞는 데이터 조회
      List<Product> products = productService.getProductsWithReadCommitted();
      System.out.println("First Read (Transaction A): " + products.size() + " products");

      try {
        Thread.sleep(3000); // 트랜잭션 B의 삽입 대기
        // 트랜잭션 A: 동일 조건으로 데이터 다시 조회
        products = productService.getProductsWithReadCommitted();
        System.out.println("Second Read (Transaction A): " + products.size() + " products");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    Thread threadB = new Thread(() -> {
      try {
        Thread.sleep(1000); // 트랜잭션 A의 첫 번째 읽기 이후 실행
        // 트랜잭션 B: 새로운 데이터 삽입 및 커밋
        productService.insertNewProduct("New Product", 92);
        System.out.println("Transaction B: Inserted New Product");
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
    List<Product> finalProducts = productRepository.findAllByStockGreaterThan(90);
    Assertions.assertEquals(firstProducts.size() + 1, finalProducts.size(),
        "트랜잭션 B가 삽입한 데이터가 반영되어야 함");
  }

  @Test
  void testLostUpdateWithoutLock() throws InterruptedException {
    Product FirstProduct = productRepository.findById(1L).get();
    Long productId = FirstProduct.getId();

    Thread threadA = new Thread(() -> {
      productService.updateStockWithoutLock(productId, 5);
      System.out.println("Transaction A: Reduced stock by 5");
    });

    Thread threadB = new Thread(() -> {
      productService.updateStockWithoutLock(productId, 3);
      System.out.println("Transaction B: Reduced stock by 3");
    });

    // 두 개의 스레드 동시 실행
    threadA.start();
    threadB.start();

    threadA.join();
    threadB.join();

    // 최종 값 확인
    Product product = productRepository.findById(productId).orElseThrow();
    System.out.println("First Stock: " + FirstProduct.getStock());
    System.out.println("Final Stock: " + product.getStock());
  }
}
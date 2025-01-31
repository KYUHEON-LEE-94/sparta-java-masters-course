package com.lecture.springmasters.domain.product.service;

import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductLockServiceTest {

  @Autowired
  private ProductLockService productLockService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void updateStockWithPessimisticLock() throws InterruptedException {
    //given
    Long productId = 101L;
    int threadCount = 2;

    Product firstProduct = productRepository.findById(productId).orElseThrow();

    //멀티스레드 환경을 제공하는 스레드 풀을 생성
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    //모든 스레드가 작업을 완료할 때까지 await()를 사용하여 기다림
    CountDownLatch latch = new CountDownLatch(threadCount);

    //when
    for (int i = 0; i < threadCount; i++) {
      // 각 스레드에서 updateStockWithPessimisticLock(productId, 1) 호출
      executorService.submit(() -> {
        try {
          productLockService.updateStockWithPessimisticLock(productId, 1);
        } finally {
          //모든 스레드가 끝나면  대기 중인 메인 스레드가 진행되도록 함
          latch.countDown();
        }
      });
    }

    latch.await();  // 모든 스레드가 작업을 마칠 때까지 대기

    //then
    Product product = productRepository.findById(productId).orElseThrow();
    Assertions.assertThat(product.getStock()).isEqualTo(firstProduct.getStock() - 2);
  }

  @Test
  public void updateStockWithOptimisticLock() throws InterruptedException {
    //given
    Long productId = 101L;
    int threadCount = 2;

    Product firstProduct = productRepository.findById(productId).orElseThrow();

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          productLockService.updateStockWithOptimisticLock(firstProduct.getName(), 1);
        } catch (Exception e) {
          System.out.println("낙관적 락 충돌 발생: " + e.getMessage());
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();  // 모든 스레드가 작업을 마칠 때까지 대기

    Product product = productRepository.findById(productId).orElseThrow();
    Assertions.assertThat(product.getStock())
        .isBetween(firstProduct.getStock() - 2, firstProduct.getStock() - 1); // 하나의 트랜잭션만 성공할 수도 있음
  }
}
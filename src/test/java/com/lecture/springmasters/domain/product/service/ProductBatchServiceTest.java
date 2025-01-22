package com.lecture.springmasters.domain.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductBatchServiceTest {

  @Autowired
  ProductBatchService productBatchService;

  @Test
  void saveProductFromCsv() {
    productBatchService.saveProductFromCsv(1L);
  }
}
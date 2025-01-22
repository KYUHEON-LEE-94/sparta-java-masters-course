package com.lecture.springmasters.domain.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserBatchServiceTest {

  @Autowired
  private UserBatchService userBatchService;

  @Test
  void getAllUsers() {
    userBatchService.batchInsertUsers();
  }
}
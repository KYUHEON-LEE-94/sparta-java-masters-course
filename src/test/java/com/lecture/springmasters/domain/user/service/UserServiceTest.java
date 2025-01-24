package com.lecture.springmasters.domain.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void getUserByEmail() {
    userService.findUserByEmail("1@naver.com");
  }

  @Test
  void getUserByNameAndCreatedAt() {
    userService.getUserByNameAndCreatedAt();
  }

  @Test
  void getUserByRequest() {
    userService.getUserByRequest();
  }
}

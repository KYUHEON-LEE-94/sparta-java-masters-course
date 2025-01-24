package com.lecture.springmasters.domain.user.service;

import jakarta.transaction.Transactional;
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

  @Test
  void searchUserIf() {
    userService.searchUserIf();
  }

  @Test
  void searchUserChoose() {
    userService.searchUserChoose();
  }

  @Test
  void searchUserForeach() {
    userService.searchUserForeach();
  }

  @Test
  void searchUserPage() {
    userService.searchUserPage();
  }

  @Test
  @Transactional
  void insertUser() {
    userService.insertUser();
  }

  @Test
  void updateUser() {
    userService.updateUser();
  }
}

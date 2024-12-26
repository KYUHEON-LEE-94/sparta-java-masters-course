package com.lecture.springmasters.domain.user.controller;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.user.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.lecture.springmasters.domain.message.controller fileName       :
 * UserController author         : LEE KYUHEON date           : 24. 12. 24. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 24.        LEE KYUHEON       최초 생성
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ApiResponse<List<Map<String, String>>> findUsers() {
    List<Map<String, String>> users = userService.getUsers();
    return ApiResponse.Success(users);
  }

  @GetMapping("/error")
  public String errorTest() {
    return userService.ThrowErrorTest();
  }

  @PostMapping
  public ApiResponse
      <List<Map<String, String>>> createUsers(
      @RequestBody HashMap<String, String> users) {
    List<Map<String, String>> responseUsers = userService.createUser(users);
    return ApiResponse.Success(responseUsers);
  }

  @PutMapping("/{username}")
  public ApiResponse
      <List<Map<String, String>>> updateUsers(
      @PathVariable String username,
      @RequestBody HashMap<String, String> password) {
    String ps = password.get("password");
    List<Map<String, String>> responseUsers = userService.updateUser(username, ps);
    return ApiResponse.Success(responseUsers);
  }
}

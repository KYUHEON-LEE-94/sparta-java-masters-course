package com.lecture.springmasters.domain.user.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.service.UserService;
import com.lecture.springmasters.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

  private final UserService userService;

  @GetMapping()
  public ApiResponse<List<User>> findAllUserById() {
    return ApiResponse.Success(userService.getAll());
  }

  @GetMapping("{id}")
  public ApiResponse<User> findUserById(@PathVariable Long id) {
    return ApiResponse.Success(userService.getById(id));
  }

  @GetMapping("response/{id}")
  public ApiResponse<UserResponse> findUserResponseById(@PathVariable Long id) {
    return ApiResponse.Success(userService.getUserResponseById(id));
  }

  @PostMapping
  public ApiResponse<User> create(@RequestBody UserRequest request) {
    return ApiResponse.Success(userService.save(request));
  }
}

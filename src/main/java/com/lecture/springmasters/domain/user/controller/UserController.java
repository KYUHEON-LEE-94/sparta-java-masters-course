package com.lecture.springmasters.domain.user.controller;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.user.dto.UserRequest;
import com.lecture.springmasters.domain.user.dto.UserResponse;
import com.lecture.springmasters.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.lecture.springmasters.domain.user.controller fileName       : UserController
 * author         : LEE KYUHEON date           : 24. 12. 28. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 28.        LEE KYUHEON       최초 생성
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.create(userRequest);
    return ApiResponse.Success(userResponse);
  }
}

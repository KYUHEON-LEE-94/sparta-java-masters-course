package com.lecture.springmasters.domain.auth.api;

import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  @GetMapping
  public ApiResponse<String> login(HttpSession session, LoginRequest request) {
    session.setAttribute("name", request.getName());
    session.setAttribute("email", request.getEmail());

    return ApiResponse.Success("Login Success. Session ID :" + session.getId());
  }

  @GetMapping("/status")
  public ApiResponse<String> checkStatus(HttpSession session) {
    String name = (String) session.getAttribute("name");

    if (ObjectUtils.isEmpty(name)) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USERS);
    }

    return ApiResponse.Success("Logged in as :" + name);
  }

  @GetMapping("/logout")
  public ApiResponse<String> logout(HttpSession session) {
    String name = (String) session.getAttribute("name");
    if (!ObjectUtils.isEmpty(name)) {
      session.setAttribute("name", null);
    }
    session.invalidate();
    return ApiResponse.Success("User logged out.");
  }
}

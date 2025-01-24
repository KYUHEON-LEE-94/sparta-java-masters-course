package com.lecture.springmasters.domain.user.dto;

import java.time.LocalDateTime;

public class UserDto {

  Long id;
  String name;
  String email;
  String password;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;

}

package com.lecture.springmasters.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : com.lecture.springmasters.domain.user.dto fileName       : UserRequest author :
 * LEE KYUHEON date           : 24. 12. 28. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 28.        LEE KYUHEON       최초 생성
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

  @NotBlank(message = "이름이 없습니다.")
  String username;
  String email;
  String phoneNumber;
  String passwordHash;
  String role;
}

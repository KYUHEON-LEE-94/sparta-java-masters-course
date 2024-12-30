package com.lecture.springmasters.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OrderRequest {

  @NotBlank(message = "로그인이 필요합니다.")
  String userId;

  @NotNull(message = "총 가격이 없습니다.")
  @PositiveOrZero(message = "총 가격은 마이너스입니다.")
  int totalPrice;

  @NotNull(message = "주문 상태 확인이 필요합니다")
  int status;
}

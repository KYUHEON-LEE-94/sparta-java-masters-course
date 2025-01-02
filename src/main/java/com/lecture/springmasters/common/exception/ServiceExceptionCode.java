package com.lecture.springmasters.common.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : com.lecture.springmasters.common.exception fileName       : ServiceExceptionCode
 * author         : LEE KYUHEON date           : 24. 12. 25. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 25.        LEE KYUHEON       최초 생성
 */
@Getter
@ToString
public enum ServiceExceptionCode {
  NOT_FOUND_PRODUCT("NOT_FOUND_PRODUCT", "상품을 찾을 수 없습니다."),
  NOT_FOUND_USERS("NOT_FOUND_USERS", "사용자를 찾을 수 없습니다."),
  NOT_FOUND_CATEGORY("NOT_FOUND_CATEGORY", "카테고리를 찾을 수 없습니다."),
  OUT_OF_STOCK_PRODUCT("OUT_OF_STOCK_PRODUCT", "상품을 찾을 수 없습니다."),
  NOT_FOUND_USER("NOT_FOUND_USER", "사용자를 찾을 수 없습니다."),
  ;
  private final String code;
  private final String message;

  ServiceExceptionCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}

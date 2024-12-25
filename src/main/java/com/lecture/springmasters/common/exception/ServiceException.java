package com.lecture.springmasters.common.exception;

import lombok.Getter;

/**
 * packageName    : com.lecture.springmasters.common.exception fileName       : ServiceException
 * author         : LEE KYUHEON date           : 24. 12. 25. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 25.        LEE KYUHEON       최초 생성
 */
@Getter
public class ServiceException extends RuntimeException {

  private String code;
  private String message;

  public ServiceException() {
  }

  public ServiceException(ServiceExceptionCode response) {
    super(response.getMessage());
    this.code = response.getCode();
    this.message = super.getMessage();
  }

  @Override
  public String getMessage() {
    return message;
  }
}

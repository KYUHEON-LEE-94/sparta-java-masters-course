package com.lecture.springmasters.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.lecture.springmasters.common.config fileName       : ApiResponse author :
 * LEE KYUHEON date           : 24. 12. 24. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 24.        LEE KYUHEON       최초 생성
 */
@Getter
public class ApiResponse<T> {

  private final Boolean reuslt;
  private final String error;
  private final String errorMessage;
  //어느 타입이든 받을 수 있도록 할것이다.
  private final T message;

  public ApiResponse(Boolean reuslt, String error, String errorMessage, T message) {
    this.reuslt = reuslt;
    this.error = error;
    this.errorMessage = errorMessage;
    this.message = message;
  }

  public static <T> ApiResponse<T> Success(T message) {
    return new ApiResponse<>(true, null, null, message);
  }

  /**
   * ResponseEntity의 상태값 활용을 하고 싶으니, 아래와 같이 ResponseEntity 를 감싸서 내리면 좋다.
   **/
  public static <T> ResponseEntity<ApiResponse<T>> ResponseSuccess(T message) {
    return ResponseEntity.ok(new ApiResponse<>(true, null, null, message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ResponseException(String code,
      String errorMessage) {
    return ResponseEntity.ok(new ApiResponse<>(false, code, errorMessage, null));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ValidException(String code,
      String errorMessage) {
    return ResponseEntity.ok(new ApiResponse<>(false, code, errorMessage, null));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ServerException(String code,
      String errorMessage) {
    return ResponseEntity.status(500)
        .body(new ApiResponse<>(false, code, errorMessage, null));
  }

  @Getter
  public static class Error {

    private final String errorCode;
    private final String errorMessage;

    @Builder
    public Error(String errorCode, String errorMessage) {
      this.errorCode = errorCode;
      this.errorMessage = errorMessage;
    }
  }
}

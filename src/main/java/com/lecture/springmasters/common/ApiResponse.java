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
  private final Error error;
  private final T message;

  public ApiResponse(Boolean reuslt, String errorCode, String errorMessage, T message) {
    this.reuslt = reuslt;
    this.error = Error.builder()
        .errorCode(errorCode)
        .errorMessage(errorMessage)
        .build();
    this.message = message;
  }

  public static <T> ApiResponse<T> Success(T message) {
    return new ApiResponse<>(true, null, null, message);
  }

  public static <T> ResponseEntity<ApiResponse<T>> ResponseException(String code,
      String errorMessage) {
    return ResponseEntity.ok(new ApiResponse<>(false, code, errorMessage, null));
  }

  /**
   * 위에서 반환되는 본문이 있기 때문에 ok로 return 보통 Front에서는 result 에러 메시지가 있는지 아닌지만 보는 경우가 있음 error code 보다는 본문에
   * 내용을 담아서 return 하는 경우 많음
   **/
  public static <T> ResponseEntity<ApiResponse<T>> ValidException(String code,
      String errorMessage) {
    return ResponseEntity.ok(new ApiResponse<>(false, code, errorMessage, null));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ServerException(String code,
      String errorMessage) {
    return ResponseEntity.ok(new ApiResponse<>(false, code, errorMessage, null));
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

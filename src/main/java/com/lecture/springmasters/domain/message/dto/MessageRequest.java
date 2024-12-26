package com.lecture.springmasters.domain.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.lecture.springmasters.domain.message.dto fileName       : MessageRequest
 * author : LEE KYUHEON date           : 24. 12. 26. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 26.        LEE KYUHEON       최초 생성
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRequest {

  @NotNull(message = "title은 필수입니다.")
  private String title;

  @NotBlank(message = "")
  @Setter
  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;


}

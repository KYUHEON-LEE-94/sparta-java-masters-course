package com.lecture.springmasters.domain.order.dto;


import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

  @NotNull(message = "회원정보가 없습니다.")
  Long userId;
  
  List<OrderProductRequest> products;

}

package com.lecture.springmasters.domain.order.api;


import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.domain.order.service.OrderService;
import com.lecture.springmasters.entity.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ApiResponse<Order> order(@Valid @RequestBody OrderRequest request) {
    return ApiResponse.Success(orderService.order(request));
  }

}

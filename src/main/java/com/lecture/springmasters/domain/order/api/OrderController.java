package com.lecture.springmasters.domain.order.api;


import com.lecture.springmasters.common.ApiResponse;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.domain.order.service.OrderService;
import com.lecture.springmasters.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ApiResponse<Order> order(@RequestBody OrderRequest request) {
    return ApiResponse.Success(orderService.order(request));
  }

}

package com.lecture.springmasters.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.order.dto.OrderProductRequest;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.OrderRepository;
import com.lecture.springmasters.repository.ProductRepository;
import com.lecture.springmasters.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class OrderProcessServiceTest {

  @Autowired
  private OrderProcessService orderProcessService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Test
  @Transactional
  void saveOrderItems_shouldOrderItemsSuccessfully() {
    //given
    User user = userRepository.findById(1L).get();
    Order order = orderRepository.save(
        Order.builder().user(user).totalPrice(BigDecimal.ZERO).build()
    );

    OrderProductRequest orderProductRequest1 = new OrderProductRequest();
    ReflectionTestUtils.setField(orderProductRequest1, "id", "1L");
    ReflectionTestUtils.setField(orderProductRequest1, "quantity", "3");

    OrderProductRequest orderProductRequest2 = new OrderProductRequest();
    ReflectionTestUtils.setField(orderProductRequest2, "id", "2L");
    ReflectionTestUtils.setField(orderProductRequest2, "quantity", "5");

    List<OrderProductRequest> orderProductRequests = Arrays.asList(orderProductRequest1,
        orderProductRequest2);

    OrderRequest orderRequest = new OrderRequest();
    ReflectionTestUtils.setField(orderRequest, "userId", user.getId());
    ReflectionTestUtils.setField(orderRequest, "products", orderProductRequests);

    //when & then
    ServiceException exception = assertThrows(
        ServiceException.class, () -> orderProcessService.createOrderItems(orderRequest, order)
    );

    assertEquals(ServiceExceptionCode.NOT_FOUND_PRODUCT.getCode(), exception.getCode());

  }
}
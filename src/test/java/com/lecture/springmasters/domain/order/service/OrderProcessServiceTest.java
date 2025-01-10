package com.lecture.springmasters.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.order.dto.OrderProductRequest;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.OrderItem;
import com.lecture.springmasters.entity.Product;
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

  @Test
  void shouldCalculateTotalPriceCorrectly() {
    // Given
    OrderItem item1 = new OrderItem(null, null, 2, BigDecimal.valueOf(100));
    OrderItem item2 = new OrderItem(null, null, 3, BigDecimal.valueOf(200));

    List<OrderItem> orderItems = Arrays.asList(item1, item2);
    BigDecimal expectedTotalPrice = BigDecimal.valueOf(800); // (2 * 100) + (3 * 200)

    // When
    BigDecimal totalPrice = orderProcessService.calculateTotalPrice(orderItems);

    // Then
    assertEquals(expectedTotalPrice, totalPrice);
  }

  @Test
  @Transactional
  void 요청_주문수량이_재고수량과_같은_경우() {
    //given
    User user = userRepository.findById(1L).get();
    Order order = orderRepository.save(
        Order.builder().user(user).totalPrice(BigDecimal.ZERO).build());

    Product product = productRepository.findById(1L).get();
    Integer stock = product.getStock();

    OrderProductRequest productRequest = new OrderProductRequest();
    ReflectionTestUtils.setField(productRequest, "id", 1L);
    ReflectionTestUtils.setField(productRequest, "quantity", stock);

    List<OrderProductRequest> orderProductRequests = Arrays.asList(productRequest);

    OrderRequest request = new OrderRequest();
    ReflectionTestUtils.setField(request, "userId", 1L);
    ReflectionTestUtils.setField(request, "products", orderProductRequests);

    // When
    List<OrderItem> orderItems = orderProcessService.createOrderItems(request, order);

    // Then
    assertEquals(product.getId(), orderItems.get(0).getProduct().getId());
    assertEquals(stock, orderItems.get(0).getQuantity());

  }


}
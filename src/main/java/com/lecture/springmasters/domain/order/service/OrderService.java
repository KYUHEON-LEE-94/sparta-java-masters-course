package com.lecture.springmasters.domain.order.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.OrderItem;
import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.OrderRepository;
import com.lecture.springmasters.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderProcessService orderProcessService;

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  @Transactional
  public Order order(OrderRequest request) {
    Order order = save(request.getUserId());
    List<OrderItem> orderItems = orderProcessService.createOrderItems(request, order);

    BigDecimal totalPrice = orderProcessService.calculateTotalPrice(orderItems);
    order.setTotalPrice(totalPrice);

    return order;
  }

  public Order save(Long userId) {
    User orderUser = getUser(userId);

    return orderRepository.save(Order.builder()
        .user(orderUser)
        .totalPrice(BigDecimal.ZERO)
        .build());
  }

  public Order update(Long orderId, Long userId) {
    User orderUser = getUser(userId);

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_ORDER));

    order.setUser(orderUser);
    order.setTotalPrice(BigDecimal.ZERO);
    return orderRepository.save(order);
  }

  public void cancelOrder(Long userId, Long orderId) {
    User orderUser = getUser(userId);

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_ORDER));

    if (orderUser.getId().equals(order.getUser().getId())) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
    }

    // TODO : 주문 취소 로직
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }
}

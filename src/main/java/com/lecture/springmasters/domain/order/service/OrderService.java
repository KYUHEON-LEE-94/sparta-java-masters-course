package com.lecture.springmasters.domain.order.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.order.dto.OrderProductRequest;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.OrderItem;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.OrderItemRepository;
import com.lecture.springmasters.repository.OrderRepository;
import com.lecture.springmasters.repository.ProductRepository;
import com.lecture.springmasters.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Transactional
  public Order order(OrderRequest request) {

    Order order = save(request.getUserId());

    orderRepository.flush();

    BigDecimal totalPrice = BigDecimal.ZERO;
    List<OrderItem> orderItems = new ArrayList<>();

    for (OrderProductRequest orderProduct : request.getProducts()) {
      Product product = productRepository.findById(orderProduct.getId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      if (orderProduct.getQuantity() > product.getStock()) {
        throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
      }

      product.reduceStock(orderProduct.getQuantity());
      OrderItem orderItem = OrderItem.builder()
          .product(product)
          .order(order)
          .quantity(orderProduct.getQuantity())
          .price(product.getPrice())
          .build();

      orderItems.add(orderItem);
      totalPrice = totalPrice.add(product.getPrice());
    }

    order.setTotalPrice(totalPrice);

    orderItemRepository.saveAll(orderItems);
    return order;
  }

  public Order save(Long userId) {
    User orderUser = userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    return orderRepository.save(Order.builder()
        .user(orderUser)
        .totalPrice(BigDecimal.ZERO)
        .build());
  }

}

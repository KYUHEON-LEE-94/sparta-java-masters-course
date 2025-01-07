package com.lecture.springmasters.domain.order.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.order.dto.OrderProductRequest;
import com.lecture.springmasters.domain.order.dto.OrderRequest;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.OrderItem;
import com.lecture.springmasters.entity.Product;
import com.lecture.springmasters.repository.OrderItemRepository;
import com.lecture.springmasters.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProcessService {

  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;

  public List<OrderItem> createOrderItems(OrderRequest request, Order order) {
    List<OrderItem> orderItems = new ArrayList<>();

    for (OrderProductRequest orderProduct : request.getProducts()) {
      Product product = productRepository.findById(orderProduct.getId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      validateStock(orderProduct, product);
      product.reduceStock(orderProduct.getQuantity());
      orderItems.add(buildOrderItem(order, product, orderProduct));
    }

    return orderItemRepository.saveAll(orderItems);
  }

  public BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(
            orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void validateStock(OrderProductRequest orderProduct, Product product) {
    if (orderProduct.getQuantity() > product.getStock()) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  private OrderItem buildOrderItem(Order order, Product product, OrderProductRequest orderProduct) {
    return OrderItem.builder()
        .product(product)
        .order(order)
        .quantity(orderProduct.getQuantity())
        .price(product.getPrice())
        .build();
  }
}

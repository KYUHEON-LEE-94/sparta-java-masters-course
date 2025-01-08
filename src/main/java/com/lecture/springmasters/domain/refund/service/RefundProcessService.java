package com.lecture.springmasters.domain.refund.service;

import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.refund.dto.RefundOrderItemRequest;
import com.lecture.springmasters.domain.refund.dto.RefundRequest;
import com.lecture.springmasters.entity.OrderItem;
import com.lecture.springmasters.entity.Refund;
import com.lecture.springmasters.entity.RefundItem;
import com.lecture.springmasters.repository.OrderItemRepository;
import com.lecture.springmasters.repository.RefundItemRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefundProcessService {

  private final RefundItemRepository refundItemRepository;
  private final OrderItemRepository orderItemRepository;

  @Transactional
  public Refund processRefund(Refund refund, RefundRequest request) {
    Map<Long, Integer> refundItemMap = buildRefundItemMap(request);
    List<OrderItem> orderItems = orderItemRepository.findAllById(refundItemMap.keySet());

    List<RefundItem> refundItems = new ArrayList<>();
    BigDecimal refundTotalPrice = BigDecimal.ZERO;

    for (OrderItem orderItem : orderItems) {
      int refundQuantity = refundItemMap.get(orderItem.getId());
      validateRefundQuantity(orderItem, refundQuantity);
      orderItem.setQuantity(orderItem.getQuantity() - refundQuantity);

      BigDecimal refundAmount = calculateRefundAmount(orderItem, refundQuantity);
      RefundItem refundItem = buildRefundItem(refund, orderItem, refundQuantity, refundAmount);

      refundItems.add(refundItem);
      refundTotalPrice = refundTotalPrice.add(refundAmount);
    }

    refundItemRepository.saveAll(refundItems);

    refund.setTotalPrice(refundTotalPrice);
    return refund;
  }


  private Map<Long, Integer> buildRefundItemMap(RefundRequest request) {
    return request.getOrderItems().stream()
        .collect(Collectors.toMap(
            RefundOrderItemRequest::getId,
            RefundOrderItemRequest::getRefundQuantity
        ));
  }

  private RefundItem buildRefundItem(Refund refund, OrderItem orderItem, Integer refundQuantity,
      BigDecimal refundAmount) {
    return RefundItem.builder()
        .refund(refund)
        .orderItem(orderItem)
        .quantity(refundQuantity)
        .price(refundAmount)
        .build();
  }

  private void validateRefundQuantity(OrderItem orderItem, Integer refundQuantity) {
    if (orderItem.getQuantity() < refundQuantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  private BigDecimal calculateRefundAmount(OrderItem orderItem, Integer refundQuantity) {
    if (orderItem.getQuantity().equals(0)) {
      return orderItem.getPrice();
    }
    BigDecimal unitPrice = orderItem.getPrice()
        .divide(BigDecimal.valueOf(orderItem.getQuantity()), 2, RoundingMode.HALF_UP);
    return unitPrice.multiply(BigDecimal.valueOf(refundQuantity));
  }

}

package com.lecture.springmasters.domain.refund.service;

import com.lecture.springmasters.common.constants.RefundStatus;
import com.lecture.springmasters.common.exception.ServiceException;
import com.lecture.springmasters.common.exception.ServiceExceptionCode;
import com.lecture.springmasters.domain.refund.dto.RefundRequest;
import com.lecture.springmasters.domain.refund.dto.RefundResponse;
import com.lecture.springmasters.domain.refund.mapper.RefundMapper;
import com.lecture.springmasters.entity.Order;
import com.lecture.springmasters.entity.Refund;
import com.lecture.springmasters.repository.OrderRepository;
import com.lecture.springmasters.repository.RefundRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

  private final RefundProcessService refundProcessService;

  private final RefundRepository refundRepository;
  private final OrderRepository orderRepository;

  public List<RefundResponse> findByUserId(Long userId) {
    return refundRepository.findAllByUser_Id(userId).stream()
        .map(RefundMapper.INSTANCE::toRefundResponse)
        .collect(Collectors.toList());
  }

  public RefundResponse refund(RefundRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_ORDER));

    if (!order.getUser().getId().equals(request.getUserId())) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
    }

    Refund refundRequest = buildRefund(order, request.getReason());

    try {
      approveRefund(refundRequest, request);
    } catch (Exception exception) {
      refundRequest.setStatus(RefundStatus.FAILED);
      refundRepository.save(refundRequest);
      throw exception;
    }

    return RefundMapper.INSTANCE.toRefundResponse(refundRequest);
  }

  private Refund buildRefund(Order order, String reason) {
    return refundRepository.save(Refund.builder()
        .user(order.getUser())
        .order(order)
        .totalPrice(BigDecimal.ZERO)
        .reason(reason)
        .status(RefundStatus.PANDING)
        .build());
  }

  private void approveRefund(Refund refund, RefundRequest request) {
    Refund processedRefund = refundProcessService.processRefund(refund, request);
    processedRefund.setStatus(RefundStatus.APPROVED);
    refundRepository.save(processedRefund);
  }
}

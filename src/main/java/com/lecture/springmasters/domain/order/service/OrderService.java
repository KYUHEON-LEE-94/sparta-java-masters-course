package com.lecture.springmasters.domain.order.service;

import com.lecture.springmasters.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.lecture.springmasters.domain.order.service fileName       : OrderService
 * author         : LEE KYUHEON date           : 24. 12. 28. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 28.        LEE KYUHEON       최초 생성
 */
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
}

package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.lecture.springmasters.repository fileName       : OrderItemRepository author
 * : LEE KYUHEON date           : 25. 1. 2. description    :
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}

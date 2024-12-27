package com.lecture.springmasters.domain.order.repository;

import com.lecture.springmasters.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * packageName    : com.lecture.springmasters.domain.products.repository fileName       :
 * ProductsRepository author         : LEE KYUHEON date           : 24. 12. 27. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 27.        LEE KYUHEON       최초 생성
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}

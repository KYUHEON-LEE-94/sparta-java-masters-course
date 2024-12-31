package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.lecture.springmasters.domain.repository fileName       : ProductRepository
 * author         : LEE KYUHEON date           : 24. 12. 31. description    :
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAllByCategory_Id(Long categoryId);

  @Query("SELECT o FROM User o JOIN FETCH o.orders")
  List<Product> findAllWithOrder();
}

package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  List<Product> findAllByStockGreaterThan(Integer stock);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :id")
  Optional<Product> findByIdForUpdate(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Product> findFirstByName(String name);

  Optional<Product> findFirstByNameOrderById(String name);
}

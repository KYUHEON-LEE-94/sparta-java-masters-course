package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT o FROM User o JOIN FETCH o.orders")
  List<User> findAllWithOrder();

}

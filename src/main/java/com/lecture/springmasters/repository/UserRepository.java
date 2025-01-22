package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT o FROM User o JOIN FETCH o.orders")
  List<User> findAllWithOrder();

  @Modifying
  @Query("UPDATE User u SET u.email = '탈퇴한 사용자' WHERE u.createdAt > :date")
  void bulkUpdateEmail(@Param("date") LocalDateTime date);

  @Modifying
  @Query("DELETE FROM User u WHERE u.createdAt > :date")
  void deleteOldUsers(@Param("date") LocalDateTime date);
}

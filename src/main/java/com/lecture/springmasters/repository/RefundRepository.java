package com.lecture.springmasters.repository;

import com.lecture.springmasters.entity.Refund;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

  List<Refund> findAllByUser_Id(Long userId);

}

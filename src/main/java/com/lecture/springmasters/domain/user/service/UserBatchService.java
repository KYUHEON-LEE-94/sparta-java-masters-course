package com.lecture.springmasters.domain.user.service;

import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBatchService {

  private final UserRepository userRepository;
  private final JdbcTemplate jdbcTemplate;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize;

  @Transactional
  public void batchInsertUsers() {
    List<User> users = new ArrayList<>();

    for (int i = 1; i <= 2000; i++) {
      User user = User.builder()
          .name("김철수" + i)
          .email(i + "@naver.com")
          .password(i + Math.random() + "")
          .build();

      users.add(user);

      if (i % batchSize == 0) {
        saveAll(users);
        users.clear();
      }
    }

    //남은게 있는지 검증
    if (!users.isEmpty()) {
      saveAll(users);
    }
  }

  private void saveAll(List<User> users) {
    String sql = "INSERT INTO users (username, email, password_hash, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    List<Object[]> batches = new ArrayList<>();
    for (User user : users) {
      Object[] args = new Object[]{
          user.getName(),
          user.getEmail(),
          user.getPassword(),
          LocalDateTime.now(),
          LocalDateTime.now()
      };

      batches.add(args);
    }

    jdbcTemplate.batchUpdate(sql, batches);
    log.info("Successfully inserted {} records", batches.size());
    log.info("query: {} ", sql);
  }

  @Transactional
  public void batchUpdateUsers() {
    //2025 - 01 - 20 00분 00시 00초
    LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2025, 1, 20), LocalTime.MIN);

    userRepository.bulkUpdateEmail(dateTime);
  }

  @Transactional
  public void batchDeleteUsers() {

    LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2025, 1, 20), LocalTime.MIN);

    userRepository.deleteOldUsers(dateTime);
  }
}

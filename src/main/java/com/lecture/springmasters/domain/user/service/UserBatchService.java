package com.lecture.springmasters.domain.user.service;

import com.lecture.springmasters.entity.User;
import com.lecture.springmasters.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBatchService {

  private final UserRepository userRepository;
  private final JdbcTemplate jdbcTemplate;

  private Integer batchSize = 100;

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

  public void saveAll(List<User> users) {
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

}

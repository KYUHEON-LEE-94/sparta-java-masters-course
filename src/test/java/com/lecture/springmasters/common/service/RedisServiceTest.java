package com.lecture.springmasters.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisServiceTest {

  @Autowired
  private RedisService redisService;

  @Test
  void redisTypeExample() {
    redisService.redisTypeExample();
  }

  @Test
  void redisStringExample() {
    redisService.redisStringExample();
  }

  @Test
  void redisListExample() {
    redisService.redisListExample();
  }

  @Test
  void redisHashExample() {
    redisService.redisHashExample();
  }

  @Test
  void redisSortedSetExample() {
    redisService.redisSortedSetExample();
  }
}
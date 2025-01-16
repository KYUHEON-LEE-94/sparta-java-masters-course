package com.lecture.springmasters.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

  private final Jedis jedis;
  private final ObjectMapper objectMapper;

  public <T> void setObject(String key, T object, Integer ttlInSeconds) {

    try {
      String jsonValue = objectMapper.writeValueAsString(object);
      jedis.setex(key, ttlInSeconds, jsonValue);
    } catch (JsonProcessingException e) {
      log.error("[RedisService] saveObject #{}", e.getMessage());
    }
  }

  public <T> T findObject(String key, TypeReference<T> type) {

    try {
      String jsonValue = jedis.get(key);
      return ObjectUtils.isEmpty(jsonValue) ? null : objectMapper.readValue(jsonValue, type);
    } catch (JsonProcessingException e) {
      log.error("[RedisService] getObject #{}", e.getMessage());
      return null;
    }
  }

  public void deleteData(String key) {
    jedis.del(key);
  }

  public void redisTypeExample() {
    log.info("Redis Type Example");

    jedis.set("username", "이철수");
    jedis.set("userAge", "20");

    String username = jedis.get("username");
    String userAge = jedis.get("userAge");

    log.info("[redisTypeExample] username:{}, userAge:{}]", username, userAge);

    //value 값을 증가
    jedis.incr("userAge");
    userAge = jedis.get("userAge");
    log.info("[redisTypeExample] username:{}, userAge:{}", username, userAge);

    jedis.decr("userAge");
    userAge = jedis.get("userAge");
    log.info("[redisTypeExample] username:{}, userAge:{}", username, userAge);

    //list
    //rpush: 입력 순서
    jedis.rpush("task1", "Task1", "Task2", "Task3");
    // Task1 -> Task2 -> Task3 순서로 저장
    // task1 = ["Task1", "Task2", "Task3"]

    //lpyush: 입력 반대
    jedis.lpush("task2", "Task1", "Task2", "Task3");
    // Task3 -> Task2 -> Task1 순서로 저장
    //task2 = ["Task3", "Task2", "Task1"]

    String task1 = jedis.lpop("task2"); //왼쪽부터 제거
    String task2 = jedis.rpop("task2"); // 오른쪽부터 제거

    log.info("[list example] 왼쪽:{}, 오른쪽:{}", task1, task2);

    //Set
    jedis.sadd("event1", "User1", "User2");
    jedis.sadd("event2", "User2", "User3");

    Set<String> commonUsers = jedis.sinter("event1", "event2");
    log.info("[list example] 교집합:{}", commonUsers);

    //Hash
    jedis.hset("userId", "name", "이철수");
    jedis.hset("userId", "email", "chlee@example.com");
    jedis.hset("userId", "age", "21");

    String userName = jedis.hget("userId", "name");
    Map<String, String> userId = jedis.hgetAll("userId");
    log.info("[hash example] userName: {}, userId: {}", userName, userId);

    //Sorted Set
    jedis.zadd("leaderboard", 1500, "Player1");
    jedis.zadd("leaderboard", 2000, "Player2");

    List<String> topPlayers = jedis.zrevrange("leaderboard", 0, 1); //상위 2명 조회
    List<String> bottomPlayers = jedis.zrange("leaderboard", 0, 1); //상위 2명 조회
    jedis.zincrby("leaderboard", 100, "Player1"); // Player1의 점수를 100 증가
    Double player1Score = jedis.zscore("leaderboard", "Player1");

    log.info("[Sorted Set] topPlayers: {}, player1Score: {}", topPlayers, player1Score);
  }

  public void redisStringExample() {
    jedis.set("user-{userId}-name", "김철수");

    String userName = jedis.get("user-{userId}-name");
    log.info("RedisStringExample userName:{}", userName);

    // 데이터 만료 설정 (1시간 후 만료)
    jedis.expire("user-{userId}-name", 3600); //1시간 = 60분 × 60초 = 3600초

    // 만료 확인
    Long ttl = jedis.ttl("user-{userId}-name");
    log.info("RedisStringExample ttl:{} seconds", ttl);
  }

  public void redisListExample() {
    jedis.lpush("taskQueue", "Task1", "Task2", "Task3");

    long queueSize = jedis.llen("taskQueue");
    log.info("queueSize : {}", queueSize);

    String task = jedis.rpop("taskQueue");
    log.info("Processing task : {}", task);

    // 대기열 남은 작업 확인
    log.info("taskQueue:{}", jedis.lrange("taskQueue", 0, -1));
  }

  public void redisHashExample() {
    jedis.hset("user-456", "name", "John");
    jedis.hset("user-456", "email", "john@example.com");
    jedis.hset("user-456", "age", "30");

    String name = jedis.hget("user-456", "name");
    log.info("RedisHashExample name :{}", name);

    log.info("RedisHashExample user info : {}", jedis.hgetAll("user-456"));

    Map<String, String> userProfile = jedis.hgetAll("user-456");
    log.info("RedisHashExample email : {}", userProfile.get("email"));
  }

  public void redisSortedSetExample() {
    jedis.zadd("leaderboard", 1500, "Player1");
    jedis.zadd("leaderboard", 2000, "Player2");
    jedis.zadd("leaderboard", 1200, "Player3");

    List<String> topPlayers = jedis.zrevrange("leaderboard", 0, 1);
    log.info("RedisSortedSetExample topPlayers Desc : {}", topPlayers);

    List<String> topPlayersAsc = jedis.zrange("leaderboard", 0, 1);
    log.info("RedisSortedSetExample topPlayers Asc: {}", topPlayersAsc);

    jedis.zincrby("leaderboard", 1000, "Player1");
    Double score = jedis.zscore("leaderboard", "Player1");
    log.info("RedisSortedSetExample score : {}", score);
  }
}

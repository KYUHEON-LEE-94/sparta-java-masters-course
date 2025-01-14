package com.lecture.springmasters.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public <T> void setObject(String key, T object) {

    try {
      String jsonValue = objectMapper.writeValueAsString(object);
      jedis.set(key, jsonValue);
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
}

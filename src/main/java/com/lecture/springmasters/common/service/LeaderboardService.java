package com.lecture.springmasters.common.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.resps.Tuple;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

  private final static String LEADERBOARD_KEY = "leaderboard";

  private final Jedis jedis;

  public void addPlayerScore(String player, Double score) {
    jedis.zadd(LEADERBOARD_KEY, score, player);
    log.info("Play {} updated with score {}", player, score);
  }

  public void getTopPlayers(Integer topN) {
    List<Tuple> topPlayers = jedis.zrangeWithScores(LEADERBOARD_KEY, 0, topN);

    int rank = 1;
    for (Tuple topPlayer : topPlayers) {
      log.info("{}. Player: {}, score: {}", rank, topPlayer.getElement(), topPlayer.getScore());
      rank++;
    }
  }

  //순위 확인
  public void getPlayerRank(String player) {
    Long rank = jedis.zrevrank(LEADERBOARD_KEY, player);
    if (Objects.nonNull(rank)) {
      log.info("{}. Rank: {}", player, rank);
    }
  }

  public void getPlayerScore(String player) {
    Double score = jedis.zscore(LEADERBOARD_KEY, player);
    if (Objects.nonNull(score)) {
      log.info("{}. Score: {}", player, score);
    }
  }

  //리더보드 전체 삭제
  public void resetLeaderboard() {
    jedis.del(LEADERBOARD_KEY);
  }

  public void batchUpdateScores() {
    Pipeline pipeline = jedis.pipelined();
    pipeline.zadd(LEADERBOARD_KEY, 300, "Player1");
    pipeline.zadd(LEADERBOARD_KEY, 250, "Player2");
    pipeline.zadd(LEADERBOARD_KEY, 350, "Player3");
    pipeline.sync(); //이 시점에 저장

    // 저장된 내용 확인
    List<String> topPlayers = jedis.zrevrange(LEADERBOARD_KEY, 0, 5);
    log.info("batchUpdateScores : {}", topPlayers);

    //TTL
    jedis.expire(LEADERBOARD_KEY, 86400); //24시간 유효
    long ttl = jedis.ttl(LEADERBOARD_KEY);
    log.info("batchUpdateScores ttl: {}", ttl);

    //start_at - end_at
    long currentTime = System.currentTimeMillis() / 1000; // 현재
    jedis.zadd("leaderboard:week", currentTime, "Player1");
    jedis.zadd("leaderboard:week", currentTime - (8 * 24 * 60 * 60), "Player2");

    long oneWeekAgo = currentTime - (7 * 24 * 60 * 60); // 일주일 전 날짜
    List<Tuple> weeklyTopPlayers = jedis.zrangeByScoreWithScores("leaderboard:week", oneWeekAgo,
        currentTime);

    log.info("batchUpdateScores Weekly Top Players: {}", weeklyTopPlayers);
  }


}

package com.lecture.springmasters.common.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
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

  //redis에게 event를 전달
  public void publishScoreUpdate(String player, Double score) {
    //pub-sub은 서로 다른 커넥션을 써야해서 아래와 같이 코딩
    try (Jedis publicJedis = new Jedis("localhost", 6379)) {
      String message = player + ":" + score;

      //Channel에 message를 넣는다. 이 채널로 이 메시지를 받을 수 있겟군!
      publicJedis.publish("score_update_channel", message);
      log.info("publishScoreUpdate {}", message);

    }
  }

  //Redis 이벤트 구독
  private final JedisPubSub pubSub = new JedisPubSub() {
    @Override
    public void onMessage(String channel, String message) {
      //메시지를 받으면 실행 되는 영역
      log.info("Received message: {}", message);
    }
  };

  //구독을 받는 메서드
  public void subscribeToScoreUpdates() {
    try (Jedis subscribeJedis = new Jedis("localhost", 6379)) {
      subscribeJedis.subscribe(pubSub, "score_update_channel");
    }
  }

  //구독을 끊는 용도
  public void unsubscribeFromScoreUpdates() {
    pubSub.unsubscribe();
    log.info("Unsubscribed from score updates.");
  }
}

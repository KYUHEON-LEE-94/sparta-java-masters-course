package com.lecture.springmasters.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeaderboardServiceTest {

  @Autowired
  private LeaderboardService leaderboardService;

  @Test
  void addPlayerScore() {
    leaderboardService.addPlayerScore("Player1", 100d);
  }

  @Test
  void getTopPlayers() {
    leaderboardService.getTopPlayers(2);
  }

  @Test
  void getPlayerRank() {
    leaderboardService.getPlayerRank("Player1");
  }

  @Test
  void getPlayerScore() {
    leaderboardService.getPlayerScore("Player3");
  }

  @Test
  void resetLeaderboard() {
    leaderboardService.resetLeaderboard();
  }

  @Test
  void batchUpdateScores() {
    leaderboardService.batchUpdateScores();
  }

  @Test
  void testPubSubFlow() throws InterruptedException {
    //구독 스레드 실행
    //subsribe하면 해당 스레드는 블락됨
    Thread subscribeThread = new Thread(() -> leaderboardService.subscribeToScoreUpdates());
    subscribeThread.start();

    //구독 준비 시간 확보
    Thread.sleep(1000);

    leaderboardService.publishScoreUpdate("Player1", 100d);

    // 메시지 처리를 기다림
    Thread.sleep(1000);

    //구독 종료
    leaderboardService.unsubscribeFromScoreUpdates();

  }
}
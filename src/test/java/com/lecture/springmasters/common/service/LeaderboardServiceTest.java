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
}
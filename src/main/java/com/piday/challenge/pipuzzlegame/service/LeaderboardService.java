package com.piday.challenge.pipuzzlegame.service;

import com.piday.challenge.pipuzzlegame.model.Leaderboard;
import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScoreService scoreService;

    @Autowired
    public LeaderboardService(LeaderboardRepository leaderboardRepository, ScoreService scoreService) {
        this.leaderboardRepository = leaderboardRepository;
        this.scoreService = scoreService;
    }

    public void saveTeamResults(Team team, int totalPoints, Duration totalTime) {
        long seconds = totalTime.getSeconds();
        String formattedTime = scoreService.formatDuration(totalTime);

        Leaderboard entry = new Leaderboard(
                team.getTeamName(),
                team.getGradeClass(),
                team.getMemberNames(),
                totalPoints,
                seconds,
                formattedTime
        );

        leaderboardRepository.save(entry);
    }

    public List<Leaderboard> getLeaderboard() {
        return leaderboardRepository.findAllByOrderByTotalPointsDescCompletionTimeSecondsAsc();
    }
}

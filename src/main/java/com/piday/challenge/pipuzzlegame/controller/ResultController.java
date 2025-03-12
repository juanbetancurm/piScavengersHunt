package com.piday.challenge.pipuzzlegame.controller;


import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.service.GameService;
import com.piday.challenge.pipuzzlegame.service.LeaderboardService;
import com.piday.challenge.pipuzzlegame.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.time.Duration;

@Controller
public class ResultController {

    private final GameService gameService;
    private final ScoreService scoreService;
    private final LeaderboardService leaderboardService;

    @Autowired
    public ResultController(GameService gameService, ScoreService scoreService,
                            LeaderboardService leaderboardService) {
        this.gameService = gameService;
        this.scoreService = scoreService;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/results")
    public String showResults(HttpSession session, Model model) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        Team team = gameService.findTeam(teamId);
        int totalPoints = gameService.getTotalPoints(teamId);
        Duration totalTime = gameService.getTotalTime(teamId);
        String formattedTime = scoreService.formatDuration(totalTime);

        // Save results to leaderboard
        leaderboardService.saveTeamResults(team, totalPoints, totalTime);

        model.addAttribute("team", team);
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("totalTime", formattedTime);

        return "results";
    }

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        model.addAttribute("entries", leaderboardService.getLeaderboard());
        return "leaderboard";
    }
}
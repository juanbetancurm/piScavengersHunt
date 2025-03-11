package com.piday.challenge.pipuzzlegame.service;

import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.model.Puzzle;
import com.piday.challenge.pipuzzlegame.model.TeamProgress;

import com.piday.challenge.pipuzzlegame.repository.PuzzleRepository;
import com.piday.challenge.pipuzzlegame.repository.TeamProgressRepository;
import com.piday.challenge.pipuzzlegame.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final TeamRepository teamRepository;
    private final PuzzleRepository puzzleRepository;
    private final TeamProgressRepository teamProgressRepository;
    private final ScoreService scoreService;

    @Autowired
    public GameService(TeamRepository teamRepository, PuzzleRepository puzzleRepository,
                       TeamProgressRepository teamProgressRepository, ScoreService scoreService) {
        this.teamRepository = teamRepository;
        this.puzzleRepository = puzzleRepository;
        this.teamProgressRepository = teamProgressRepository;
        this.scoreService = scoreService;
    }

    public Team registerTeam(String teamName, String gradeClass, String memberNames) {
        Team team = new Team(teamName, gradeClass, memberNames);
        return teamRepository.save(team);
    }

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElse(null);
    }

    public Puzzle getPuzzleBySequence(Integer sequenceNumber) {
        return puzzleRepository.findBySequenceNumber(sequenceNumber)
                .orElseThrow(() -> new RuntimeException("Puzzle not found"));
    }

    public boolean validateCode(String code, Integer sequenceNumber) {
        Optional<Puzzle> puzzle = puzzleRepository.findBySequenceNumber(sequenceNumber);
        return puzzle.map(p -> p.getCode().equalsIgnoreCase(code)).orElse(false);
    }

    public TeamProgress initiatePuzzleProgress(Team team, Integer puzzleSequence) {
        Puzzle puzzle = getPuzzleBySequence(puzzleSequence);
        Optional<TeamProgress> existingProgress = teamProgressRepository.findByTeamAndPuzzle(team, puzzle);

        if (existingProgress.isPresent()) {
            return existingProgress.get();
        } else {
            TeamProgress progress = new TeamProgress(team, puzzle);
            return teamProgressRepository.save(progress);
        }
    }

    public TeamProgress submitAnswer(Long teamId, Integer puzzleSequence, Character selectedOption) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Puzzle puzzle = getPuzzleBySequence(puzzleSequence);

        TeamProgress progress = teamProgressRepository.findByTeamAndPuzzle(team, puzzle)
                .orElseGet(() -> new TeamProgress(team, puzzle));

        boolean isCorrect = selectedOption.equals(puzzle.getCorrectOption());
        int points = scoreService.calculatePoints(isCorrect);

        progress.setSelectedOption(selectedOption);
        progress.setIsCorrect(isCorrect);
        progress.setPoints(points);
        progress.setCompletionTime(LocalDateTime.now());

        return teamProgressRepository.save(progress);
    }

    public List<TeamProgress> getTeamProgress(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return teamProgressRepository.findByTeamOrderByPuzzle_SequenceNumber(team);
    }

    public int getTotalPoints(Long teamId) {
        Integer totalPoints = teamProgressRepository.getTotalPointsByTeamId(teamId);
        return totalPoints != null ? totalPoints : 0;
    }

    public Duration getTotalTime(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        LocalDateTime endTime = team.getEndTime() != null ? team.getEndTime() : LocalDateTime.now();
        return Duration.between(team.getStartTime(), endTime);
    }

    public void completeGame(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setEndTime(LocalDateTime.now());
        teamRepository.save(team);
    }

    public int getCurrentPuzzleSequence(Long teamId) {
        List<TeamProgress> progressList = getTeamProgress(teamId);

        // Count completed puzzles
        long completedPuzzles = progressList.stream()
                .filter(p -> p.getCompletionTime() != null)
                .count();

        // Add 1 to get the next puzzle sequence number (1-indexed)
        return (int) completedPuzzles + 1;
    }

    public boolean isGameComplete(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Count completed puzzles
        List<TeamProgress> progressList = getTeamProgress(teamId);
        long completedPuzzles = progressList.stream()
                .filter(p -> p.getCompletionTime() != null)
                .count();

        // Get total number of puzzles
        long totalPuzzles = puzzleRepository.count();

        return completedPuzzles >= totalPuzzles;
    }
}
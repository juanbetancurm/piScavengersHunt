package com.piday.challenge.pipuzzlegame.controller;

import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.model.Puzzle;
import com.piday.challenge.pipuzzlegame.model.TeamProgress;
import com.piday.challenge.pipuzzlegame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/code-hint")
    public String showCodeHint(HttpSession session, Model model) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPosition = (int) session.getAttribute("currentPuzzleSequence");
        Team team = gameService.findTeam(teamId);

        if (currentPosition > 5) {
            // All puzzles completed, go to results
            gameService.completeGame(teamId);
            return "redirect:/results";
        }

        Puzzle puzzle = gameService.getPuzzleByTeamAndPosition(team, currentPosition);
        model.addAttribute("locationHint", puzzle.getLocationHint());
        model.addAttribute("puzzleNumber", currentPosition);

        return "code-hint";
    }


    @GetMapping("/code-input")
    public String showCodeInput(HttpSession session, Model model) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPuzzleSequence = (int) session.getAttribute("currentPuzzleSequence");
        model.addAttribute("puzzleNumber", currentPuzzleSequence);

        return "code-input";
    }

    @PostMapping("/check-code")
    public String checkCode(
            @RequestParam("code") String code,
            HttpSession session,
            Model model) {

        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPosition = (int) session.getAttribute("currentPuzzleSequence");
        Team team = gameService.findTeam(teamId);
        Puzzle puzzle = gameService.getPuzzleByTeamAndPosition(team, currentPosition);

        if (code.equalsIgnoreCase(puzzle.getCode())) {
            // Code is correct, initialize puzzle progress and move to puzzle
            gameService.initiatePuzzleProgress(team, puzzle.getSequenceNumber());
            return "redirect:/puzzle";
        } else {
            // Code is incorrect, show error message
            model.addAttribute("error", "Incorrect code. Please try again.");
            model.addAttribute("puzzleNumber", currentPosition);
            return "code-input";
        }
    }

    @GetMapping("/puzzle")
    public String showPuzzle(HttpSession session, Model model) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPosition = (int) session.getAttribute("currentPuzzleSequence");
        Team team = gameService.findTeam(teamId);

        // Get the puzzle based on the team's custom sequence and current position
        Puzzle puzzle = gameService.getPuzzleByTeamAndPosition(team, currentPosition);

        model.addAttribute("puzzle", puzzle);
        model.addAttribute("puzzleNumber", currentPosition);

        return "puzzle";
    }


    @PostMapping("/submit-answer")
    public String submitAnswer(
            @RequestParam("selectedOption") Character selectedOption,
            HttpSession session,
            Model model) {

        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPosition = (int) session.getAttribute("currentPuzzleSequence");
        Team team = gameService.findTeam(teamId);

        // Get the puzzle based on the team's custom sequence and current position
        Puzzle puzzle = gameService.getPuzzleByTeamAndPosition(team, currentPosition);

        // Submit the answer for the correct puzzle
        TeamProgress progress = gameService.submitAnswer(teamId, puzzle.getSequenceNumber(), selectedOption);

        // Prepare the puzzle result model
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("selectedOption", selectedOption);
        model.addAttribute("isCorrect", progress.getIsCorrect());
        model.addAttribute("points", progress.getPoints());
        model.addAttribute("correctOption", puzzle.getCorrectOption());
        model.addAttribute("puzzleNumber", currentPosition);

        // Determine if this was the last puzzle
        boolean isLastPuzzle = currentPosition >= 5;
        model.addAttribute("isLastPuzzle", isLastPuzzle);

        if (isLastPuzzle) {
            gameService.completeGame(teamId);
        }

        return "puzzle-result";
    }

    @GetMapping("/next-puzzle")
    public String nextPuzzle(HttpSession session) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPuzzleSequence = (int) session.getAttribute("currentPuzzleSequence");

        // Move to the next puzzle
        session.setAttribute("currentPuzzleSequence", currentPuzzleSequence + 1);

        // Check if all puzzles completed
        if (currentPuzzleSequence >= 5) {
            gameService.completeGame(teamId);
            return "redirect:/results";
        }

        return "redirect:/code-hint";
    }
}
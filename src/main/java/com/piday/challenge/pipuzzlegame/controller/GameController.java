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

        int currentPuzzleSequence = gameService.getCurrentPuzzleSequence(teamId);

        if (currentPuzzleSequence > 5) {
            // All puzzles completed, go to results
            gameService.completeGame(teamId);
            return "redirect:/results";
        }

        Puzzle puzzle = gameService.getPuzzleBySequence(currentPuzzleSequence);
        model.addAttribute("locationHint", puzzle.getLocationHint());
        model.addAttribute("puzzleNumber", currentPuzzleSequence);

        session.setAttribute("currentPuzzleSequence", currentPuzzleSequence);

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

        int currentPuzzleSequence = (int) session.getAttribute("currentPuzzleSequence");

        if (gameService.validateCode(code, currentPuzzleSequence)) {
            // Code is correct, initialize puzzle progress and move to puzzle
            Team team = gameService.findTeam(teamId);
            gameService.initiatePuzzleProgress(team, currentPuzzleSequence);
            return "redirect:/puzzle";
        } else {
            // Code is incorrect, show error message
            model.addAttribute("error", "Incorrect code. Please try again.");
            model.addAttribute("puzzleNumber", currentPuzzleSequence);
            return "code-input";
        }
    }

    @GetMapping("/puzzle")
    public String showPuzzle(HttpSession session, Model model) {
        Long teamId = (Long) session.getAttribute("teamId");
        if (teamId == null) {
            return "redirect:/";
        }

        int currentPuzzleSequence = (int) session.getAttribute("currentPuzzleSequence");
        Puzzle puzzle = gameService.getPuzzleBySequence(currentPuzzleSequence);

        model.addAttribute("puzzle", puzzle);
        model.addAttribute("puzzleNumber", currentPuzzleSequence);

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

        int currentPuzzleSequence = (int) session.getAttribute("currentPuzzleSequence");

        // Submit the answer and get the result
        TeamProgress progress = gameService.submitAnswer(teamId, currentPuzzleSequence, selectedOption);

        // Prepare the puzzle result model
        Puzzle puzzle = progress.getPuzzle();
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("selectedOption", selectedOption);
        model.addAttribute("isCorrect", progress.getIsCorrect());
        model.addAttribute("points", progress.getPoints());
        model.addAttribute("correctOption", puzzle.getCorrectOption());
        model.addAttribute("puzzleNumber", currentPuzzleSequence);

        // Determine if this was the last puzzle
        boolean isLastPuzzle = currentPuzzleSequence >= 5;
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
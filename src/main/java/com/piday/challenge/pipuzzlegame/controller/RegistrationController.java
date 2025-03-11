package com.piday.challenge.pipuzzlegame.controller;

import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegistrationController {

    private final GameService gameService;

    @Autowired
    public RegistrationController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerTeam(
            @RequestParam("teamName") String teamName,
            @RequestParam("gradeClass") String gradeClass,
            @RequestParam("memberNames") String memberNames,
            HttpSession session,
            Model model) {

        Team team = gameService.registerTeam(teamName, gradeClass, memberNames);
        session.setAttribute("teamId", team.getId());

        // Start with the first puzzle
        int puzzleSequence = 1;
        session.setAttribute("currentPuzzleSequence", puzzleSequence);

        return "redirect:/code-hint";
    }
}
package com.piday.challenge.pipuzzlegame;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Pi Puzzle Game API",
                version = "1.0",
                description = "A simple web-based game application that guides users through a series of puzzles about the number pi."
        )
)
public class PiPuzzleGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(PiPuzzleGameApplication.class, args);
    }
}
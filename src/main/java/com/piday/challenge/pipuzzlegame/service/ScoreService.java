package com.piday.challenge.pipuzzlegame.service;

import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    public int calculatePoints(boolean isCorrect) {
        return isCorrect ? 5 : 2;
    }

    public String formatDuration(java.time.Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        return String.format("%d min %d sec", minutes, seconds);
    }
}
package com.piday.challenge.pipuzzlegame.repository;

import com.piday.challenge.pipuzzlegame.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    // Find all entries ordered by points (desc) and time (asc)
    List<Leaderboard> findAllByOrderByTotalPointsDescCompletionTimeSecondsAsc();
}
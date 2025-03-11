package com.piday.challenge.pipuzzlegame.repository;

import com.piday.challenge.pipuzzlegame.model.Team;
import com.piday.challenge.pipuzzlegame.model.Puzzle;
import com.piday.challenge.pipuzzlegame.model.TeamProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamProgressRepository extends JpaRepository<TeamProgress, Long> {
    List<TeamProgress> findByTeamOrderByPuzzle_SequenceNumber(Team team);

    Optional<TeamProgress> findByTeamAndPuzzle(Team team, Puzzle puzzle);

    @Query("SELECT SUM(tp.points) FROM TeamProgress tp WHERE tp.team.id = :teamId")
    Integer getTotalPointsByTeamId(@Param("teamId") Long teamId);
}
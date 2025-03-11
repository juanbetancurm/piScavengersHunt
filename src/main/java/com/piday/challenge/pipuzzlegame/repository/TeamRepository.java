package com.piday.challenge.pipuzzlegame.repository;

import com.piday.challenge.pipuzzlegame.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
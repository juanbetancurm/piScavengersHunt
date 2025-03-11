package com.piday.challenge.pipuzzlegame.repository;

import com.piday.challenge.pipuzzlegame.model.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    Optional<Puzzle> findBySequenceNumber(Integer sequenceNumber);

    Optional<Puzzle> findByCode(String code);
}
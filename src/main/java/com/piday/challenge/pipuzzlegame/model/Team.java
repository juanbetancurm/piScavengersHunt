package com.piday.challenge.pipuzzlegame.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "grade_class", nullable = false)
    private String gradeClass;

    @Column(name = "member_names", nullable = false, columnDefinition = "TEXT")
    private String memberNames;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    //Puzzle sequence
    @Column(name = "puzzle_sequence", length = 100)
    private String puzzleSequence;
    // Constructors
    public Team() {
    }

    public Team(String teamName, String gradeClass, String memberNames) {
        this.teamName = teamName;
        this.gradeClass = gradeClass;
        this.memberNames = memberNames;
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getGradeClass() {
        return gradeClass;
    }

    public void setGradeClass(String gradeClass) {
        this.gradeClass = gradeClass;
    }

    public String getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(String memberNames) {
        this.memberNames = memberNames;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getPuzzleSequence() {
        return puzzleSequence;
    }
    public void setPuzzleSequence(String puzzleSequence) {
        this.puzzleSequence = puzzleSequence;
    }
}
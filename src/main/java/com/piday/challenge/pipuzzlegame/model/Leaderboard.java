package com.piday.challenge.pipuzzlegame.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "grade_class", nullable = false)
    private String gradeClass;

    @Column(name = "member_names", nullable = false, columnDefinition = "TEXT")
    private String memberNames;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "completion_time_seconds", nullable = false)
    private Long completionTimeSeconds;

    @Column(name = "formatted_time", nullable = false)
    private String formattedTime;

    @Column(name = "completion_date", nullable = false)
    private LocalDateTime completionDate;

    // Constructors
    public Leaderboard() {
    }

    public Leaderboard(String teamName, String gradeClass, String memberNames,
                       Integer totalPoints, Long completionTimeSeconds,
                       String formattedTime) {
        this.teamName = teamName;
        this.gradeClass = gradeClass;
        this.memberNames = memberNames;
        this.totalPoints = totalPoints;
        this.completionTimeSeconds = completionTimeSeconds;
        this.formattedTime = formattedTime;
        this.completionDate = LocalDateTime.now();
    }

    // Getters and setters

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

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getCompletionTimeSeconds() {
        return completionTimeSeconds;
    }

    public void setCompletionTimeSeconds(Long completionTimeSeconds) {
        this.completionTimeSeconds = completionTimeSeconds;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }
}

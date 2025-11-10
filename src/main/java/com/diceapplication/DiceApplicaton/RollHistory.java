package com.diceapplication.DiceApplicaton;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class RollHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player;

    private int result;

    private LocalDateTime rolledAt;

    public RollHistory() {}

    public RollHistory(String player, int result, LocalDateTime rolledAt) {
        this.player = player;
        this.result = result;
        this.rolledAt = rolledAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }

    public int getResult() { return result; }
    public void setResult(int result) { this.result = result; }

    public LocalDateTime getRolledAt() { return rolledAt; }
    public void setRolledAt(LocalDateTime rolledAt) { this.rolledAt = rolledAt; }
}

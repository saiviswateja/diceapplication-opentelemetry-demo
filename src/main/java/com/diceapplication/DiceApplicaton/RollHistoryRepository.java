package com.diceapplication.DiceApplicaton;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RollHistoryRepository extends JpaRepository<RollHistory, Long> {
    // Basic CRUD methods are inherited
}

package com.diceapplication.DiceApplicaton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class RollHistoryController {
    @Autowired
    private RollHistoryRepository historyRepository;

    @GetMapping
    public List<RollHistory> getAllRolls() {
        return historyRepository.findAll();
    }
}

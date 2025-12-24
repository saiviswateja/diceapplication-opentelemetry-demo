package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.HazelcastInstance;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class RollHistoryController {
    @Autowired
    private RollHistoryRepository historyRepository;

    @Autowired(required = false)
    private HazelcastInstance hazelcastInstance;

    /**
     * Get roll history from H2 database
     */
    @GetMapping
    public List<RollHistory> getAllRolls() {
        return historyRepository.findAll();
    }

    /**
     * Get roll history from Hazelcast cache (uses AbstractSerializationService for deserialization)
     */
    @GetMapping("/hazelcast")
    public List<RollHistory> getRollsFromHazelcast() {
        if (hazelcastInstance == null) {
            return new ArrayList<>(); // Return empty list if Hazelcast is disabled
        }
        // IMap operations automatically deserialize using AbstractSerializationService internally
        Map<String, RollHistory> map = hazelcastInstance.getMap("rollHistoryMap");
        Collection<RollHistory> values = map.values();
        return new ArrayList<>(values);
    }
}

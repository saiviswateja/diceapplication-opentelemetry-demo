package com.diceapplication.DiceApplicaton;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Component
public class RollService2
{
    private static final Logger logger = LoggerFactory.getLogger(RollService2.class);

    @Autowired
    private RollHistoryRepository historyRepo;

    @Autowired
    private HazelcastSerializationService hazelcastSerializationService;

    public String serviceRolled(@RequestParam("player") Optional<String> player) {
        int result = this.getRandomNumber(1, 6);

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        String playerVal = player.orElse(null);
        if (playerVal != null) {
            logger.info("{} is rolling the dice: {}", playerVal, result);
        } else {
            logger.info("Anonymous player is rolling the dice: {}", result);
        }
        // Save roll to db
        RollHistory history = new RollHistory(playerVal, result, LocalDateTime.now());
        
        // Use Hazelcast serialization service (which uses AbstractSerializationService internally)
        // Serialize the roll history using Hazelcast's SerializationService
        try {
            // Store in Hazelcast map - this triggers serialization using AbstractSerializationService internally
            hazelcastSerializationService.storeInHazelcastMap(history);
            // Explicitly serialize to demonstrate AbstractSerializationService usage
            hazelcastSerializationService.serializeRollHistory(history);
            logger.info("Roll history serialized and stored in Hazelcast using AbstractSerializationService");
        } catch (Exception e) {
            logger.warn("Hazelcast serialization failed, but continuing with DB save", e);
        }
        
        // Multiple database calls for sampling/tracing purposes
        // DB Call 1: Save the current roll
        RollHistory savedHistory = historyRepo.save(history);
        logger.info("Saved roll history with ID: {}", savedHistory.getId());
        
        // DB Call 2: Count total rolls for this player (or all rolls if player is null)
        long totalRolls;
        if (playerVal != null) {
            // Count rolls for specific player - using findAll and filter in memory
            // (For better performance, you could add a custom query method)
            totalRolls = historyRepo.findAll().stream()
                    .filter(h -> playerVal.equals(h.getPlayer()))
                    .count();
        } else {
            totalRolls = historyRepo.count();
        }
        logger.info("Total rolls count: {}", totalRolls);
        
        // DB Call 3: Find all recent rolls (last 10)
        java.util.List<RollHistory> recentRolls = historyRepo.findAll();
        if (recentRolls.size() > 10) {
            recentRolls = recentRolls.subList(Math.max(0, recentRolls.size() - 10), recentRolls.size());
        }
        logger.info("Retrieved {} recent rolls", recentRolls.size());
        
        // DB Call 4: Find the roll by ID (just saved)
        RollHistory foundHistory = historyRepo.findById(savedHistory.getId()).orElse(null);
        if (foundHistory != null) {
            logger.info("Verified saved roll: player={}, result={}", foundHistory.getPlayer(), foundHistory.getResult());
        }
        
        // DB Call 5: Check if player has previous rolls
        if (playerVal != null) {
            java.util.List<RollHistory> playerRolls = historyRepo.findAll().stream()
                    .filter(h -> playerVal.equals(h.getPlayer()))
                    .collect(java.util.stream.Collectors.toList());
            logger.info("Player {} has {} total rolls", playerVal, playerRolls.size());
        }
        
        // DB Call 6: Get all rolls and calculate average result
        java.util.List<RollHistory> allRolls = historyRepo.findAll();
        if (!allRolls.isEmpty()) {
            double averageResult = allRolls.stream()
                    .mapToInt(RollHistory::getResult)
                    .average()
                    .orElse(0.0);
            logger.info("Average dice result across all rolls: {}", averageResult);
        }
        
        return Integer.toString(result);
    }

    public int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}

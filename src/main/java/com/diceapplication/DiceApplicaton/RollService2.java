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
        historyRepo.save(history);
        return Integer.toString(result);
    }

    public int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}

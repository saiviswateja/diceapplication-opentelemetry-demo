package com.diceapplication.DiceApplicaton;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class RollService
{
    private static final Logger logger = LoggerFactory.getLogger(RollService.class);

    @Autowired
    RollService3 rollService3;

    @Transactional
    public String serviceRolled(Optional<String> player) {
        System.out.println("Came to the transactional service rolled function");
        return rollService3.serviceRolled(player);
    }


}

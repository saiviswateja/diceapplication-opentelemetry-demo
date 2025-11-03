package com.diceapplication.DiceApplicaton;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RollController {
    private static final Logger logger = LoggerFactory.getLogger(RollController.class);

    @Autowired
    RollService rollerService;

    @GetMapping("/rolldice")
    public String index(@RequestParam("player") Optional<String> player) throws InterruptedException
    {
        Thread.sleep(10000);

        return rollerService.serviceRolled(player);
    }

    @GetMapping("/lesstimerolldice")
    public String lesstimeRollDice(@RequestParam("player") Optional<String> player) throws InterruptedException
    {
        Thread.sleep(1000);

        return rollerService.serviceRolled(player);
    }

}
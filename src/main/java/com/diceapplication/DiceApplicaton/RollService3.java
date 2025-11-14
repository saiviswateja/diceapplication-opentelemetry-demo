package com.diceapplication.DiceApplicaton;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diceapplication.DiceApplicaton.subpackage.RollService4;

@Component
public class RollService3
{
    private static final Logger logger = LoggerFactory.getLogger(RollService3.class);

    @Autowired
    RollService4 rollService4;

    @MAOtelTrace
    public String serviceRolled(Optional<String> player) {
        System.out.println("Came to Roll Service 3");
        return rollService4.serviceRolled(player);
    }
}

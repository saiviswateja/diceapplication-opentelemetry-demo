package com.diceapplication.DiceApplicaton.subpackage;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.diceapplication.DiceApplicaton.RollService2;

@Component
public class RollService4
{
    private static final Logger logger = LoggerFactory.getLogger(RollService4.class);

    @Autowired
    RollService2 rollService2;

    @Transactional
    public String serviceRolled(Optional<String> player) {
        return rollService2.serviceRolled(player);
    }
}

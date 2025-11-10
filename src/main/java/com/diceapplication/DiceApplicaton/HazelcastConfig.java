package com.diceapplication.DiceApplicaton;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setInstanceName("dice-roll-hazelcast");
        
        // Configure map for storing roll history
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("rollHistoryMap");
        mapConfig.setTimeToLiveSeconds(3600); // 1 hour TTL
        
        config.addMapConfig(mapConfig);
        
        return Hazelcast.newHazelcastInstance(config);
    }
}


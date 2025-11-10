package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.HazelcastInstance;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HazelcastSerializationService {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastSerializationService.class);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * Serializes and stores a RollHistory object in Hazelcast IMap.
     * The Map.put() operation internally uses AbstractSerializationService
     * to serialize the RollHistory object before storing it.
     */
    public void serializeRollHistory(RollHistory rollHistory) {
        try {
            // Map operations automatically serialize objects using AbstractSerializationService internally
            // This demonstrates the use of AbstractSerializationService through Map operations
            Map<String, RollHistory> map = hazelcastInstance.getMap("rollHistoryMap");
            String key = rollHistory.getId() != null ? rollHistory.getId().toString() : 
                        System.currentTimeMillis() + "_" + (rollHistory.getPlayer() != null ? rollHistory.getPlayer() : "anonymous");
            
            // put() internally uses AbstractSerializationService to serialize the RollHistory object
            map.put(key, rollHistory);
            logger.info("RollHistory serialized and stored successfully using AbstractSerializationService (via Map.put())");
        } catch (Exception e) {
            logger.error("Error serializing/storing RollHistory in Hazelcast", e);
            throw new RuntimeException("Serialization failed", e);
        }
    }

    /**
     * Stores roll history in Hazelcast IMap using serialization
     * This is an alias for serializeRollHistory - both use AbstractSerializationService internally
     */
    public void storeInHazelcastMap(RollHistory rollHistory) {
        serializeRollHistory(rollHistory);
    }
}


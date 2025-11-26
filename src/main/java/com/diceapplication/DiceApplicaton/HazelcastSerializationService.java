package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
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

    /**
     * Collects cache statistics from all members in the Hazelcast cluster
     * @param cacheName The name of the cache/map to collect statistics for
     * @return Map containing member UUID as key and statistics string as value
     */
    public Map<String, String> collectCacheStats(String cacheName) {
        if (logger.isTraceEnabled()) {
            logger.trace(">> collectCacheStats()");
        }
        logger.warn("-- collectCacheStats() cacheName : {}", cacheName);
        System.out.println("Came here");

        IExecutorService executorService = hazelcastInstance.getExecutorService("default");
        Map<String, String> toRet = new HashMap<>();
        Map<Member, Future<String>> futures = executorService
                .submitToAllMembers(new CacheStatsAccessor(cacheName, null));

        logger.debug("-- collectCacheStats() futures : {}", futures);

        for (Member member : futures.keySet()) {
            try {
                toRet.put(member.getUuid().toString(), futures.get(member).get());
            } catch (InterruptedException | ExecutionException ex) {
                logger.error("Error collecting cache stats from member: {}", member.getUuid(), ex);
                throw new RuntimeException("Failed to collect cache statistics", ex);
            }
        }

        dummyMethod1();
        dummyMethod2();

        if (logger.isTraceEnabled()) {
            logger.trace("<< collectCacheStats()");
        }
        return toRet;
    }

    public Map<String, String> collectCacheStatsExecute(String cacheName)
    {
        if (logger.isTraceEnabled()) {
            logger.trace(">> collectCacheStatsExecute()");
        }
        logger.warn("-- collectCacheStatsExecute() cacheName : {}", cacheName);
      logger.warn("Came here to collectCacheStatsExecute");

        IExecutorService executorService = hazelcastInstance.getExecutorService("default");
        Map<String, String> toRet = new HashMap<>();
        toRet.put("Success", "Passed");

        executorService
                .execute(new CacheStatsAccessorRunnable(cacheName, null));

        dummyMethod1();
        dummyMethod2();

        if (logger.isTraceEnabled()) {
            logger.trace("<< collectCacheStats()");
        }
        return toRet;
    }

    public Map<String, String> collectCacheStatsSubmit(String cacheName) {

        IExecutorService executorService = hazelcastInstance.getExecutorService("default");

        Future<String> future =
                executorService.submit(new CacheStatsAccessor(cacheName, null));

        Map<String, String> result = new HashMap<>();

        try {
            String value = future.get();            // single result
            result.put("single-member", value);     // your choice of key
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Failed to collect cache statistics", ex);
        }

        dummyMethod1();
        dummyMethod2();

        return result;
    }

    public Map<String, String> collectCacheStatsSubmitToMembers(String cacheName) {

        IExecutorService executorService = hazelcastInstance.getExecutorService("default");

        // Select the members you want to run on
        Set<Member> members = hazelcastInstance.getCluster().getMembers();

        Map<Member, Future<String>> futures =
                executorService.submitToMembers(new CacheStatsAccessor(cacheName, null), members);

        Map<String, String> results = new HashMap<>();

        for (Map.Entry<Member, Future<String>> entry : futures.entrySet()) {
            Member member = entry.getKey();
            Future<String> future = entry.getValue();

            try {
                results.put(member.getUuid().toString(), future.get());
            } catch (Exception ex) {
                logger.error("Error collecting cache stats from member: {}", member.getUuid(), ex);
                throw new RuntimeException("Failed to collect cache statistics", ex);
            }
        }

        dummyMethod1();
        dummyMethod2();

        return results;
    }

    public void dummyMethod1()
    {
        System.out.println("Came to dummy method");
    }

    public void dummyMethod2()
    {
        System.out.println("Came to dummy method");
    }
}


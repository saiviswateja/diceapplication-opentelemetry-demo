package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

@Service
public class CacheOperationService {
    private static final Logger logger = LoggerFactory.getLogger(CacheOperationService.class);

    @Autowired(required = false)
    private HazelcastInstance hazelcastInstance;

    /**
     * Executes all cache operations (put, get, evict, removeAll, clear) via executor service
     * @param mapName The name of the map to operate on
     * @return Result string from all operations
     */
    public String executeAllOperations(String mapName) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute cache operations.";
        }
        logger.info("Executing all cache operations for map: {}", mapName);
        try {
            IExecutorService executorService = hazelcastInstance.getExecutorService("default");
            Callable<String> task = new AllInOneCacheOperationTest(mapName);
            Map<Member, Future<String>> futures = executorService.submitToAllMembers(task);
            
            StringBuilder result = new StringBuilder();
            result.append("Results from all members:\n");
            for (Member member : futures.keySet()) {
                try {
                    String memberResult = futures.get(member).get();
                    result.append("Member ").append(member.getUuid()).append(": ").append(memberResult).append("\n");
                } catch (InterruptedException | ExecutionException ex) {
                    logger.error("Error executing operations on member: {}", member.getUuid(), ex);
                    result.append("Member ").append(member.getUuid()).append(": Error - ").append(ex.getMessage()).append("\n");
                }
            }
            return result.toString();
        } catch (Exception e) {
            logger.error("Error executing all cache operations", e);
            throw new RuntimeException("Failed to execute cache operations", e);
        }
    }

    /**
     * Executes all cache operations directly on the local instance
     * @param mapName The name of the map to operate on
     * @return Result string from all operations
     */
    public String executeAllOperationsLocal(String mapName) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute cache operations.";
        }
        logger.info("Executing all cache operations locally for map: {}", mapName);
        try {
            AllInOneCacheOperationTest test = new AllInOneCacheOperationTest(mapName);
            return test.call();
        } catch (Exception e) {
            logger.error("Error executing cache operations locally", e);
            throw new RuntimeException("Failed to execute cache operations", e);
        }
    }

    /**
     * Executes PUT operation
     * @param mapName The name of the map
     * @param key The key to put
     * @param value The value to put
     * @return Result message
     */
    public String executePut(String mapName, String key, String value) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute PUT operation.";
        }
        logger.info("Executing PUT operation: map={}, key={}, value={}", mapName, key, value);
        try {
            IMap<String, String> map = hazelcastInstance.getMap(mapName);
            map.put(key, value);
            return "PUT executed: " + key + " -> " + value;
        } catch (Exception e) {
            logger.error("Error executing PUT operation", e);
            throw new RuntimeException("Failed to execute PUT operation", e);
        }
    }

    /**
     * Executes GET operation
     * @param mapName The name of the map
     * @param key The key to get
     * @return Result message with the value
     */
    public String executeGet(String mapName, String key) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute GET operation.";
        }
        logger.info("Executing GET operation: map={}, key={}", mapName, key);
        try {
            IMap<String, String> map = hazelcastInstance.getMap(mapName);
            String value = map.get(key);
            return "GET executed: " + key + " -> " + (value != null ? value : "null");
        } catch (Exception e) {
            logger.error("Error executing GET operation", e);
            throw new RuntimeException("Failed to execute GET operation", e);
        }
    }

    /**
     * Executes EVICT operation
     * @param mapName The name of the map
     * @param key The key to evict
     * @return Result message
     */
    public String executeEvict(String mapName, String key) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute EVICT operation.";
        }
        logger.info("Executing EVICT operation: map={}, key={}", mapName, key);
        try {
            IMap<String, String> map = hazelcastInstance.getMap(mapName);
            map.evict(key);
            return "EVICT executed: " + key + " evicted";
        } catch (Exception e) {
            logger.error("Error executing EVICT operation", e);
            throw new RuntimeException("Failed to execute EVICT operation", e);
        }
    }

    /**
     * Executes REMOVE ALL operation
     * @param mapName The name of the map
     * @return Result message
     */
    public String executeRemoveAll(String mapName) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute REMOVE ALL operation.";
        }
        logger.info("Executing REMOVE ALL operation: map={}", mapName);
        try {
            IMap<String, String> map = hazelcastInstance.getMap(mapName);
            // In client mode, use clear() or iterate and remove
            // removeAll with predicate may not be available in client API
            map.clear();
            return "REMOVE ALL executed: removed all entries (using clear)";
        } catch (Exception e) {
            logger.error("Error executing REMOVE ALL operation", e);
            throw new RuntimeException("Failed to execute REMOVE ALL operation", e);
        }
    }

    /**
     * Executes CLEAR operation
     * @param mapName The name of the map
     * @return Result message
     */
    public String executeClear(String mapName) {
        if (hazelcastInstance == null) {
            return "Hazelcast is disabled. Cannot execute CLEAR operation.";
        }
        logger.info("Executing CLEAR operation: map={}", mapName);
        try {
            IMap<String, String> map = hazelcastInstance.getMap(mapName);
            map.clear();
            return "CLEAR executed: map cleared";
        } catch (Exception e) {
            logger.error("Error executing CLEAR operation", e);
            throw new RuntimeException("Failed to execute CLEAR operation", e);
        }
    }
}


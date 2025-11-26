package com.diceapplication.DiceApplicaton;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheStatsController {
    
    @Autowired
    HazelcastSerializationService hazelcastSerializationService;

    @Autowired
    RollService2 rollService2;

    /**
     * Get cache statistics for a specific cache/map from all cluster members
     * @param cacheName The name of the cache/map (defaults to "rollHistoryMap" if not provided)
     * @return Map containing member UUID as key and statistics string as value
     */
    @GetMapping("/stats")
    public Map<String, String> getCacheStats(
            @RequestParam(value = "cacheName", required = false, defaultValue = "rollHistoryMap") String cacheName) {
        return hazelcastSerializationService.collectCacheStats(cacheName);
    }

    /**
     * Get cache statistics for a specific cache/map from all cluster members
     * @param cacheName The name of the cache/map (defaults to "rollHistoryMap" if not provided)
     * @return Map containing member UUID as key and statistics string as value
     */
    @GetMapping("/stats/execute")
    public Map<String, String> getCacheStatsExecute(
            @RequestParam(value = "cacheName", required = false, defaultValue = "rollHistoryMap") String cacheName) {
        return hazelcastSerializationService.collectCacheStatsExecute(cacheName);
    }

    /**
     * Get cache statistics for a specific cache/map from all cluster members
     * @param cacheName The name of the cache/map (defaults to "rollHistoryMap" if not provided)
     * @return Map containing member UUID as key and statistics string as value
     */
    @GetMapping("/stats/submit")
    public Map<String, String> getCacheStatsSubmit(
            @RequestParam(value = "cacheName", required = false, defaultValue = "rollHistoryMap") String cacheName) {
        return hazelcastSerializationService.collectCacheStatsSubmit(cacheName);
    }

    /**
     * Get cache statistics for a specific cache/map from all cluster members
     * @param cacheName The name of the cache/map (defaults to "rollHistoryMap" if not provided)
     * @return Map containing member UUID as key and statistics string as value
     */
    @GetMapping("/stats/submitToMembers")
    public Map<String, String> getCacheStatsSubmitSubmitToMembers(
            @RequestParam(value = "cacheName", required = false, defaultValue = "rollHistoryMap") String cacheName) {
        return hazelcastSerializationService.collectCacheStatsSubmitToMembers(cacheName);
    }
}


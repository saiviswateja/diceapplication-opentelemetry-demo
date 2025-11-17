package com.diceapplication.DiceApplicaton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache/operations")
public class CacheOperationController {

    @Autowired
    private CacheOperationService cacheOperationService;

    /**
     * Execute all cache operations (put, get, evict, removeAll, clear) via executor service
     * GET /api/cache/operations/all?mapName=testMap
     */
    @GetMapping("/all")
    public String executeAllOperations(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName) {
        return cacheOperationService.executeAllOperations(mapName);
    }

    /**
     * Execute all cache operations locally (not via executor service)
     * GET /api/cache/operations/all-local?mapName=testMap
     */
    @GetMapping("/all-local")
    public String executeAllOperationsLocal(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName) {
        return cacheOperationService.executeAllOperationsLocal(mapName);
    }

    /**
     * Execute PUT operation
     * POST /api/cache/operations/put?mapName=testMap&key=myKey&value=myValue
     */
    @PostMapping("/put")
    public String executePut(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName,
            @RequestParam("key") String key,
            @RequestParam("value") String value) {
        return cacheOperationService.executePut(mapName, key, value);
    }

    /**
     * Execute GET operation
     * GET /api/cache/operations/get?mapName=testMap&key=myKey
     */
    @GetMapping("/get")
    public String executeGet(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName,
            @RequestParam("key") String key) {
        return cacheOperationService.executeGet(mapName, key);
    }

    /**
     * Execute EVICT operation
     * DELETE /api/cache/operations/evict?mapName=testMap&key=myKey
     */
    @DeleteMapping("/evict")
    public String executeEvict(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName,
            @RequestParam("key") String key) {
        return cacheOperationService.executeEvict(mapName, key);
    }

    /**
     * Execute REMOVE ALL operation
     * DELETE /api/cache/operations/remove-all?mapName=testMap
     */
    @DeleteMapping("/remove-all")
    public String executeRemoveAll(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName) {
        return cacheOperationService.executeRemoveAll(mapName);
    }

    /**
     * Execute CLEAR operation
     * DELETE /api/cache/operations/clear?mapName=testMap
     */
    @DeleteMapping("/clear")
    public String executeClear(
            @RequestParam(value = "mapName", required = false, defaultValue = "rollHistoryMap") String mapName) {
        return cacheOperationService.executeClear(mapName);
    }
}


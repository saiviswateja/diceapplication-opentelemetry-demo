package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class AllInOneCacheOperationTest implements Callable<String>, Serializable
{

    private static final long serialVersionUID = 1L;

    private final String mapName;

    public AllInOneCacheOperationTest(String mapName)
    {
        this.mapName = mapName;
    }

    @Override
    public String call() throws Exception
    {

        HazelcastInstance hz = Hazelcast.getHazelcastInstanceByName("dice-roll-hazelcast");
        IMap<String, String> map = hz.getMap(mapName);

        StringBuilder sb = new StringBuilder();
        sb.append("Starting Hazelcast Cache Tests...\n");

        sb.append(callPut(map)).append("\n");
        sb.append(callGet(map)).append("\n");
        sb.append(callEvict(map)).append("\n");
        sb.append(callRemoveAll(map)).append("\n");
        sb.append(callClear(map)).append("\n");

        sb.append("All operations completed.");

        return sb.toString();
    }

    // --------------------------------------------------
    // 1. PUT
    // --------------------------------------------------
    public String callPut(IMap<String, String> map)
    {
        map.put("pKey1", "pValue1");
        map.put("pKey2", "pValue2");
        return "PUT executed: pKey1, pKey2 inserted";
    }

    // --------------------------------------------------
    // 2. GET
    // --------------------------------------------------
    public String callGet(IMap<String, String> map)
    {
        map.put("gKey1", "gValue1");
        String value = map.get("gKey1");
        return "GET executed: gKey1 -> " + value;
    }

    // --------------------------------------------------
    // 3. EVICT
    // --------------------------------------------------
    public String callEvict(IMap<String, String> map)
    {
        map.put("eKey1", "eValue1");
        map.evict("eKey1");
        return "EVICT executed: eKey1 evicted";
    }

    // --------------------------------------------------
    // 4. REMOVE ALL
    // --------------------------------------------------
    public String callRemoveAll(IMap<String, String> map)
    {
        map.put("r1", "v1");
        map.put("r2", "v2");
        map.removeAll(Predicates.alwaysTrue());
        return "REMOVE ALL executed: removed all entries";
    }

    // --------------------------------------------------
    // 5. CLEAR
    // --------------------------------------------------
    public String callClear(IMap<String, String> map)
    {
        map.put("c1", "v1");
        map.put("c2", "v2");
        map.clear();
        return "CLEAR executed: map cleared";
    }
}

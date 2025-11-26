package com.diceapplication.DiceApplicaton;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.LocalExecutorStats;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Transactional;

public class CacheStatsAccessor implements Callable<String>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String AVAILABILITY_CACHE = "dice-roll-hazelcast";
    
    private String mapName;
    private String executorName;

    public CacheStatsAccessor(String mapName, String executorName) {
        this.mapName = mapName;
        this.executorName = executorName;
    }

    @Override
    public String call() throws Exception {
        HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(AVAILABILITY_CACHE);

        textMethod();
        testTransactionalMethod2();
        testMethodWithoutTransactional();

        if (mapName != null) {
            IMap<String, Object> cache = hazelcastInstance.getMap(mapName);
            LocalMapStats mapStatistics = cache.getLocalMapStats();
            return mapStatistics.toString();
        } else {
            IExecutorService executorService = hazelcastInstance.getExecutorService(executorName);
            LocalExecutorStats executorStats = executorService.getLocalExecutorStats();
            return executorStats.toString();
        }
    }

    @Transactional
    public void textMethod()
    {
        System.out.println("Came to Text Method");
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void testTransactionalMethod2()
    {
        System.out.println("Came to Text Transactional Method");

        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void testMethodWithoutTransactional()
    {
        System.out.println("Came to Text Transactional without the transactional annotation");

        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}


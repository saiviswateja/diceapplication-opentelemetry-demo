package com.diceapplication.DiceApplicaton;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.LocalExecutorStats;

public class CacheStatsAccessorRunnable implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String AVAILABILITY_CACHE = "dice-roll-hazelcast";

    private String mapName;
    private String executorName;

    public CacheStatsAccessorRunnable(String mapName, String executorName) {
        this.mapName = mapName;
        this.executorName = executorName;
    }

    @Override
    public void run() {
        HazelcastInstance hazelcastInstance = Hazelcast.getHazelcastInstanceByName(AVAILABILITY_CACHE);

        textMethodRunnable();
        testTransactionalMethod2Runnable();
        testMethodWithoutTransactionalRunnable();

        if (mapName != null) {
            IMap<String, Object> cache = hazelcastInstance.getMap(mapName);
            LocalMapStats mapStatistics = cache.getLocalMapStats();
            System.out.println(mapStatistics.toString());
        } else {
            IExecutorService executorService = hazelcastInstance.getExecutorService(executorName);
            LocalExecutorStats executorStats = executorService.getLocalExecutorStats();
            System.out.println(executorStats.toString());
        }
    }

    @Transactional
    public void textMethodRunnable()
    {
        System.out.println("Came to Text Method Runnable");
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
    public void testTransactionalMethod2Runnable()
    {
        System.out.println("Came to Text Transactional Method Runnable");

        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void testMethodWithoutTransactionalRunnable()
    {
        System.out.println("Came to Text Transactional without the transactional annotation Runnable");

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


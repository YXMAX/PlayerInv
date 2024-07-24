package com.playerinv.Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.playerinv.PlayerInv.*;

public class PlaceHolderScheduler implements Runnable {

    public static void Placeholder_start(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(
                new PlaceHolderScheduler(),
                0,
                2,
                TimeUnit.HOURS);
    }


    @Override
    public void run() {
        Placeholder_List = new ArrayList<>();
        PlayerExpiryMap_Large = new HashMap<>();
        PlayerExpiryMap_Medium = new HashMap<>();
    }
}

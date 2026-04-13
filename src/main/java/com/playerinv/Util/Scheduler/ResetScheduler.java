package com.playerinv.Util.Scheduler;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import static com.playerinv.PlayerInv.playerExpiryMap_Large;
import static com.playerinv.PlayerInv.playerExpiryMap_Medium;
import static com.playerinv.Util.InitUtil.scheduler;
import static com.playerinv.Util.LoadUtil.Placeholder_VaultAmount;

public class ResetScheduler {

    public static void startCounter() {
        scheduler.scheduling().asyncScheduler().runAtFixedRate(task -> {
            Placeholder_VaultAmount = new HashMap<>();
            playerExpiryMap_Large = new HashMap<>();
            playerExpiryMap_Medium = new HashMap<>();
        }, Duration.ofMinutes(5), Duration.ofMinutes(5));
    }
}

package com.tabstats.event;

import com.tabstats.achievement.AchievementChecker;
import com.tabstats.data.PlayerStats;
import com.tabstats.data.StatsManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class KeyInputHandler {
    private static boolean wasTabListOpen = false;
    private static long lastSaveTime = System.currentTimeMillis();
    private static final long SAVE_INTERVAL = 30000;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean isTabListOpen = client.options.playerListKey.isPressed();

            if (isTabListOpen && !wasTabListOpen) {
                onTabPress();
            }

            if (isTabListOpen) {
                onTabOpen();
            } else if (wasTabListOpen) {
                onTabClose();
            }

            wasTabListOpen = isTabListOpen;

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSaveTime >= SAVE_INTERVAL) {
                StatsManager.saveStats();
                lastSaveTime = currentTime;
            }
        });
    }

    private static void onTabPress() {
        PlayerStats stats = StatsManager.getPlayerStats();
        stats.incrementTabPress();
        AchievementChecker.checkAchievements(MinecraftClient.getInstance(), stats);
    }

    private static void onTabOpen() {
        PlayerStats stats = StatsManager.getPlayerStats();
        stats.startTabOpen();
        stats.updateOpenTime();
    }

    private static void onTabClose() {
        PlayerStats stats = StatsManager.getPlayerStats();
        stats.endTabOpen();
        AchievementChecker.checkAchievements(MinecraftClient.getInstance(), stats);
        StatsManager.saveStats();
    }
}

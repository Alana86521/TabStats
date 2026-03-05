package com.tabstats.event;

import com.tabstats.achievement.AchievementChecker;
import com.tabstats.data.PlayerStats;
import com.tabstats.data.StatsManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ServerNetworkHandler {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
            new Identifier("tabstats", "tab_press"),
            (server, player, handler, buf, responseSender) -> {
                server.execute(() -> {
                    PlayerStats stats = StatsManager.getPlayerStats(player.getUuid());
                    stats.incrementTabPress();
                    AchievementChecker.checkAchievements(player, stats);
                });
            }
        );

        ServerPlayNetworking.registerGlobalReceiver(
            new Identifier("tabstats", "tab_open"),
            (server, player, handler, buf, responseSender) -> {
                server.execute(() -> {
                    PlayerStats stats = StatsManager.getPlayerStats(player.getUuid());
                    stats.startTabOpen();
                });
            }
        );

        ServerPlayNetworking.registerGlobalReceiver(
            new Identifier("tabstats", "tab_close"),
            (server, player, handler, buf, responseSender) -> {
                server.execute(() -> {
                    PlayerStats stats = StatsManager.getPlayerStats(player.getUuid());
                    stats.endTabOpen();
                    AchievementChecker.checkAchievements(player, stats);
                    StatsManager.saveStats();
                });
            }
        );
    }
}

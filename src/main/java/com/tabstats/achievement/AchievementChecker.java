package com.tabstats.achievement;

import com.tabstats.data.PlayerStats;
import com.tabstats.data.StatsManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class AchievementChecker {
    public static void checkAchievements(MinecraftClient client, PlayerStats stats) {
        if (client.player == null) return;
        
        List<Achievement> achievements = AchievementRegistry.getAllAchievements();
        
        for (Achievement achievement : achievements) {
            if (!stats.hasAchievement(achievement.getId())) {
                boolean unlocked = false;
                
                if (achievement.getType() == Achievement.AchievementType.TAB_PRESSES) {
                    if (stats.getTotalTabPresses() >= achievement.getRequirement()) {
                        unlocked = true;
                    }
                } else if (achievement.getType() == Achievement.AchievementType.OPEN_TIME) {
                    long totalMinutes = stats.getTotalOpenTime() / 60000;
                    if (totalMinutes >= achievement.getRequirement()) {
                        unlocked = true;
                    }
                }
                
                if (unlocked) {
                    stats.unlockAchievement(achievement.getId());
                    notifyAchievement(client, achievement);
                    StatsManager.saveStats();
                }
            }
        }
    }
    
    private static void notifyAchievement(MinecraftClient client, Achievement achievement) {
        if (client.player == null) return;
        client.player.sendMessage(Text.literal("§6§l✦ Achievement Unlocked! ✦"), false);
        client.player.sendMessage(Text.literal("§e" + achievement.getTitle()), false);
        client.player.sendMessage(Text.literal("§7" + achievement.getDescription()), false);
    }
}

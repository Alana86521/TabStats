package com.tabstats.achievement;

import com.tabstats.data.PlayerStats;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;

public class AchievementChecker {
    public static void checkAchievements(ServerPlayerEntity player, PlayerStats stats) {
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
                    notifyAchievement(player, achievement);
                }
            }
        }
    }
    
    private static void notifyAchievement(ServerPlayerEntity player, Achievement achievement) {
        player.sendMessage(Text.literal("§6§l✦ Achievement Unlocked! ✦"), false);
        player.sendMessage(Text.literal("§e" + achievement.getTitle()), false);
        player.sendMessage(Text.literal("§7" + achievement.getDescription()), false);
        
        player.getWorld().playSound(
            null,
            player.getBlockPos(),
            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
            SoundCategory.PLAYERS,
            1.0F,
            1.0F
        );
    }
}

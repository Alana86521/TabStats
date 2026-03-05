package com.tabstats.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tabstats.achievement.Achievement;
import com.tabstats.achievement.AchievementRegistry;
import com.tabstats.data.PlayerStats;
import com.tabstats.data.StatsManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Set;

public class TabStatsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("tabstats")
            .executes(TabStatsCommand::showStats)
            .then(ClientCommandManager.literal("achievements")
                .executes(TabStatsCommand::showAchievements))
            .then(ClientCommandManager.literal("reset")
                .executes(TabStatsCommand::resetSession))
        );
    }

    private static int showStats(CommandContext<FabricClientCommandSource> context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;

        PlayerStats stats = StatsManager.getPlayerStats();
        
        player.sendMessage(Text.literal("§8§m                                                    "), false);
        player.sendMessage(Text.literal("§6§lTabStats §7- Server: §e" + StatsManager.getCurrentServer()), false);
        player.sendMessage(Text.literal(""), false);
        
        player.sendMessage(Text.literal("§e§lTotal Statistics:"), false);
        player.sendMessage(Text.literal("  §7Tab Presses: §f" + formatNumber(stats.getTotalTabPresses())), false);
        player.sendMessage(Text.literal("  §7Time Open: §f" + formatTime(stats.getTotalOpenTime())), false);
        player.sendMessage(Text.literal(""), false);
        
        player.sendMessage(Text.literal("§b§lSession Statistics:"), false);
        player.sendMessage(Text.literal("  §7Tab Presses: §f" + formatNumber(stats.getSessionTabPresses())), false);
        player.sendMessage(Text.literal("  §7Time Open: §f" + formatTime(stats.getSessionOpenTime())), false);
        player.sendMessage(Text.literal(""), false);
        
        Set<String> unlocked = stats.getUnlockedAchievements();
        List<Achievement> allAchievements = AchievementRegistry.getAllAchievements();
        player.sendMessage(Text.literal("§d§lAchievements: §f" + unlocked.size() + "§7/§f" + allAchievements.size()), false);
        
        Text achievementsButton = Text.literal("§a§l[View Achievements]")
            .styled(style -> style
                .withClickEvent(new ClickEvent.RunCommand("/tabstats achievements"))
                .withHoverEvent(new HoverEvent.ShowText(Text.literal("§7Click to view achievements"))));
        player.sendMessage(achievementsButton, false);
        
        player.sendMessage(Text.literal("§8§m                                                    "), false);
        
        return 1;
    }

    private static int showAchievements(CommandContext<FabricClientCommandSource> context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;

        PlayerStats stats = StatsManager.getPlayerStats();
        Set<String> unlocked = stats.getUnlockedAchievements();
        List<Achievement> allAchievements = AchievementRegistry.getAllAchievements();
        
        player.sendMessage(Text.literal("§8§m                                                    "), false);
        player.sendMessage(Text.literal("§6§lTabStats Achievements §7(" + unlocked.size() + "/" + allAchievements.size() + ")"), false);
        player.sendMessage(Text.literal(""), false);
        
        player.sendMessage(Text.literal("§e§lTab Press Achievements:"), false);
        for (Achievement achievement : allAchievements) {
            if (achievement.getType() == Achievement.AchievementType.TAB_PRESSES) {
                displayAchievement(player, stats, achievement);
            }
        }
        
        player.sendMessage(Text.literal(""), false);
        player.sendMessage(Text.literal("§b§lTime Open Achievements:"), false);
        for (Achievement achievement : allAchievements) {
            if (achievement.getType() == Achievement.AchievementType.OPEN_TIME) {
                displayAchievement(player, stats, achievement);
            }
        }
        
        player.sendMessage(Text.literal("§8§m                                                    "), false);
        
        return 1;
    }

    private static void displayAchievement(ClientPlayerEntity player, PlayerStats stats, Achievement achievement) {
        boolean isUnlocked = stats.hasAchievement(achievement.getId());
        
        String status = isUnlocked ? "§a✔" : "§7✘";
        String title = isUnlocked ? achievement.getTitle() : "§8" + stripColors(achievement.getTitle());
        
        String progress = "";
        if (!isUnlocked) {
            if (achievement.getType() == Achievement.AchievementType.TAB_PRESSES) {
                int current = stats.getTotalTabPresses();
                int required = achievement.getRequirement();
                double percentage = Math.min(100.0, (current * 100.0) / required);
                progress = String.format(" §7(%.1f%%)", percentage);
            } else if (achievement.getType() == Achievement.AchievementType.OPEN_TIME) {
                long currentMinutes = stats.getTotalOpenTime() / 60000;
                int required = achievement.getRequirement();
                double percentage = Math.min(100.0, (currentMinutes * 100.0) / required);
                progress = String.format(" §7(%.1f%%)", percentage);
            }
        }
        
        Text achievementText = Text.literal("  " + status + " " + title + progress + " §8- " + achievement.getDescription());
        
        player.sendMessage(achievementText, false);
    }

    private static int resetSession(CommandContext<FabricClientCommandSource> context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;

        PlayerStats stats = StatsManager.getPlayerStats();
        stats.resetSession();
        
        player.sendMessage(Text.literal("§aSession statistics have been reset!"), false);
        
        return 1;
    }

    private static String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.2fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.2fK", number / 1000.0);
        }
        return String.valueOf(number);
    }

    private static String formatTime(long milliseconds) {
        double totalSeconds = milliseconds / 1000.0;
        long minutes = (long)(totalSeconds / 60);
        long hours = minutes / 60;
        
        if (hours > 0) {
            double remainingSeconds = totalSeconds - (hours * 3600) - ((minutes % 60) * 60);
            return String.format("%dh %dm %.2fs", hours, minutes % 60, remainingSeconds);
        } else if (minutes > 0) {
            double remainingSeconds = totalSeconds - (minutes * 60);
            return String.format("%dm %.2fs", minutes, remainingSeconds);
        } else {
            return String.format("%.2fs", totalSeconds);
        }
    }

    private static String stripColors(String text) {
        return text.replaceAll("§[0-9a-fk-or]", "");
    }
}

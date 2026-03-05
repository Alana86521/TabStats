package com.tabstats.achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementRegistry {
    private static final List<Achievement> ACHIEVEMENTS = new ArrayList<>();

    static {
        ACHIEVEMENTS.add(new Achievement("tab_10", "§6First Contact", "Press Tab 10 times", Achievement.AchievementType.TAB_PRESSES, 10));
        ACHIEVEMENTS.add(new Achievement("tab_50", "§6Button Masher", "Press Tab 50 times", Achievement.AchievementType.TAB_PRESSES, 50));
        ACHIEVEMENTS.add(new Achievement("tab_100", "§6Century Club", "Press Tab 100 times", Achievement.AchievementType.TAB_PRESSES, 100));
        ACHIEVEMENTS.add(new Achievement("tab_500", "§6Tab Warrior", "Press Tab 500 times", Achievement.AchievementType.TAB_PRESSES, 500));
        ACHIEVEMENTS.add(new Achievement("tab_1000", "§6Thousand Taps", "Press Tab 1,000 times", Achievement.AchievementType.TAB_PRESSES, 1000));
        ACHIEVEMENTS.add(new Achievement("tab_5000", "§6Tab Virtuoso", "Press Tab 5,000 times", Achievement.AchievementType.TAB_PRESSES, 5000));
        ACHIEVEMENTS.add(new Achievement("tab_10000", "§6Keyboard Conqueror", "Press Tab 10,000 times", Achievement.AchievementType.TAB_PRESSES, 10000));
        ACHIEVEMENTS.add(new Achievement("tab_50000", "§6Tab Titan", "Press Tab 50,000 times", Achievement.AchievementType.TAB_PRESSES, 50000));
        ACHIEVEMENTS.add(new Achievement("tab_100000", "§6Legendary Presser", "Press Tab 100,000 times", Achievement.AchievementType.TAB_PRESSES, 100000));
        ACHIEVEMENTS.add(new Achievement("tab_500000", "§6Tab Deity", "Press Tab 500,000 times", Achievement.AchievementType.TAB_PRESSES, 500000));
        ACHIEVEMENTS.add(new Achievement("tab_1000000", "§6Million Mile Milestone", "Press Tab 1,000,000 times", Achievement.AchievementType.TAB_PRESSES, 1000000));
        
        ACHIEVEMENTS.add(new Achievement("time_1", "§bGlimpse of Glory", "Keep Tab open for 1 minute total", Achievement.AchievementType.OPEN_TIME, 1));
        ACHIEVEMENTS.add(new Achievement("time_5", "§bCurious Gazer", "Keep Tab open for 5 minutes total", Achievement.AchievementType.OPEN_TIME, 5));
        ACHIEVEMENTS.add(new Achievement("time_10", "§bWatchful Eye", "Keep Tab open for 10 minutes total", Achievement.AchievementType.OPEN_TIME, 10));
        ACHIEVEMENTS.add(new Achievement("time_15", "§bPatient Observer", "Keep Tab open for 15 minutes total", Achievement.AchievementType.OPEN_TIME, 15));
        ACHIEVEMENTS.add(new Achievement("time_20", "§bTab Contemplator", "Keep Tab open for 20 minutes total", Achievement.AchievementType.OPEN_TIME, 20));
        ACHIEVEMENTS.add(new Achievement("time_30", "§bHalf Hour Hero", "Keep Tab open for 30 minutes total", Achievement.AchievementType.OPEN_TIME, 30));
        ACHIEVEMENTS.add(new Achievement("time_45", "§bDedicated Viewer", "Keep Tab open for 45 minutes total", Achievement.AchievementType.OPEN_TIME, 45));
        ACHIEVEMENTS.add(new Achievement("time_60", "§bHourly Devotee", "Keep Tab open for 1 hour total", Achievement.AchievementType.OPEN_TIME, 60));
        ACHIEVEMENTS.add(new Achievement("time_90", "§bTime Traveler", "Keep Tab open for 1.5 hours total", Achievement.AchievementType.OPEN_TIME, 90));
        ACHIEVEMENTS.add(new Achievement("time_120", "§bTwo Hour Titan", "Keep Tab open for 2 hours total", Achievement.AchievementType.OPEN_TIME, 120));
        ACHIEVEMENTS.add(new Achievement("time_180", "§bTriple Threat", "Keep Tab open for 3 hours total", Achievement.AchievementType.OPEN_TIME, 180));
        ACHIEVEMENTS.add(new Achievement("time_240", "§bQuadruple Champion", "Keep Tab open for 4 hours total", Achievement.AchievementType.OPEN_TIME, 240));
        ACHIEVEMENTS.add(new Achievement("time_300", "§bFive Hour Fanatic", "Keep Tab open for 5 hours total", Achievement.AchievementType.OPEN_TIME, 300));
        ACHIEVEMENTS.add(new Achievement("time_480", "§bMarathon Watcher", "Keep Tab open for 8 hours total", Achievement.AchievementType.OPEN_TIME, 480));
        ACHIEVEMENTS.add(new Achievement("time_720", "§bHalf Day Dedication", "Keep Tab open for 12 hours total", Achievement.AchievementType.OPEN_TIME, 720));
        ACHIEVEMENTS.add(new Achievement("time_1440", "§bFull Day Devotion", "Keep Tab open for 24 hours total", Achievement.AchievementType.OPEN_TIME, 1440));
    }

    public static List<Achievement> getAllAchievements() {
        return new ArrayList<>(ACHIEVEMENTS);
    }

    public static Achievement getAchievement(String id) {
        return ACHIEVEMENTS.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

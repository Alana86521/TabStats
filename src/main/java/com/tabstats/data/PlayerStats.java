package com.tabstats.data;

import net.minecraft.nbt.NbtCompound;
import java.util.HashSet;
import java.util.Set;

public class PlayerStats {
    private int totalTabPresses = 0;
    private int sessionTabPresses = 0;
    private long totalOpenTime = 0;
    private long sessionOpenTime = 0;
    private long lastOpenTimestamp = 0;
    private boolean isTabOpen = false;
    private Set<String> unlockedAchievements = new HashSet<>();

    public void incrementTabPress() {
        totalTabPresses++;
        sessionTabPresses++;
    }

    public void startTabOpen() {
        if (!isTabOpen) {
            isTabOpen = true;
            lastOpenTimestamp = System.currentTimeMillis();
        }
    }

    public void endTabOpen() {
        if (isTabOpen) {
            isTabOpen = false;
            long duration = System.currentTimeMillis() - lastOpenTimestamp;
            totalOpenTime += duration;
            sessionOpenTime += duration;
        }
    }

    public void updateOpenTime() {
        if (isTabOpen) {
            long currentTime = System.currentTimeMillis();
            long duration = currentTime - lastOpenTimestamp;
            totalOpenTime += duration;
            sessionOpenTime += duration;
            lastOpenTimestamp = currentTime;
        }
    }

    public void unlockAchievement(String achievementId) {
        unlockedAchievements.add(achievementId);
    }

    public boolean hasAchievement(String achievementId) {
        return unlockedAchievements.contains(achievementId);
    }

    public Set<String> getUnlockedAchievements() {
        return new HashSet<>(unlockedAchievements);
    }

    public int getTotalTabPresses() {
        return totalTabPresses;
    }

    public int getSessionTabPresses() {
        return sessionTabPresses;
    }

    public long getTotalOpenTime() {
        updateOpenTime();
        return totalOpenTime;
    }

    public long getSessionOpenTime() {
        updateOpenTime();
        return sessionOpenTime;
    }

    public void resetSession() {
        sessionTabPresses = 0;
        sessionOpenTime = 0;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        updateOpenTime();
        nbt.putInt("TotalTabPresses", totalTabPresses);
        nbt.putInt("SessionTabPresses", sessionTabPresses);
        nbt.putLong("TotalOpenTime", totalOpenTime);
        nbt.putLong("SessionOpenTime", sessionOpenTime);
        
        String[] achievements = unlockedAchievements.toArray(new String[0]);
        nbt.putInt("AchievementCount", achievements.length);
        for (int i = 0; i < achievements.length; i++) {
            nbt.putString("Achievement_" + i, achievements[i]);
        }
        
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        totalTabPresses = nbt.getInt("TotalTabPresses");
        sessionTabPresses = nbt.getInt("SessionTabPresses");
        totalOpenTime = nbt.getLong("TotalOpenTime");
        sessionOpenTime = nbt.getLong("SessionOpenTime");
        
        unlockedAchievements.clear();
        int count = nbt.getInt("AchievementCount");
        for (int i = 0; i < count; i++) {
            String achievement = nbt.getString("Achievement_" + i);
            if (!achievement.isEmpty()) {
                unlockedAchievements.add(achievement);
            }
        }
    }
}

package com.tabstats.achievement;

public class Achievement {
    private final String id;
    private final String title;
    private final String description;
    private final AchievementType type;
    private final int requirement;

    public Achievement(String id, String title, String description, AchievementType type, int requirement) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.requirement = requirement;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public AchievementType getType() {
        return type;
    }

    public int getRequirement() {
        return requirement;
    }

    public enum AchievementType {
        TAB_PRESSES,
        OPEN_TIME
    }
}

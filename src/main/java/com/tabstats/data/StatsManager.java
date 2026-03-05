package com.tabstats.data;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatsManager {
    private static final Map<String, PlayerStats> serverStatsMap = new HashMap<>();
    private static File saveDir;
    private static String currentServerAddress = "singleplayer";
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        saveDir = FabricLoader.getInstance().getConfigDir().resolve("tabstats").toFile();
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        initialized = true;
    }

    public static void setCurrentServer(String serverAddress) {
        if (serverAddress == null || serverAddress.isEmpty()) {
            currentServerAddress = "singleplayer";
        } else {
            currentServerAddress = extractBaseDomain(serverAddress);
        }
        loadStats();
    }

    private static String extractBaseDomain(String address) {
        String cleaned = address.replaceAll(":[0-9]+$", "");
        cleaned = cleaned.replaceAll("^/", "");
        
        String[] parts = cleaned.split("\\.");
        if (parts.length >= 2) {
            return (parts[parts.length - 2] + "." + parts[parts.length - 1]).toLowerCase();
        }
        return cleaned.replaceAll("[^a-zA-Z0-9.-]", "_").toLowerCase();
    }

    public static String getCurrentServer() {
        return currentServerAddress;
    }

    public static PlayerStats getPlayerStats() {
        initialize();
        return serverStatsMap.computeIfAbsent(currentServerAddress, k -> {
            loadStats();
            return serverStatsMap.getOrDefault(currentServerAddress, new PlayerStats());
        });
    }

    public static void saveStats() {
        initialize();
        if (saveDir == null) return;
        
        try {
            File saveFile = new File(saveDir, currentServerAddress + ".dat");
            PlayerStats stats = serverStatsMap.get(currentServerAddress);
            if (stats != null) {
                NbtCompound root = new NbtCompound();
                stats.writeNbt(root);
                NbtIo.writeCompressed(root, saveFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadStats() {
        initialize();
        if (saveDir == null) return;
        
        File saveFile = new File(saveDir, currentServerAddress + ".dat");
        if (!saveFile.exists()) {
            serverStatsMap.put(currentServerAddress, new PlayerStats());
            return;
        }
        
        try {
            NbtCompound root = NbtIo.readCompressed(saveFile.toPath(), net.minecraft.nbt.NbtSizeTracker.ofUnlimitedBytes());
            PlayerStats stats = new PlayerStats();
            stats.readNbt(root);
            serverStatsMap.put(currentServerAddress, stats);
        } catch (IOException e) {
            e.printStackTrace();
            serverStatsMap.put(currentServerAddress, new PlayerStats());
        }
    }
}

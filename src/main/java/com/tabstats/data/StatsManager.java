package com.tabstats.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsManager {
    private static final Map<UUID, PlayerStats> playerStatsMap = new HashMap<>();
    private static File saveFile;

    public static void initialize(MinecraftServer server) {
        if (server != null) {
            File worldDir = server.getSavePath(net.minecraft.world.SaveProperties.LEVEL_DAT).getParent().toFile();
            File tabStatsDir = new File(worldDir, "tabstats");
            if (!tabStatsDir.exists()) {
                tabStatsDir.mkdirs();
            }
            saveFile = new File(tabStatsDir, "player_stats.dat");
            loadStats();
        }
    }

    public static PlayerStats getPlayerStats(UUID playerUuid) {
        return playerStatsMap.computeIfAbsent(playerUuid, k -> new PlayerStats());
    }

    public static void saveStats() {
        if (saveFile == null) return;
        
        try {
            NbtCompound root = new NbtCompound();
            NbtCompound playersNbt = new NbtCompound();
            
            for (Map.Entry<UUID, PlayerStats> entry : playerStatsMap.entrySet()) {
                NbtCompound playerNbt = new NbtCompound();
                entry.getValue().writeNbt(playerNbt);
                playersNbt.put(entry.getKey().toString(), playerNbt);
            }
            
            root.put("Players", playersNbt);
            NbtIo.writeCompressed(root, saveFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadStats() {
        if (saveFile == null || !saveFile.exists()) return;
        
        try {
            NbtCompound root = NbtIo.readCompressed(saveFile.toPath(), net.minecraft.nbt.NbtSizeTracker.ofUnlimitedBytes());
            NbtCompound playersNbt = root.getCompound("Players");
            
            for (String key : playersNbt.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(key);
                    PlayerStats stats = new PlayerStats();
                    stats.readNbt(playersNbt.getCompound(key));
                    playerStatsMap.put(uuid, stats);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

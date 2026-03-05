package com.tabstats.event;

import com.tabstats.achievement.AchievementChecker;
import com.tabstats.data.PlayerStats;
import com.tabstats.data.StatsManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static boolean wasTabPressed = false;
    private static long lastSaveTime = System.currentTimeMillis();
    private static final long SAVE_INTERVAL = 30000;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean isTabPressed = GLFW.glfwGetKey(
                MinecraftClient.getInstance().getWindow().getHandle(),
                GLFW.GLFW_KEY_TAB
            ) == GLFW.GLFW_PRESS;

            if (isTabPressed && !wasTabPressed) {
                sendTabPressPacket();
            }

            if (isTabPressed) {
                sendTabOpenPacket();
            } else if (wasTabPressed) {
                sendTabClosePacket();
            }

            wasTabPressed = isTabPressed;

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSaveTime >= SAVE_INTERVAL) {
                StatsManager.saveStats();
                lastSaveTime = currentTime;
            }
        });
    }

    private static void sendTabPressPacket() {
        ClientPlayNetworking.send(new Identifier("tabstats", "tab_press"), PacketByteBufs.create());
    }

    private static void sendTabOpenPacket() {
        ClientPlayNetworking.send(new Identifier("tabstats", "tab_open"), PacketByteBufs.create());
    }

    private static void sendTabClosePacket() {
        ClientPlayNetworking.send(new Identifier("tabstats", "tab_close"), PacketByteBufs.create());
    }
}

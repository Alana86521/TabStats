package com.tabstats;

import com.tabstats.command.TabStatsCommand;
import com.tabstats.data.StatsManager;
import com.tabstats.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class TabStatsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StatsManager.initialize();
        
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TabStatsCommand.register(dispatcher);
        });
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String serverAddress = "singleplayer";
            if (client.getCurrentServerEntry() != null) {
                serverAddress = client.getCurrentServerEntry().address;
            } else if (handler.getConnection() != null && handler.getConnection().getAddress() != null) {
                serverAddress = handler.getConnection().getAddress().toString();
            }
            StatsManager.setCurrentServer(serverAddress);
            TabStats.LOGGER.info("TabStats connected to server: " + serverAddress);
        });
        
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            StatsManager.saveStats();
        });
        
        KeyInputHandler.register();
        TabStats.LOGGER.info("TabStats client initialized!");
    }
}

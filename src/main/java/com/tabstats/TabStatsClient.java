package com.tabstats;

import com.tabstats.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class TabStatsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        TabStats.LOGGER.info("TabStats client initialized!");
    }
}

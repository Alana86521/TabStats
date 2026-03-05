package com.tabstats;

import com.tabstats.command.TabStatsCommand;
import com.tabstats.data.StatsManager;
import com.tabstats.event.ServerNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabStats implements ModInitializer {
	public static final String MOD_ID = "tabstats";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing TabStats...");
		
		ServerNetworkHandler.register();
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TabStatsCommand.register(dispatcher);
		});
		
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			StatsManager.initialize(server);
			LOGGER.info("TabStats data manager initialized!");
		});
		
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			StatsManager.saveStats();
			LOGGER.info("TabStats data saved!");
		});
		
		LOGGER.info("TabStats initialized successfully!");
	}
}
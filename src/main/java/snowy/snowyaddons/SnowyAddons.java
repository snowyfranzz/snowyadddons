package snowy.snowyaddons;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snowy.snowyaddons.config.ModConfig;


public class SnowyAddons implements ModInitializer {
	public static final String MOD_ID = "snowyaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Config instance
	public static final ModConfig CONFIG = new ModConfig();

	@Override
	public void onInitialize() {
		ModConfig.HANDLER.load(); // load config

		LOGGER.info("Initialized successfully!");
	}
}
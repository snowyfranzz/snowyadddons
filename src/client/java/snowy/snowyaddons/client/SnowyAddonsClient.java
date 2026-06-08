package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.modules.dungeons.M2FireFreezeTimer;
import snowy.snowyaddons.client.modules.dungeons.M2Phase2Timer;
import snowy.snowyaddons.client.modules.dungeons.M2SenTech;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);
		ChatListener.register();

		M2FireFreezeTimer m2FfTimer = new M2FireFreezeTimer();
		m2FfTimer.registerEvents();

		M2Phase2Timer m2P2Timer = new M2Phase2Timer();
		m2P2Timer.registerEvents();

		M2SenTech m2SenTech = new M2SenTech();
		m2SenTech.registerEvents();


		// every end of tick
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.level != null) {
				if (GetServerInfo.isInSkyBlock()) {
					DailiesManager.sendDailiesNotification();

					if (GetServerInfo.isInDungeon())
					{
						if (ModConfig.HANDLER.instance().m2FireFreeze){
							m2FfTimer.onTick();
						}

						if (ModConfig.HANDLER.instance().m2Phase2){
							m2P2Timer.onTick();
						}

						if (ModConfig.HANDLER.instance().m2SenTech){
							m2SenTech.onTick();
						}


					}
				}
			}
		});
	}
}
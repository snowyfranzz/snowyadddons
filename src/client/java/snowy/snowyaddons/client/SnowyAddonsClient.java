package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.modules.dungeons.M2FireFreezeTimer;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;
import snowy.snowyaddons.client.utils.listeners.ChatListener;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);
		ChatListener.register();

		M2FireFreezeTimer m2Timer = new M2FireFreezeTimer();
		m2Timer.registerHudRenderer();


		// every end of tick
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.level != null) {
				if (GetServerInfo.isInSkyBlock()) {
					DailiesManager.sendDailiesNotification();

					if (GetServerInfo.isInDungeon())
					{
						m2Timer.onTick();
					}
				}
			}
		});
	}
}
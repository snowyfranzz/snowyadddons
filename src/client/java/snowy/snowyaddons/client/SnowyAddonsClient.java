package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if (client.player != null && client.level != null) {
				DailiesManager.sendDailiesNotification();
			}
		});
	}
}
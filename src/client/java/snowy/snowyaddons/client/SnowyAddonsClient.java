package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);

	}
}
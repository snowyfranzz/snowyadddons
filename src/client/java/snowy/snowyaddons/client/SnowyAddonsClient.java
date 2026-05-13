package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import snowy.snowyaddons.client.modules.render.BatESP;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.END_MAIN.register(new BatESP()::onRender);
	}
}
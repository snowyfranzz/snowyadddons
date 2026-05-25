package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.modules.dungeons.m2.FireFreezeTimer;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class SnowyAddonsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);
		ChatListener.register();

		// every end of tick
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.level != null) {
				if(GetServerInfo.isInSkyBlock()){
					DailiesManager.sendDailiesNotification();

					if(GetServerInfo.isInDungeon()){
						// m2 fire freeze timer
						if(ModConfig.HANDLER.instance().m2FireFreeze){
=
						}

					}
				}
			}
		});
	}
}
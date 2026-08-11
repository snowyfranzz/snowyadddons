package snowy.snowyaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.modules.dungeons.*;
import snowy.snowyaddons.client.modules.fun.AutoQuakecraftOnDt;
import snowy.snowyaddons.client.modules.jukebox.JukeboxManager;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.command.ModCommandRegister;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class SnowyAddonsClient implements ClientModInitializer {

	// FIX: Define these as class fields so the entire mod reads the exact same instances
	public static M2FireFreezeTimer m2FfTimer;
	public static M2Phase2Timer m2P2Timer;
	public static M2SenTech m2SenTech;
	public static BloodCampHelper bcHelper;
	public static AutoQuakecraftOnDt autoQuake;
	public static M2Splits m2Splits;
	public static JukeboxManager jukebox;

	private int dungeonLeaveGraceTicks = 0;

	@Override
	public void onInitializeClient() {

		ClientCommandRegistrationCallback.EVENT.register(ModCommandRegister::commandRegister);
		ChatListener.register();

		// Initialize the class fields
		m2FfTimer = new M2FireFreezeTimer();
		m2FfTimer.registerEvents();

		m2P2Timer = new M2Phase2Timer();
		m2P2Timer.registerEvents();

		m2SenTech = new M2SenTech();
		m2SenTech.registerEvents();

		bcHelper = new BloodCampHelper();
		bcHelper.registerEvents();

		autoQuake = new AutoQuakecraftOnDt();
		autoQuake.registerEvents();

		m2Splits = new M2Splits();
		m2Splits.registerEvents();

		jukebox = new JukeboxManager();
		jukebox.registerEvents();

		// every end of tick
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && client.level != null) {
				jukebox.onTick();

				if (GetServerInfo.isInSkyBlock()) {

					DailiesManager.sendDailiesNotification();

					if (GetServerInfo.isInDungeon()) {
						this.dungeonLeaveGraceTicks = 0;

						if (ModConfig.HANDLER.instance().m2FireFreeze){
							m2FfTimer.onTick();
						}

						if (ModConfig.HANDLER.instance().m2Phase2){
							m2P2Timer.onTick();
						}

						if (ModConfig.HANDLER.instance().m2SenTech){
							m2SenTech.onTick();
						}

						if (ModConfig.HANDLER.instance().bcHelper){
							bcHelper.onTick();
						}

						if (ModConfig.HANDLER.instance().autoQuake){
							autoQuake.onTick();
						}

						if (ModConfig.HANDLER.instance().m2Splits){
							m2Splits.onTick();
						}
					} else {
						if (!m2Splits.isReset){
							this.dungeonLeaveGraceTicks++;

							if (this.dungeonLeaveGraceTicks >= 60) {
								m2Splits.resetValues();
								this.dungeonLeaveGraceTicks = 0;
							}
						}
					}
				} else {
					if (!m2Splits.isReset){
						this.dungeonLeaveGraceTicks++;

						if (this.dungeonLeaveGraceTicks >= 60) {
							m2Splits.resetValues();
							this.dungeonLeaveGraceTicks = 0;
						}
					}
				}
			}
		});
	}
}
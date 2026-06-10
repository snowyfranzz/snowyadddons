package snowy.snowyaddons.client.modules.fun;

import snowy.snowyaddons.client.utils.CommandUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;
import java.util.concurrent.ThreadLocalRandom;

public class AutoQuakecraftOnDt {

    // QUAKECRAFT DUELS >>>>>>>> ANYTHING

    private boolean dtCalled = false;
    private long executeAtMillis = 0;

    private static final long MIN_DELAY_MS = 1400;
    private static final long MAX_DELAY_MS = 1800;

    public void registerEvents(){
        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().autoQuake) return;

            if (cleanMessage.contains("PARTY >") && cleanMessage.contains(": !DT")) {
                this.dtCalled = true;
            }

            if (cleanMessage.contains("[BOSS] SCARF: HIS TECHNIQUE.. IS TOO ADVANCED..") && this.dtCalled){
                if (this.executeAtMillis == 0) {
                    long randomDelay = ThreadLocalRandom.current().nextLong(MIN_DELAY_MS, MAX_DELAY_MS + 1);
                    this.executeAtMillis = System.currentTimeMillis() + randomDelay;
                }
            }
        });
    }

    public void onTick(){
        if (this.executeAtMillis == 0) return;

        if (System.currentTimeMillis() >= this.executeAtMillis) {
            String playerName = ModConfig.HANDLER.instance().quakePlayerName;
            if (playerName != null) {
                CommandUtil.runCommand("duel " + playerName + " quakecraft");
            }

            this.dtCalled = false;
            this.executeAtMillis = 0;
        }
    }
}
package snowy.snowyaddons.client.modules.fun;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.commons.lang3.ObjectUtils;
import snowy.snowyaddons.client.utils.CommandUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class AutoQuakecraftOnDt {

    // QUAKECRAFT DUELS >>>>>>>> ANYTHING

    private boolean dtCalled = false;
    private boolean dungeonOver = false;

    public void registerEvents(){

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().autoQuake) return;
            if (cleanMessage.contains("PARTY >") && cleanMessage.contains(": !DT")) {
                this.dtCalled = true;
            }
            if (cleanMessage.contains("CLICK HERE TO RE-QUEUE") && this.dtCalled){
                this.dungeonOver = true;
            }
        });
    }

    public void onTick(){
        if(this.dtCalled){
            if(this.dungeonOver){
                String playerName = ModConfig.HANDLER.instance().quakePlayerName;
                if(playerName != null){
                    CommandUtil.runCommand("duel " + playerName + " quakecraft");
                }
                dtCalled = false;
                dungeonOver = false;
            }
        }
    }

}

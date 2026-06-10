package snowy.snowyaddons.client.modules.dungeons;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.utils.HudRendererUtil;
import snowy.snowyaddons.client.utils.SoundUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class BloodCampHelper {

    // bc420 is real

    private int remainingTicks = 0;
    private boolean timerActivated = false;

    public void registerEvents() {
        HudRenderCallback.EVENT.register(this::onHudRender);

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().bcHelper) return;
            if (cleanMessage.contains("[BOSS] THE WATCHER: LET'S SEE HOW YOU CAN HANDLE THIS.")) {
                this.remainingTicks = 45;
                this.timerActivated = true;
            }
        });
    }

    public void onTick() {
        if (this.remainingTicks > 0) {
            this.remainingTicks--;
        }
    }

    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer localPlayer = mc.player;

        if (this.remainingTicks > 0) {

            int centerHeight = guiGraphics.guiHeight() - (guiGraphics.guiHeight() / 2);
            int centerWidth = guiGraphics.guiWidth() - (guiGraphics.guiWidth() / 2);

            double secondsRemaining = this.remainingTicks / 20.0;

            // format to 1 decimal place
            String formattedTime = String.format("%.1fs", secondsRemaining);
            Component textToDraw = Component.literal("Blood Camp Timer: " + formattedTime);
            HudRendererUtil.renderText(guiGraphics, textToDraw, centerWidth + 20, centerHeight - 20, ModConfig.HANDLER.instance().bcHelperColor, true);

            // DEBUG -> localPlayer.displayClientMessage(Component.literal("§d[Debug]§r tried drawing to hud"), false);
        } else {
            if(this.timerActivated){
                HudRendererUtil.displayTitle("CLEAR BLOOD", "", 10, 40, 10, ModConfig.HANDLER.instance().bcHelperColor);
                SoundUtil.playSoundCatMeow();
                timerActivated = false;
            }
        }
    }

}

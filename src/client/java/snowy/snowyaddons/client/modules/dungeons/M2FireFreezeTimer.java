package snowy.snowyaddons.client.modules.dungeons;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.utils.HudRendererUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import net.minecraft.client.gui.GuiGraphics;
import snowy.snowyaddons.config.ModConfig;

public class M2FireFreezeTimer {

    // DEDICTED TO CHUNGES <3
    // M2 RATS ON TOP
    // does he know

    private int remainingTicks = 0;

    public void registerEvents() {

        HudRenderCallback.EVENT.register(this::onHudRender);

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().m2FireFreeze) return;
            if (cleanMessage.contains("[BOSS] SCARF: THOSE TOYS ARE NOT STRONG ENOUGH I SEE.")) {
                this.remainingTicks = 110;
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

            Component textToDraw = Component.literal("Fire Freeze: " + formattedTime);

            HudRendererUtil.renderText(guiGraphics, textToDraw, centerWidth + 20, centerHeight - 20, ModConfig.HANDLER.instance().m2FfColor, true);

            // DEBUG -> localPlayer.displayClientMessage(Component.literal("§d[Debug]§r tried drawing to hud"), false);
        }
    }
}
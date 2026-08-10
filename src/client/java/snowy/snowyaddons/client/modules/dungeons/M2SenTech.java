package snowy.snowyaddons.client.modules.dungeons;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snowy.snowyaddons.client.utils.HudRendererUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class M2SenTech {

    // Copyright* © 2026–2026 SenTashi SenTech. All Rights Reserved.
    // *Patent lowkey pending

    private int remainingTicks = 0;

    public void registerEvents() {
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("snowyaddons", "m2_sentech"),
                this::onHudRender
        );

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().m2SenTech) return;
                if (cleanMessage.contains("[BOSS] SCARF: THIS IS WHERE THE JOURNEY ENDS FOR YOU, ADVENTURERS.")) {
                this.remainingTicks = 370;
            }
        });
    }

    public void onTick() {
        if (this.remainingTicks > 0) {
            float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
            this.remainingTicks -= Math.max(1, (int) delta);
        }
    }

    public void onHudRender(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer localPlayer = mc.player;

        if (this.remainingTicks > 0) {

            int centerHeight = guiGraphics.guiHeight() - (guiGraphics.guiHeight() / 2);
            int centerWidth = guiGraphics.guiWidth() - (guiGraphics.guiWidth() / 2);

            double secondsRemaining = this.remainingTicks / 20.0;

            // format to 1 decimal place
            String formattedTime = String.format("%.1fs", secondsRemaining);

            Component textToDraw = Component.literal("SenTech©: " + formattedTime);

            HudRendererUtil.renderText(guiGraphics, textToDraw, centerWidth + 20, centerHeight - 20, ModConfig.HANDLER.instance().m2SenTechColor, true);

            // DEBUG -> localPlayer.displayClientMessage(Component.literal("§d[Debug]§r tried drawing to hud"), false);
        }
    }
}

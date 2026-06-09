package snowy.snowyaddons.client.modules.dungeons;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.utils.HudRendererUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

public class M2Splits {
    /*
    splits:

    blood open (start - open)
    blood clear (open - blood clear)
    entry (blood clear - entry)
    cutscene 1 (entry - p1 start)
    p1 (p1 start - p1 end)
    cutscene 2 (p1 end - p2 start)
    p2 (p2 start - p2 end)
    end cutscene (p2 end - dungeon end)
     */

    private int brTicks = 0;
    private int bcTicks = 0;
    private int entryTicks = 0;
    private int cutscene1Ticks = 0;
    private int phase1Ticks = 0;
    private int cutscene2Ticks = 0;
    private int phase2Ticks = 0;
    private int cutsceneEndTicks = 0;

    private boolean flagStart = false;
    private boolean flagOpenBlood = false;
    private boolean flagClearBlood = false;
    private boolean flagEntry = false;
    private boolean flagP1Start = false;
    private boolean flagP1End = false;
    private boolean flagP2Start = false;
    private boolean flagP2End = false;
    private boolean flagDungeonEnd = false;


    public void registerEvents() {
        HudRenderCallback.EVENT.register(this::onHudRender);

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().m2Splits) return;

            // dungeon start
            if (cleanMessage.contains("[NPC] MORT: HERE, I FOUND THIS MAP WHEN I FIRST ENTERED THE DUNGEON.")) {
                this.flagStart = true;
            }

            // blood open
            if (cleanMessage.contains("blood open message")) { // TODO
                this.flagOpenBlood = true;
            }

            // blood clear
            if (cleanMessage.contains("you have proven yourselves you may pass or some shit")) { // TODO
                this.flagClearBlood = true;
            }

            // entry
            if (cleanMessage.contains("[BOSS] SCARF: THIS IS WHERE THE JOURNEY ENDS FOR YOU, ADVENTURERS.")) {
                this.flagEntry = true;
            }

            // phase 1 start
            if (cleanMessage.contains("[BOSS] ARISE, MY CREATIONS!")) {
                this.flagP1Start = true;
            }

            // phase 1 end
            if (cleanMessage.contains("[BOSS] SCARF: THOSE TOYS ARE NOT STRONG ENOUGH I SEE.")) {
                this.flagP1End = true;
            }

            // phase 2 start
            if (cleanMessage.contains("[BOSS] SCARF: DID YOU FORGET? I WAS TAUGHT BY THE BEST! LET'S DANCE.")) {
                this.flagP2Start = true;
            }

            // phase 2 end
            if (cleanMessage.contains("[BOSS] SCARF: WHATEVER...")) {
                this.flagP2End = true;
            }

            // dungeon end
            if (cleanMessage.contains("MASTER MODE THE CATACOMBS - FLOOR II")) {
                this.flagDungeonEnd = true;
            }
        });
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

            Component textToDraw = Component.literal("SenTech©: " + formattedTime);

            HudRendererUtil.renderText(guiGraphics, textToDraw, centerWidth + 20, centerHeight - 20, ModConfig.HANDLER.instance().m2SenTechColor, true);

            // DEBUG -> localPlayer.displayClientMessage(Component.literal("§d[Debug]§r tried drawing to hud"), false);
        }
    }
}
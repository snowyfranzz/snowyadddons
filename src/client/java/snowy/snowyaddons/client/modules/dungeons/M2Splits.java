package snowy.snowyaddons.client.modules.dungeons;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.utils.HudRendererUtil;
import snowy.snowyaddons.client.utils.listeners.ChatListener;
import snowy.snowyaddons.config.ModConfig;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class M2Splits {

    private int totalTicks = 0;
    private int bloodOpenTicks = 0;
    private int bloodClearTicks = 0;
    private int entryTicks = 0;
    private int cutscene1Ticks = 0;
    private int phase1Ticks = 0;
    private int cutscene2Ticks = 0;
    private int phase2Ticks = 0;
    private int dungeonEndTicks = 0;

    private boolean flagStart = false;
    private boolean flagOpenBlood = false;
    private boolean flagClearBlood = false;
    private boolean flagEntry = false;
    private boolean flagP1Start = false;
    private boolean flagP1End = false;
    private boolean flagP2Start = false;
    private boolean flagP2End = false;
    private boolean flagDungeonEnd = false;

    public boolean isReset = true;

    private int endTargetTick = 0;
    private boolean flagEndTriggered = false;

    public void registerEvents() {
        HudRenderCallback.EVENT.register(this::onHudRender);

        ChatListener.subscribe(cleanMessage -> {
            if (!ModConfig.HANDLER.instance().m2Splits) return;

            // run start
            if (cleanMessage.contains("[NPC] MORT: HERE, I FOUND THIS MAP WHEN I FIRST ENTERED THE DUNGEON.")) {
                resetValues();
                this.flagStart = true;
                this.isReset = false;
                return;
            }

            // blood open
            if (cleanMessage.contains("THE BLOOD DOOR HAS BEEN OPENED!") && !this.flagOpenBlood) {
                this.bloodOpenTicks = this.totalTicks;
                this.flagOpenBlood = true;
            }

            // blood clear
            if (cleanMessage.contains("[BOSS] THE WATCHER: YOU HAVE PROVEN YOURSELF. YOU MAY PASS.") && !this.flagClearBlood) {
                this.bloodClearTicks = this.totalTicks;
                this.flagClearBlood = true;
            }

            // entry
            if (cleanMessage.contains("[BOSS] SCARF: THIS IS WHERE THE JOURNEY ENDS FOR YOU, ADVENTURERS.") && !this.flagEntry) {
                this.entryTicks = this.totalTicks;
                this.flagEntry = true;
            }

            // phase 1 start
            if (cleanMessage.contains("[BOSS] SCARF: ARISE, MY CREATIONS!") && !this.flagP1Start) {
                this.cutscene1Ticks = this.totalTicks;
                this.flagP1Start = true;
            }

            // phase 1 end
            if (cleanMessage.contains("[BOSS] SCARF: THOSE TOYS ARE NOT STRONG ENOUGH I SEE.") && !this.flagP1End) {
                this.phase1Ticks = this.totalTicks;
                this.flagP1End = true;
            }

            // phase 2 start
            if (cleanMessage.contains("[BOSS] SCARF: DID YOU FORGET? I WAS TAUGHT BY THE BEST! LET'S DANCE.") && !this.flagP2Start) {
                this.cutscene2Ticks = this.totalTicks;
                this.flagP2Start = true;
            }

            // phase 2 end
            if (cleanMessage.contains("[BOSS] SCARF: WHATEVER...") && !this.flagP2End) {
                this.phase2Ticks = this.totalTicks;
                this.flagP2End = true;
            }

            // dungeon end
            if (cleanMessage.contains("[BOSS] SCARF: HIS TECHNIQUE.. IS TOO ADVANCED..") && !this.flagDungeonEnd) {
                this.endTargetTick = this.totalTicks + 20;
                this.flagEndTriggered = true;
            }
        });
    }

    public void resetValues() {
        this.totalTicks = 0;
        this.bloodOpenTicks = 0;
        this.bloodClearTicks = 0;
        this.entryTicks = 0;
        this.cutscene1Ticks = 0;
        this.phase1Ticks = 0;
        this.cutscene2Ticks = 0;
        this.phase2Ticks = 0;
        this.dungeonEndTicks = 0;

        this.flagStart = false;
        this.flagOpenBlood = false;
        this.flagClearBlood = false;
        this.flagEntry = false;
        this.flagP1Start = false;
        this.flagP1End = false;
        this.flagP2Start = false;
        this.flagP2End = false;
        this.flagDungeonEnd = false;

        isReset = true;
    }

    public void onTick() {
        if (!this.flagStart) return;

        if (this.flagEndTriggered && !this.flagDungeonEnd && this.totalTicks >= this.endTargetTick) {
            this.dungeonEndTicks = this.totalTicks;
            this.flagDungeonEnd = true;
        }

        if (!this.flagDungeonEnd) {
            this.totalTicks++;
        }
    }

    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!ModConfig.HANDLER.instance().m2Splits || !this.flagStart) return;

        int startX = ModConfig.HANDLER.instance().m2SplitsX;
        int startY = ModConfig.HANDLER.instance().m2SplitsY;

        if (startX == -1 || startY == -1) {
            int[] defaults = getDefaultPositions(guiGraphics);
            if (startX == -1) startX = defaults[0];
            if (startY == -1) startY = defaults[1];
        }

        int yOffset = 0;
        List<String[]> splitsToDraw = new ArrayList<>();
        splitsToDraw.add(getSplitData("Blood Open: ", 0, bloodOpenTicks, totalTicks, this.flagStart && !this.flagOpenBlood));
        splitsToDraw.add(getSplitData("Blood Clear: ", bloodOpenTicks, bloodClearTicks, totalTicks, this.flagOpenBlood && !this.flagClearBlood));
        splitsToDraw.add(getSplitData("Boss Entry: ", bloodClearTicks, entryTicks, totalTicks, this.flagClearBlood && !this.flagEntry));
        splitsToDraw.add(getSplitData("Cutscene 1: ", entryTicks, cutscene1Ticks, totalTicks, this.flagEntry && !this.flagP1Start));
        splitsToDraw.add(getSplitData("Phase 1: ", cutscene1Ticks, phase1Ticks, totalTicks, this.flagP1Start && !this.flagP1End));
        splitsToDraw.add(getSplitData("Cutscene 2: ", phase1Ticks, cutscene2Ticks, totalTicks, this.flagP1End && !this.flagP2Start));
        splitsToDraw.add(getSplitData("Phase 2: ", cutscene2Ticks, phase2Ticks, totalTicks, this.flagP2Start && !this.flagP2End));
        splitsToDraw.add(getSplitData("End Cutscene: ", phase2Ticks, dungeonEndTicks, totalTicks, this.flagP2End && !this.flagDungeonEnd));

        HudRendererUtil.renderText(guiGraphics, Component.literal("[SnowyAddons] M2 Splits").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA), startX, startY - 14, Color.WHITE, true);

        for (String[] split : splitsToDraw) {
            String label = split[0];
            String timeStr = split[1];
            String status = split[2];

            String formattedLine;
            if (status.equals("idle")) {
                formattedLine = "§c" + label + "0.0s §7(0.0s)";
            } else if (status.equals("active")) {
                formattedLine = "§e" + label + timeStr;
            } else {
                formattedLine = "§a" + label + timeStr;
            }

            Component textComponent = Component.literal(formattedLine);
            HudRendererUtil.renderText(guiGraphics, textComponent, startX, startY + yOffset, Color.WHITE, true);

            yOffset += 12;
        }
    }

    private String[] getSplitData(String label, int lastMilestoneTicks, int targetMilestoneTicks, int currentTicks, boolean isActive) {
        if (!this.flagStart) return new String[]{label, "0.0s §7(0.0s)", "idle"};

        if (targetMilestoneTicks > 0) {
            double segmentTime = (targetMilestoneTicks - lastMilestoneTicks) / 20.0;
            double cumulativeTime = targetMilestoneTicks / 20.0;
            String formatted = String.format("%.1fs §7(%.1fs)", segmentTime, cumulativeTime);
            return new String[]{label, formatted, "done"};
        } else if (isActive) {
            double segmentTime = (currentTicks - lastMilestoneTicks) / 20.0;
            double cumulativeTime = currentTicks / 20.0;
            String formatted = String.format("%.1fs §7(%.1fs)", segmentTime, cumulativeTime);
            return new String[]{label, formatted, "active"};
        } else {
            return new String[]{label, "0.0s §7(0.0s)", "idle"};
        }
    }

    public int[] getDefaultPositions(GuiGraphics guiGraphics) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int defaultX = (screenWidth / 2) - 360;

        int defaultY = (screenHeight / 2) - 120;

        return new int[]{defaultX, defaultY};
    }
}
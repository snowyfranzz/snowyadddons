package snowy.snowyaddons.client.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;

public class HudRendererUtil {

    public static void renderText(GuiGraphics guiGraphics, Component text, int x, int y, int color, boolean shadow) {
        Font font = Minecraft.getInstance().font;

        guiGraphics.drawString(font, text, x, y, color, shadow);
    }
}
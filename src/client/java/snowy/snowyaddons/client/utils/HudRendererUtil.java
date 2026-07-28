package snowy.snowyaddons.client.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import snowy.snowyaddons.config.ModConfig;

import java.awt.*;

public class HudRendererUtil {

    public static void renderText(GuiGraphicsExtractor guiGraphics, Component text, int x, int y, Color color, boolean shadow) {
        Font font = Minecraft.getInstance().font;

        int renderColor = ColorUtil.getDecimalFromColor(color);

        guiGraphics.text(font, text, x, y, renderColor, shadow);
    }

    public static void displayTitle(String mainTitle, String subTitle, int fadeIn, int stay, int fadeOut, Color color) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui == null) return;

        int renderColor = ColorUtil.getDecimalFromColor(color);
        TextColor titleColor = TextColor.fromRgb(renderColor & 0x00FFFFFF);

        Component finalTitle = Component.literal(mainTitle)
                .withStyle(Style.EMPTY.withColor(titleColor).withBold(true));

        Component finalSub = subTitle != null ? Component.literal(subTitle) : null;

        // animation ticks
        mc.gui.hud.setTimes(fadeIn, stay, fadeOut);

        // subtitle
        if (subTitle != null && !subTitle.isEmpty()) {
            mc.gui.hud.setSubtitle(finalSub);
        }

        // title
        mc.gui.hud.setTitle(finalTitle);
    }
}

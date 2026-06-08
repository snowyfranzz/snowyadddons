package snowy.snowyaddons.client.utils;

import java.awt.*;

public class ColorUtil {
    // transforms java.awt.Color objects into hex -
    // TODO: Implement rainbow cycling colors / gradients like youd see in moduleList text (idk if ill do this, maybe in the distant future ig?)

    public static int getDecimalFromColor(Color color) {
        if (color == null) {
            return 0xFFA47DC9; // W PURPLE <3 <3 <3 <3
        }

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}

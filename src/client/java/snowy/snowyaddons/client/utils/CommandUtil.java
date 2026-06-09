package snowy.snowyaddons.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class CommandUtil {
    public static void runCommand(String command) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null && player.connection != null) {
            player.connection.sendCommand(command);
        }
    }
}
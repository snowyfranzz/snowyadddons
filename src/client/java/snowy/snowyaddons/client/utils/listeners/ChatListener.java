package snowy.snowyaddons.client.utils.listeners;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.chat.Component;

public class ChatListener {

    private static String latestChatMessage = "";

    public static void register() {
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTime) -> {
            storeMessage(message);
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            storeMessage(message);
        });
    }

    private static void storeMessage(Component message) {
        String rawText = message.getString();
        latestChatMessage = rawText.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();
    }

    public static boolean isChatMessage(String messageTarget) {
        String cleanTarget = messageTarget.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();

        if (latestChatMessage.contains(cleanTarget)) {
            latestChatMessage = "";
            return true;
        }
        return false;
    }
}

package snowy.snowyaddons.client.utils.listeners;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChatListener {

    private static final List<Consumer<String>> subscribers = new ArrayList<>();

    public static void register() {

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTime) -> {
            handleIncoming(message);
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            handleIncoming(message);
        });
    }

    public static void subscribe(Consumer<String> callback) {
        subscribers.add(callback);
    }

    private static void handleIncoming(Component message) {
        String rawText = message.getString();

        String cleanText = rawText.replaceAll("(?i)§[0-9a-fk-or]", "").toUpperCase();

        for (Consumer<String> subscriber : subscribers) {
            subscriber.accept(cleanText);
        }
    }
}

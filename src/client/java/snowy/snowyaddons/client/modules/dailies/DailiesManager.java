package snowy.snowyaddons.client.modules.dailies;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.config.dailies.DailiesNotificationSendMethod;
import snowy.snowyaddons.data.DataManager;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

public class DailiesManager {
    /*
    List of dailies

    Farming:
        - Pests
        - Greenhouse
        - Visitors

    Crimson isles:
        - Matriarch
        - Reputation
        - XP Bottles Flip

    Galatea:
        - Agatha's contests

    Other:
        - Rabbit hitman
        - Chocolate factory
        - Huntraps
        - Dungeons
        - Minions
        - Experimentation Table
     */

    private static long lastNotificationTime = 0L; // for WHILE_UNCOMPLETED

    static ModConfig config = ModConfig.HANDLER.instance();

    public static List<Supplier<Boolean>> dailiesToggled = List.of(
            () -> config.dailiesShowPests,
            () -> config.dailiesShowGreenhouse,
            () -> config.dailiesShowVisitors,
            () -> config.dailiesShowMatriarch,
            () -> config.dailiesShowReputation,
            () -> config.dailiesShowBottlesFlip,
            () -> config.dailiesShowAgatha,
            () -> config.dailiesShowHitman,
            () -> config.dailiesShowChocoFactory,
            () -> config.dailiesShowHuntraps,
            () -> config.dailiesShowDungeons,
            () -> config.dailiesShowMinions,
            () -> config.dailiesShowExpTable
    );

    public static List<Supplier<Boolean>> dailiesCompletionState = List.of(
            () -> DataManager.INSTANCE.dailiesPestsState,
            () -> DataManager.INSTANCE.dailiesGreenhouseState,
            () -> DataManager.INSTANCE.dailiesVisitorState,
            () -> DataManager.INSTANCE.dailiesMatriarchState,
            () -> DataManager.INSTANCE.dailiesReputationState,
            () -> DataManager.INSTANCE.dailiesBottleFlipState,
            () -> DataManager.INSTANCE.dailiesAgathaState,
            () -> DataManager.INSTANCE.dailiesHitmanState,
            () -> DataManager.INSTANCE.dailiesChocoFactoryState,
            () -> DataManager.INSTANCE.dailiesHuntrapsState,
            () -> DataManager.INSTANCE.dailiesDungeonState,
            () -> DataManager.INSTANCE.dailiesMinionsState,
            () -> DataManager.INSTANCE.dailiesExpTableState
    );

    // no supplier updates cause this shouldn't change
    public static List<String> dailiesNames = List.of(
            "Pests",
            "Greenhouse",
            "Visitors",
            "Matriarch (Heavy Pearls)",
            "Faction Reputation Quests",
            "Grand EXP Bottles Flip (Mage Faction)",
            "Agatha's Contest",
            "Claim Rabbit Hitman",
            "Manage Chocolate Factory",
            "Claim and Replace Huntraps",
            "Daily Dungeons XP Bonus",
            "Claim Minions",
            "Experimentation Table"
    );

    public static Component dailiesListBuilder(){
        MutableComponent dailiesComponent = Component.literal("");

        List<Component> lines = new ArrayList<>();

        for(int i = 0; i < dailiesToggled.size(); i++){
            if(dailiesToggled.get(i).get() && !dailiesCompletionState.get(i).get()){
                lines.add(Component.literal("✗ " + dailiesNames.get(i)).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            } else {
                if(dailiesToggled.get(i).get() && config.dailiesShowCompleted && dailiesCompletionState.get(i).get()){
                    lines.add(Component.literal("✓ " + dailiesNames.get(i)).withStyle(ChatFormatting.GREEN));
                }
            }
        }

        for(int i = 0; i < lines.size(); i++){
            dailiesComponent.append(lines.get(i));
            if(i != lines.size() - 1){
                dailiesComponent.append("\n");
            }
        }

        return dailiesComponent;
    }
    public static boolean isSentToday(){
        DataManager.JsonDate saved = DataManager.INSTANCE.lastDailyDate;

        if (saved.year == 0 || saved.month == 0 || saved.day == 0) {
            return false;
        }

        LocalDate savedDate = LocalDate.of(saved.year, saved.month, saved.day);
        return !savedDate.isBefore(LocalDate.now());
    }

    public static void sendDailiesNotification() {

        if (!config.dailiesNotification || !DataManager.notificationState()) {
            return;
        }

        boolean shouldSend = false;
        long currentTime = System.currentTimeMillis();

        // --- PER_SESSION ---
        if (config.dailiesSendMethod == DailiesNotificationSendMethod.PER_SESSION) {
            if (lastNotificationTime == 0L) {
                shouldSend = true;
            }
        }
        // --- ONCE_PER_DAY ---
        else if (config.dailiesSendMethod == DailiesNotificationSendMethod.ONCE_PER_DAY) {
            if (!isSentToday()) {
                shouldSend = true;
            }
        }
        // --- WHILE_UNCOMPLETED ---
        else if (config.dailiesSendMethod == DailiesNotificationSendMethod.WHILE_UNCOMPLETED) {
            long cooldownMs = (long) config.whileUncompletedSleep * 60 * 1000;

            if (lastNotificationTime == 0L || (currentTime - lastNotificationTime >= cooldownMs)) {
                shouldSend = true;
            }
        }

        if (shouldSend) {
            lastNotificationTime = currentTime;

            Component notificationMessage = Component.literal("[SnowyAddons] You have pending daily tasks:\n").withStyle(ChatFormatting.AQUA)
                    .copy()
                    .append(dailiesListBuilder());

            Minecraft.getInstance().player.displayClientMessage(notificationMessage, false);

            if (config.dailiesSendMethod == DailiesNotificationSendMethod.ONCE_PER_DAY) {

                DataManager.INSTANCE.lastDailyDate = new DataManager.JsonDate();
                DataManager.save();
            }
        }
    }


}

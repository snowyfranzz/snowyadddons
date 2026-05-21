package snowy.snowyaddons.client.modules.dailies;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.data.DataManager;

import java.util.*;
import java.util.function.Supplier;

public class DailiesManager {
    /*
    List of dailies

    Farming:
        - Pests
        - Greenhouse

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

    static ModConfig config = ModConfig.HANDLER.instance();

    public static List<Supplier<Boolean>> dailiesToggled = List.of(
            () -> config.dailiesShowPests,
            () -> config.dailiesShowGreenhouse,
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
    };


}

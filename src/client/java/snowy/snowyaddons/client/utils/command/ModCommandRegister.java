package snowy.snowyaddons.client.utils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.config.ModConfigScreen;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.data.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static snowy.snowyaddons.SnowyAddons.MOD_ID;

public class ModCommandRegister {

    public static Minecraft mc = Minecraft.getInstance();
    static Options options = mc.options;

    private static final Map<String, TogglePair> CONFIG_TOGGLES = new HashMap<>();
    private static final Map<String, DailyPair> DAILY_TOGGLES = new HashMap<>();

    static {
        // modules
        CONFIG_TOGGLES.put("batesp", new TogglePair("batEsp", () -> ModConfig.HANDLER.instance().batEsp, val -> ModConfig.HANDLER.instance().batEsp = val));
        CONFIG_TOGGLES.put("starmobesp", new TogglePair("starMobEsp", () -> ModConfig.HANDLER.instance().starMobEsp, val -> ModConfig.HANDLER.instance().starMobEsp = val));
        CONFIG_TOGGLES.put("playeresp", new TogglePair("playerEsp", () -> ModConfig.HANDLER.instance().playerEsp, val -> ModConfig.HANDLER.instance().playerEsp = val));
        CONFIG_TOGGLES.put("m2firefreezetimer", new TogglePair("m2FireFreezeTimer", () -> ModConfig.HANDLER.instance().m2FireFreeze, val -> ModConfig.HANDLER.instance().m2FireFreeze = val));
        CONFIG_TOGGLES.put("m2p2timer", new TogglePair("m2P2Timer", () -> ModConfig.HANDLER.instance().m2Phase2, val -> ModConfig.HANDLER.instance().m2Phase2 = val));
        CONFIG_TOGGLES.put("m2sentech", new TogglePair("m2SenTech", () -> ModConfig.HANDLER.instance().m2SenTech, val -> ModConfig.HANDLER.instance().m2SenTech = val));
        CONFIG_TOGGLES.put("bchelper", new TogglePair("m2BcHelper", () -> ModConfig.HANDLER.instance().bcHelper, val -> ModConfig.HANDLER.instance().bcHelper = val));
        CONFIG_TOGGLES.put("m2splits", new TogglePair("m2Splits", () -> ModConfig.HANDLER.instance().m2Splits, val -> ModConfig.HANDLER.instance().m2Splits = val));
        CONFIG_TOGGLES.put("autoquakecraft", new TogglePair("autoQuake", () -> ModConfig.HANDLER.instance().autoQuake, val -> ModConfig.HANDLER.instance().autoQuake = val));

        // dailies
        DAILY_TOGGLES.put("pests", new DailyPair("Pests", () -> DataManager.INSTANCE.dailiesPestsState, val -> DataManager.INSTANCE.dailiesPestsState = val));
        DAILY_TOGGLES.put("greenhouse", new DailyPair("Greenhouse", () -> DataManager.INSTANCE.dailiesGreenhouseState, val -> DataManager.INSTANCE.dailiesGreenhouseState = val));
        DAILY_TOGGLES.put("matriarch", new DailyPair("Matriarch", () -> DataManager.INSTANCE.dailiesMatriarchState, val -> DataManager.INSTANCE.dailiesMatriarchState = val));
        DAILY_TOGGLES.put("factionreputation", new DailyPair("Faction Reputation Quests", () -> DataManager.INSTANCE.dailiesReputationState, val -> DataManager.INSTANCE.dailiesReputationState = val));
        DAILY_TOGGLES.put("expbottleflip", new DailyPair("Grand EXP Bottles Flip", () -> DataManager.INSTANCE.dailiesBottleFlipState, val -> DataManager.INSTANCE.dailiesBottleFlipState = val));
        DAILY_TOGGLES.put("agathacontest", new DailyPair("Agatha's Contest", () -> DataManager.INSTANCE.dailiesAgathaState, val -> DataManager.INSTANCE.dailiesAgathaState = val));
        DAILY_TOGGLES.put("rabbithitman", new DailyPair("Claim Rabbit Hitman", () -> DataManager.INSTANCE.dailiesHitmanState, val -> DataManager.INSTANCE.dailiesHitmanState = val));
        DAILY_TOGGLES.put("chocolatefactory", new DailyPair("Manage Chocolate Factory", () -> DataManager.INSTANCE.dailiesChocoFactoryState, val -> DataManager.INSTANCE.dailiesChocoFactoryState = val));
        DAILY_TOGGLES.put("huntraps", new DailyPair("Claim and Replace Huntraps", () -> DataManager.INSTANCE.dailiesHuntrapsState, val -> DataManager.INSTANCE.dailiesHuntrapsState = val));
        DAILY_TOGGLES.put("dungeons", new DailyPair("Daily Dungeons XP Bonus", () -> DataManager.INSTANCE.dailiesDungeonState, val -> DataManager.INSTANCE.dailiesDungeonState = val));
        DAILY_TOGGLES.put("minions", new DailyPair("Claim Minions", () -> DataManager.INSTANCE.dailiesMinionsState, val -> DataManager.INSTANCE.dailiesMinionsState = val));
        DAILY_TOGGLES.put("experimentationtable", new DailyPair("Experimentation Table", () -> DataManager.INSTANCE.dailiesExpTableState, val -> DataManager.INSTANCE.dailiesExpTableState = val));
    }

    public static String textSeparatorBuilder() {
        StringBuilder separatorBuilder = new StringBuilder();
        for (double i = options.chatWidth().get(); i > (double) 1 / 52; i -= (double) 1 / 52) {
            separatorBuilder.append("-");
        }
        return separatorBuilder.toString();
    }

    public static void commandRegister(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(ClientCommandManager.literal("snowy")
                .executes(context -> openConfigScreen())

                .then(ClientCommandManager.literal("config")
                        .executes(context -> openConfigScreen())
                )

                .then(ClientCommandManager.literal("help")
                        .executes(ModCommandRegister::handleHelpCommand)
                )

                .then(ClientCommandManager.literal("version")
                        .executes(context -> {
                            String version = FabricLoader.getInstance()
                                    .getModContainer(MOD_ID)
                                    .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                                    .orElse("0.0.0");
                            Component formattedMessage = Component.literal(MOD_ID).withStyle(ChatFormatting.AQUA)
                                    .append(" Version " + version).withStyle(ChatFormatting.GOLD);
                            context.getSource().sendFeedback(formattedMessage);
                            return 1;
                        })
                )

                .then(ClientCommandManager.literal("boop")
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Boop!").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                            return 1;
                        })
                )

                // --- TOGGLE HANDLER ---
                .then(ClientCommandManager.literal("toggle")
                        .then(ClientCommandManager.argument("module", StringArgumentType.word())
                                .suggests((ctx, cb) -> suggestFromMap(cb, CONFIG_TOGGLES))
                                .executes(context -> {
                                    String input = StringArgumentType.getString(context, "module").toLowerCase();
                                    TogglePair pair = CONFIG_TOGGLES.get(input);

                                    if (pair != null) {
                                        boolean newValue = !pair.getter.get();
                                        pair.setter.accept(newValue);
                                        ModConfig.HANDLER.save();
                                        String prefix = newValue ? "[+] SnowyAddons: Toggled " : "[-] SnowyAddons: Toggled ";
                                        String suffix = newValue ? " on!" : " off!";
                                        context.getSource().sendFeedback(Component.literal(prefix + pair.displayName + suffix).withStyle(ChatFormatting.AQUA));
                                    } else {
                                        context.getSource().sendFeedback(Component.literal("Unknown module! Try tab-completing options.").withStyle(ChatFormatting.DARK_RED));
                                    }
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Usage: /snowyaddons toggle <module>").withStyle(ChatFormatting.DARK_RED));
                            return 1;
                        })
                )

                // --- DAILIES HANDLER ---
                .then(ClientCommandManager.literal("dailies")
                        .then(ClientCommandManager.argument("option", StringArgumentType.word())
                                .suggests((ctx, cb) -> suggestFromMap(cb, DAILY_TOGGLES))
                                .executes(context -> {
                                    String input = StringArgumentType.getString(context, "option").toLowerCase();
                                    DailyPair pair = DAILY_TOGGLES.get(input);

                                    if (pair != null) {
                                        boolean newValue = !pair.getter.get();
                                        pair.setter.accept(newValue);
                                        String status = newValue ? "[+] SnowyAddons: Checked " : "[-] SnowyAddons: Unchecked ";
                                        String suffix = newValue ? " as complete!" : "!";
                                        ChatFormatting color = newValue ? ChatFormatting.GREEN : ChatFormatting.RED;

                                        context.getSource().sendFeedback(Component.literal(status + pair.displayName + suffix).withStyle(color));
                                        DataManager.save();
                                    } else {
                                        context.getSource().sendFeedback(Component.literal("Unknown daily target!").withStyle(ChatFormatting.DARK_RED));
                                    }
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            mc.execute(() -> {
                                context.getSource().sendFeedback(Component.literal("[SnowyAddons] Dailies list:").withStyle(ChatFormatting.AQUA));
                                context.getSource().sendFeedback(DailiesManager.dailiesListBuilder());
                            });
                            return 1;
                        })
                )
        );
    }

    private static int openConfigScreen() {
        mc.execute(() -> {
            Screen configScreen = ModConfigScreen.create(mc.screen, ModConfig.HANDLER.instance());
            mc.setScreen(configScreen);
        });
        return 1;
    }

    private static int handleHelpCommand(CommandContext<FabricClientCommandSource> context) {
        var source = context.getSource();
        String linesFormattedToWidth = textSeparatorBuilder();
        source.sendFeedback(Component.literal(linesFormattedToWidth).withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal(" SnowyAddons Help:").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal(""));
        source.sendFeedback(Component.literal("  /snowy addons -> Opens the mod screen.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy help -> Sends a message in chat with all commands.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy config -> Opens the config screen.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy version -> Sends the version in chat.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy boop -> Replies with a Boop!").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy toggle <module> -> Enables / Disables a module.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal("  /snowy dailies <option> -> Lists dailies and toggles them.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal(linesFormattedToWidth).withStyle(ChatFormatting.DARK_GREEN));
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestFromMap(SuggestionsBuilder builder, Map<String, ?> map) {
        for (String key : map.keySet()) {
            builder.suggest(key);
        }
        return builder.buildFuture();
    }

    private static class TogglePair {
        final String displayName;
        final Supplier<Boolean> getter;
        final Consumer<Boolean> setter;
        TogglePair(String dn, Supplier<Boolean> g, Consumer<Boolean> s) { this.displayName = dn; this.getter = g; this.setter = s; }
    }

    private static class DailyPair {
        final String displayName;
        final Supplier<Boolean> getter;
        final Consumer<Boolean> setter;
        DailyPair(String dn, Supplier<Boolean> g, Consumer<Boolean> s) { this.displayName = dn; this.getter = g; this.setter = s; }
    }
}
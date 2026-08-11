package snowy.snowyaddons.client.utils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import snowy.snowyaddons.client.SnowyAddonsClient;
import snowy.snowyaddons.client.config.ModConfigScreen;
import snowy.snowyaddons.client.modules.dailies.DailiesManager;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.SkyblockIsland;
import snowy.snowyaddons.client.utils.audio.JukeboxAudioLibrary;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.data.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        CONFIG_TOGGLES.put("jukebox", new TogglePair("jukebox", () -> ModConfig.HANDLER.instance().jukeboxEnabled, val -> ModConfig.HANDLER.instance().jukeboxEnabled = val));

        // dailies
        DAILY_TOGGLES.put("pests", new DailyPair("Pests", () -> DataManager.INSTANCE.dailiesPestsState, val -> DataManager.INSTANCE.dailiesPestsState = val));
        DAILY_TOGGLES.put("greenhouse", new DailyPair("Greenhouse", () -> DataManager.INSTANCE.dailiesGreenhouseState, val -> DataManager.INSTANCE.dailiesGreenhouseState = val));
        DAILY_TOGGLES.put("visitors", new DailyPair("Visitors", () -> DataManager.INSTANCE.dailiesVisitorState, val -> DataManager.INSTANCE.dailiesVisitorState = val));
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
        dispatcher.register(ClientCommands.literal("snowy")
                .executes(context -> openConfigScreen())

                .then(ClientCommands.literal("config")
                        .executes(context -> openConfigScreen())
                )

                .then(ClientCommands.literal("help")
                        .executes(ModCommandRegister::handleHelpCommand)
                )

                .then(ClientCommands.literal("version")
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

                .then(ClientCommands.literal("boop")
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Boop!").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                            return 1;
                        })
                )

                // --- TOGGLE HANDLER ---
                .then(ClientCommands.literal("toggle")
                        .then(ClientCommands.argument("module", StringArgumentType.word())
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
                .then(ClientCommands.literal("dailies")
                        .then(ClientCommands.argument("option", StringArgumentType.word())
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

                // --- JUKEBOX HANDLER ---
                .then(ClientCommands.literal("jukebox")
                        .then(ClientCommands.literal("play").executes(ModCommandRegister::handleJukeboxPlay))
                        .then(ClientCommands.literal("pause").executes(ModCommandRegister::handleJukeboxPause))
                        .then(ClientCommands.literal("skip").executes(ModCommandRegister::handleJukeboxSkip))
                        .then(ClientCommands.literal("status").executes(ModCommandRegister::handleJukeboxStatus))
                        .then(ClientCommands.literal("debug").executes(ModCommandRegister::handleJukeboxDebug))
                        .then(ClientCommands.literal("islands").executes(ModCommandRegister::handleJukeboxIslands))
                        .then(ClientCommands.literal("files").executes(ModCommandRegister::handleJukeboxFiles))
                        .then(ClientCommands.literal("folder").executes(ModCommandRegister::handleJukeboxFolder))

                        .then(ClientCommands.literal("playlist")
                                .then(ClientCommands.argument("island", StringArgumentType.word())
                                        .suggests(ModCommandRegister::suggestIslands)
                                        .executes(ModCommandRegister::handleJukeboxPlaylist))
                        )

                        .then(ClientCommands.literal("add")
                                .then(ClientCommands.argument("island", StringArgumentType.word())
                                        .suggests(ModCommandRegister::suggestIslands)
                                        .then(ClientCommands.argument("file", StringArgumentType.greedyString())
                                                .suggests(ModCommandRegister::suggestAudioFiles)
                                                .executes(ModCommandRegister::handleJukeboxAdd))
                                )
                        )

                        .then(ClientCommands.literal("remove")
                                .then(ClientCommands.argument("island", StringArgumentType.word())
                                        .suggests(ModCommandRegister::suggestIslands)
                                        .then(ClientCommands.argument("file", StringArgumentType.greedyString())
                                                .suggests(ModCommandRegister::suggestPlaylistFiles)
                                                .executes(ModCommandRegister::handleJukeboxRemove))
                                )
                        )

                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Usage: /snowy jukebox <play|pause|skip|status|debug|islands|files|folder|playlist|add|remove>").withStyle(ChatFormatting.DARK_RED));
                            return 1;
                        })
                )
        );
    }

    private static int openConfigScreen() {
        mc.execute(() -> {
            Screen configScreen = ModConfigScreen.create(mc.gui.screen(), ModConfig.HANDLER.instance());
            mc.gui.setScreen(configScreen);
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
        source.sendFeedback(Component.literal("  /snowy jukebox <play|pause|skip|status|islands|files|folder|playlist|add|remove> -> Manages per-island music playlists.").withStyle(ChatFormatting.DARK_GREEN));
        source.sendFeedback(Component.literal(linesFormattedToWidth).withStyle(ChatFormatting.DARK_GREEN));
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestFromMap(SuggestionsBuilder builder, Map<String, ?> map) {
        for (String key : map.keySet()) {
            builder.suggest(key);
        }
        return builder.buildFuture();
    }

    // --- JUKEBOX ---

    private static int handleJukeboxPlay(CommandContext<FabricClientCommandSource> context) {
        SnowyAddonsClient.jukebox.play();
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Jukebox resumed.").withStyle(ChatFormatting.AQUA));
        return 1;
    }

    private static int handleJukeboxPause(CommandContext<FabricClientCommandSource> context) {
        SnowyAddonsClient.jukebox.pause();
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Jukebox paused.").withStyle(ChatFormatting.AQUA));
        return 1;
    }

    private static int handleJukeboxSkip(CommandContext<FabricClientCommandSource> context) {
        SnowyAddonsClient.jukebox.skip();
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Skipped to the next track.").withStyle(ChatFormatting.AQUA));
        return 1;
    }

    private static int handleJukeboxStatus(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(SnowyAddonsClient.jukebox.getStatusMessage());
        return 1;
    }

    private static int handleJukeboxDebug(CommandContext<FabricClientCommandSource> context) {
        var source = context.getSource();
        ModConfig config = ModConfig.HANDLER.instance();

        SkyblockIsland island = GetServerInfo.getCurrentIsland();

        List<String> playlist = DataManager.INSTANCE.jukeboxPlaylists.getOrDefault(island.getId(), List.of());

        source.sendFeedback(Component.literal("[SnowyAddons] Jukebox debug:").withStyle(ChatFormatting.AQUA));
        source.sendFeedback(Component.literal(" jukeboxEnabled = " + config.jukeboxEnabled).withStyle(ChatFormatting.GRAY));
        source.sendFeedback(Component.literal(" isInSkyBlock() = " + GetServerInfo.isInSkyBlock()).withStyle(ChatFormatting.GRAY));
        source.sendFeedback(Component.literal(" isInDungeon() = " + GetServerInfo.isInDungeon()).withStyle(ChatFormatting.GRAY));
        source.sendFeedback(Component.literal(" getCurrentIsland() = " + island.getId() + " (" + island.getDisplayName() + ")").withStyle(ChatFormatting.GRAY));
        source.sendFeedback(Component.literal(" playlist size for that island = " + playlist.size()).withStyle(ChatFormatting.GRAY));
        source.sendFeedback(Component.literal(" Raw sidebar lines (color codes stripped):").withStyle(ChatFormatting.YELLOW));

        List<String> lines = GetServerInfo.getSidebarLines();
        if (lines.isEmpty()) {
            source.sendFeedback(Component.literal("  (no sidebar objective found)").withStyle(ChatFormatting.DARK_RED));
        } else {
            for (String line : lines) {
                source.sendFeedback(Component.literal("  \"" + line + "\"").withStyle(ChatFormatting.WHITE));
            }
        }
        return 1;
    }

    private static int handleJukeboxIslands(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Islands:").withStyle(ChatFormatting.AQUA));
        for (SkyblockIsland island : SkyblockIsland.values()) {
            if (island == SkyblockIsland.UNKNOWN) continue;
            context.getSource().sendFeedback(Component.literal(" - " + island.getId() + " (" + island.getDisplayName() + ")").withStyle(ChatFormatting.GRAY));
        }
        return 1;
    }

    private static int handleJukeboxFiles(CommandContext<FabricClientCommandSource> context) {
        List<String> files = JukeboxAudioLibrary.listAudioFiles();
        if (files.isEmpty()) {
            context.getSource().sendFeedback(Component.literal("[SnowyAddons] No .wav files found in " + JukeboxAudioLibrary.getAudioDir()).withStyle(ChatFormatting.DARK_RED));
        } else {
            context.getSource().sendFeedback(Component.literal("[SnowyAddons] Available audio files:").withStyle(ChatFormatting.AQUA));
            for (String file : files) {
                context.getSource().sendFeedback(Component.literal(" - " + file).withStyle(ChatFormatting.GRAY));
            }
        }
        return 1;
    }

    private static int handleJukeboxFolder(CommandContext<FabricClientCommandSource> context) {
        JukeboxAudioLibrary.ensureDirExists();
        Util.getPlatform().openFile(JukeboxAudioLibrary.getAudioDir().toFile());
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Opening the jukebox audio folder... Drop .wav files in there!").withStyle(ChatFormatting.AQUA));
        return 1;
    }

    private static int handleJukeboxPlaylist(CommandContext<FabricClientCommandSource> context) {
        SkyblockIsland island = SkyblockIsland.fromId(StringArgumentType.getString(context, "island").toLowerCase());
        if (island == null) {
            context.getSource().sendFeedback(Component.literal("Unknown island! Try tab-completing options.").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        List<String> playlist = DataManager.INSTANCE.jukeboxPlaylists.getOrDefault(island.getId(), List.of());
        context.getSource().sendFeedback(Component.literal("[SnowyAddons] Playlist for " + island.getDisplayName() + ":").withStyle(ChatFormatting.AQUA));
        if (playlist.isEmpty()) {
            context.getSource().sendFeedback(Component.literal(" (empty)").withStyle(ChatFormatting.GRAY));
        } else {
            for (String file : playlist) {
                context.getSource().sendFeedback(Component.literal(" - " + file).withStyle(ChatFormatting.GRAY));
            }
        }
        return 1;
    }

    private static int handleJukeboxAdd(CommandContext<FabricClientCommandSource> context) {
        SkyblockIsland island = SkyblockIsland.fromId(StringArgumentType.getString(context, "island").toLowerCase());
        String file = StringArgumentType.getString(context, "file");

        if (island == null) {
            context.getSource().sendFeedback(Component.literal("Unknown island! Try tab-completing options.").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        if (!JukeboxAudioLibrary.listAudioFiles().contains(file)) {
            context.getSource().sendFeedback(Component.literal("That file wasn't found in the jukebox audio folder. Run /snowy jukebox files to see what's available.").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        List<String> playlist = DataManager.INSTANCE.jukeboxPlaylists.computeIfAbsent(island.getId(), key -> new ArrayList<>());
        if (playlist.contains(file)) {
            context.getSource().sendFeedback(Component.literal("That track is already in the playlist for " + island.getDisplayName() + ".").withStyle(ChatFormatting.YELLOW));
            return 1;
        }

        playlist.add(file);
        DataManager.save();
        context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Added \"" + file + "\" to " + island.getDisplayName() + "'s playlist.").withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int handleJukeboxRemove(CommandContext<FabricClientCommandSource> context) {
        SkyblockIsland island = SkyblockIsland.fromId(StringArgumentType.getString(context, "island").toLowerCase());
        String file = StringArgumentType.getString(context, "file");

        if (island == null) {
            context.getSource().sendFeedback(Component.literal("Unknown island! Try tab-completing options.").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        List<String> playlist = DataManager.INSTANCE.jukeboxPlaylists.get(island.getId());
        if (playlist == null || !playlist.remove(file)) {
            context.getSource().sendFeedback(Component.literal("That track wasn't in the playlist for " + island.getDisplayName() + ".").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        DataManager.save();
        context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Removed \"" + file + "\" from " + island.getDisplayName() + "'s playlist.").withStyle(ChatFormatting.RED));
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestIslands(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        for (SkyblockIsland island : SkyblockIsland.values()) {
            if (island != SkyblockIsland.UNKNOWN) builder.suggest(island.getId());
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestAudioFiles(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        for (String file : JukeboxAudioLibrary.listAudioFiles()) {
            builder.suggest(file);
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestPlaylistFiles(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        try {
            String islandId = StringArgumentType.getString(context, "island").toLowerCase();
            List<String> playlist = DataManager.INSTANCE.jukeboxPlaylists.get(islandId);
            if (playlist != null) {
                for (String file : playlist) builder.suggest(file);
            }
        } catch (IllegalArgumentException ignored) {
            // "island" argument not resolved yet
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
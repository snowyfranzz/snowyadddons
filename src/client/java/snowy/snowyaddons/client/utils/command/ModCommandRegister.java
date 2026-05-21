package snowy.snowyaddons.client.utils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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

import java.awt.*;

import static snowy.snowyaddons.SnowyAddons.MOD_ID;

/*

    COMMAND LIST:

    /snowyaddons -> Opens the mod screen.
    /snowyaddons help -> Sends a message in chat with all commands.
    /snowyaddons config -> Opens the config screen.
    /snowyaddons version -> Sends the version in chat.
    /snowyaddons boop -> Replies with a Boop!
    /snowyaddons toggle <module> -> Enables / Disables a module.
    /snowyaddons daily <check> <daily> -> Lists dailies and toggles them as completed/uncompleted.

 */



public class ModCommandRegister {

    public static Minecraft mc = Minecraft.getInstance();
    static Options options = mc.options;

    public static String textSeparatorBuilder(){
        StringBuilder separatorBuilder = new StringBuilder();

        for(double i = options.chatWidth().get(); i>(double)1/52; i-=(double)1/52){
            separatorBuilder.append("-");
        }

        return separatorBuilder.toString();
    }

    public static String linesFormattedToWidth = textSeparatorBuilder();

    public static void commandRegister(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        // snowy (main command)
        dispatcher.register(ClientCommandManager.literal("snowy")
                .executes(context -> {
                    mc.execute(() -> {
                        Screen currentScreen = mc.screen;
                        Screen configScreen = ModConfigScreen.create(currentScreen, ModConfig.HANDLER.instance());
                        mc.setScreen(configScreen);
                    });
                    return 1;
                })
                // config subcommand
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            mc.execute(() -> {
                                Screen currentScreen = mc.screen;
                                Screen configScreen = ModConfigScreen.create(currentScreen, ModConfig.HANDLER.instance());
                                mc.setScreen(configScreen);
                            });
                            return 1;
                        })
                )
                // help subcommand
                .then(ClientCommandManager.literal("help")
                        .executes(context -> {
                            var source = context.getSource();

                            source.sendFeedback(Component.literal(linesFormattedToWidth                                                            ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(" SnowyAddons Help:"                                                      ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(""                                                                        ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons -> Opens the mod screen."                                 ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons help -> Sends a message in chat with all commands."       ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons config -> Opens the config screen."                       ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons version -> Sends the version in chat."                    ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons boop -> Replies with a Boop!"                             ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons toggle <module> -> Enables / Disables a module."          ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons daily <check> <daily> -> Lists dailies and toggles them." ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(linesFormattedToWidth                                                            ).withStyle(ChatFormatting.DARK_GREEN));
                            return 1;
                        })
                )
                // version subcommand
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
                // boop subcommand
                .then(ClientCommandManager.literal("boop")
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Boop!").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                            return 1;
                        })
                )
                // toggle subcommand
                .then(ClientCommandManager.literal("toggle")

                        // TODO: Compress multiple statements into one using brigadier

                        .then(ClientCommandManager.literal("batEsp")
                                .executes(context -> {
                                    ModConfig.HANDLER.instance().batEsp = !ModConfig.HANDLER.instance().batEsp;
                                    if(ModConfig.HANDLER.instance().batEsp){
                                        context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Toggled batEsp on!").withStyle(ChatFormatting.AQUA));
                                    } else {
                                        context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Toggled batEsp off!").withStyle(ChatFormatting.AQUA));
                                    }
                                    return 1;
                                })
                        )
                        .then(ClientCommandManager.literal("starMobEsp")
                                .executes(context -> {
                                    ModConfig.HANDLER.instance().starMobEsp = !ModConfig.HANDLER.instance().starMobEsp;
                                    if(ModConfig.HANDLER.instance().starMobEsp){
                                        context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Toggled starMobEsp on!").withStyle(ChatFormatting.AQUA));
                                    } else {
                                        context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Toggled starMobEsp off!").withStyle(ChatFormatting.AQUA));
                                    }
                                    return 1;
                                })
                        )
                        .then(ClientCommandManager.literal("playerEsp")
                                .executes(context -> {
                                    ModConfig.HANDLER.instance().playerEsp = !ModConfig.HANDLER.instance().playerEsp;
                                    if (ModConfig.HANDLER.instance().playerEsp) {
                                        context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Toggled playerEsp on!").withStyle(ChatFormatting.AQUA));
                                    } else {
                                        context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Toggled playerEsp off!").withStyle(ChatFormatting.AQUA));
                                    }
                                    return 1;
                                }))
                        // fallback
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Usage: /snowyaddons toggle <module>").withStyle(ChatFormatting.DARK_RED));
                            return 1;
                        })
                )
                // dailies subcommand
                .then(ClientCommandManager.literal("dailies")
                        .executes(context -> {
                            mc.execute(() -> {

                                context.getSource().sendFeedback(DailiesManager.dailiesListBuilder()); // DailiesManager.java

                            });
                            return 1;
                        })
                        // check subcommand
                        .then(ClientCommandManager.literal("check")

                                // TODO: Compress multiple statements into one using brigadier

                                .then(ClientCommandManager.literal("pests")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesPestsState = !DataManager.INSTANCE.dailiesPestsState;
                                            if (DataManager.INSTANCE.dailiesPestsState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Pests as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Pests!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("greenhouse")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesGreenhouseState = !DataManager.INSTANCE.dailiesGreenhouseState;
                                            if (DataManager.INSTANCE.dailiesGreenhouseState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Greenhouse as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Greenhouse!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("matriarch")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesMatriarchState = !DataManager.INSTANCE.dailiesMatriarchState;
                                            if (DataManager.INSTANCE.dailiesMatriarchState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Matriarch as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Matriarch!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("factionReputation")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesReputationState = !DataManager.INSTANCE.dailiesReputationState;
                                            if (DataManager.INSTANCE.dailiesReputationState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Faction Reputation Quests as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Faction Reputation Quests!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("expBottleFlip")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesBottleFlipState = !DataManager.INSTANCE.dailiesBottleFlipState;
                                            if (DataManager.INSTANCE.dailiesBottleFlipState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Grand EXP Bottles Flip as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Grand EXP Bottles Flip!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("agathaContest")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesAgathaState = !DataManager.INSTANCE.dailiesAgathaState;
                                            if (DataManager.INSTANCE.dailiesAgathaState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Agatha's Contest as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Agatha's Contest Bottles Flip!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("rabbitHitman")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesHitmanState = !DataManager.INSTANCE.dailiesHitmanState;
                                            if (DataManager.INSTANCE.dailiesHitmanState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Claim Rabbit Hitman as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Claim Rabbit Hitman!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("chocolateFactory")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesChocoFactoryState = !DataManager.INSTANCE.dailiesChocoFactoryState;
                                            if (DataManager.INSTANCE.dailiesChocoFactoryState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Manage Chocolate Factory as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Manage Chocolate Factory!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("huntraps")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesHuntrapsState = !DataManager.INSTANCE.dailiesHuntrapsState;
                                            if (DataManager.INSTANCE.dailiesHuntrapsState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Claim and Replace Huntraps as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Clam and Replace Huntraps!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("dungeons")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesDungeonState = !DataManager.INSTANCE.dailiesDungeonState;
                                            if (DataManager.INSTANCE.dailiesDungeonState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Daily Dungeons XP Bonus as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Daily Dungeons XP Bonus!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("minions")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesMinionsState = !DataManager.INSTANCE.dailiesMinionsState;
                                            if (DataManager.INSTANCE.dailiesMinionsState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Claim Minions as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Claim Minions!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .then(ClientCommandManager.literal("experimentationTable")
                                        .executes(context -> {
                                            DataManager.INSTANCE.dailiesExpTableState = !DataManager.INSTANCE.dailiesExpTableState;
                                            if (DataManager.INSTANCE.dailiesExpTableState) {
                                                context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Checked Experimentation Table as complete!").withStyle(ChatFormatting.GREEN));
                                            } else {
                                                context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Unchecked Experimentation Table!").withStyle(ChatFormatting.RED));
                                            }
                                            DataManager.save();
                                            return 1;
                                        })
                                )

                                .executes(context -> {
                                mc.execute(() -> {

                                    context.getSource().sendFeedback(Component.literal("Usage: /snowyaddons dailies check <option>").withStyle(ChatFormatting.DARK_RED));

                                });
                                return 1;
                                })
                        )
                )
        );
    }
}


package snowy.snowyaddons.client.utils.command;

import com.mojang.brigadier.CommandDispatcher;
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
import snowy.snowyaddons.config.ModConfig;

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

 */



public class ModCommandRegister {

    static Options options = Minecraft.getInstance().options;

    public static void commandRegister(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        // snowy (main command)
        dispatcher.register(ClientCommandManager.literal("snowy")
                .executes(context -> {
                    Minecraft mc = Minecraft.getInstance();
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
                            Minecraft mc = Minecraft.getInstance();
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

                            StringBuilder separatorBuilder = new StringBuilder();

                            for(double i = options.chatWidth().get(); i>(double)1/52; i-=(double)1/52){
                                separatorBuilder.append("-");
                            }

                            String linesFormattedToWidth = separatorBuilder.toString();

                            source.sendFeedback(Component.literal(linesFormattedToWidth                                                             ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(" SnowyAddons Help:"                                                         ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(""                                                                        ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons -> Opens the mod screen."                                 ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons help -> Sends a message in chat with all commands."       ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons config -> Opens the config screen."                       ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons version -> Sends the version in chat."                    ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons boop -> Replies with a Boop!"                             ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal("  /snowyaddons toggle <module> -> Enables / Disables a module."          ).withStyle(ChatFormatting.DARK_GREEN));
                            source.sendFeedback(Component.literal(linesFormattedToWidth                                                             ).withStyle(ChatFormatting.DARK_GREEN));
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
                                    .append(" Version " + version).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
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
                        .executes(context -> {
                            context.getSource().sendFeedback(Component.literal("Usage: /snowyaddons toggle <module>").withStyle(ChatFormatting.RED));
                            return 1;
                        })
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
                                if(ModConfig.HANDLER.instance().playerEsp){
                                    context.getSource().sendFeedback(Component.literal("[+] SnowyAddons: Toggled playerEsp on!").withStyle(ChatFormatting.AQUA));
                                } else {
                                    context.getSource().sendFeedback(Component.literal("[-] SnowyAddons: Toggled playerEsp off!").withStyle(ChatFormatting.AQUA));
                                }
                                return 1;
                        }))
                )
        );
    }
}


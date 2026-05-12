package snowy.snowyaddons.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.SnowyAddons;

public class ModConfigScreen {

    public static Screen create(Screen parent, ModConfig config) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("SnowyAddons Config"))
                .save(ModConfig.HANDLER::save)
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Settings"))
                        .tooltip(Component.literal("What else do you wanna know bro </3"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("BatEsp"))
                                .description(OptionDescription.of(Component.literal("Highlights bat secrets through walls so you don't lose them and throw the run!")))
                                .binding(
                                        false,
                                        () -> ModConfig.HANDLER.instance().batEsp,
                                        newVal -> ModConfig.HANDLER.instance().batEsp = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build()) // Builds the Category
                .build() // Builds the Library
                .generateScreen(parent); // Generates MC Screen
    }
}
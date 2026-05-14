package snowy.snowyaddons.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.SnowyAddons;

import java.awt.*;

public class ModConfigScreen {

    public static Screen create(Screen parent, ModConfig config) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("SnowyAddons Config"))
                .save(ModConfig.HANDLER::save)
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Render"))
                        .tooltip(Component.literal("Modules that renders information on your screen."))

                        // bat esp start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Bat ESP"))
                                .description(OptionDescription.of(Component.literal("Highlights bat secrets through walls so you don't lose them.")))
                                .binding(
                                        false,
                                        () -> ModConfig.HANDLER.instance().batEsp,
                                        newVal -> ModConfig.HANDLER.instance().batEsp = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // bat esp end

                        // bat esp color picker start
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Glow Color"))
                                .description(OptionDescription.of(Component.literal("Changes the glow color of the Bat ESP.")))
                                .binding(
                                        new Color(255, 165, 0),
                                        () -> ModConfig.HANDLER.instance().batEspColor,
                                        newVal -> ModConfig.HANDLER.instance().batEspColor = newVal
                                )
                                .controller(ColorControllerBuilder::create)
                                .build()) // bat esp color picker end

                        // star mob esp start
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Star Mob ESP"))
                                .description(OptionDescription.of(Component.literal("Highlights star mobs through walls so you don't lose them.")))
                                .binding(
                                        false,
                                        () -> ModConfig.HANDLER.instance().starMobEsp,
                                        newVal -> ModConfig.HANDLER.instance().starMobEsp = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // star mob esp end

                        // star mob esp color picker start
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Glow Color"))
                                .description(OptionDescription.of(Component.literal("Changes the glow color of the Star Mob ESP.")))
                                .binding(
                                        new Color(127, 0, 255),
                                        () -> ModConfig.HANDLER.instance().starMobEspColor,
                                        newVal -> ModConfig.HANDLER.instance().starMobEspColor = newVal
                                )
                                .controller(ColorControllerBuilder::create)
                                .build()) // star mob esp color picker end

                        // player esp start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Player ESP"))
                                .description(OptionDescription.of(Component.literal("Highlights players through walls. Wallhax!")))
                                .binding(
                                        false,
                                        () -> ModConfig.HANDLER.instance().playerEsp,
                                        newVal -> ModConfig.HANDLER.instance().playerEsp = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // player esp end

                        // player esp color picker start
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Glow Color"))
                                .description(OptionDescription.of(Component.literal("Changes the glow color of the Player ESP.")))
                                .binding(
                                        new Color(255, 0, 0),
                                        () -> ModConfig.HANDLER.instance().playerEspColor,
                                        newVal -> ModConfig.HANDLER.instance().playerEspColor = newVal
                                )
                                .controller(ColorControllerBuilder::create)
                                .build()) // player esp color picker end

                        // player esp start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Render yourself on ESP"))
                                .description(OptionDescription.of(Component.literal("Renders yourself on Player ESP. Idk why you'd want this but you do you twin!")))
                                .binding(
                                        false,
                                        () -> ModConfig.HANDLER.instance().selfRenderPlayerEsp,
                                        newVal -> ModConfig.HANDLER.instance().selfRenderPlayerEsp = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // player esp end

                        .build()) // category

                .build().generateScreen(parent); // mc screen
    }
}
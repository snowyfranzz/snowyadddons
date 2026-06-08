package snowy.snowyaddons.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.config.dailies.DailiesNotificationSendMethod;
import snowy.snowyaddons.config.ModConfig;

import java.awt.*;

public class ModConfigScreen {

    public static Screen create(Screen parent, ModConfig config) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("SnowyAddons Config"))
                .save(ModConfig.HANDLER::save)

                // dungeons category start
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Dungeons"))
                        .tooltip(Component.literal("Best skill in the game btw"))

                        // m2 group
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("M2"))
                                .collapsed(false)

                                // ff timer start
                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Phase 2 Fire Freeze timer"))
                                        .description(OptionDescription.of(Component.literal("Draws a timer on your screen indicating when to use your fire freeze staff. Can be used to freeze scarf or the undeads!")))
                                        .binding(
                                                false,
                                                () -> ModConfig.HANDLER.instance().m2FireFreeze,
                                                newVal -> ModConfig.HANDLER.instance().m2FireFreeze = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build()) // ff timer end

                                // spray timer start
                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Phase 2 Ice Spray timer"))
                                        .description(OptionDescription.of(Component.literal("Draws a timer on your screen indicating when phase 2 starts. Can be used to freeze/gyro scarf or the undeads!")))
                                        .binding(
                                                false,
                                                () -> ModConfig.HANDLER.instance().m2IceSpray,
                                                newVal -> ModConfig.HANDLER.instance().m2IceSpray = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build()) // spray timer end

                                .build())
                        //m2 group end

                        .build()) // dungeons category end

                // render category start
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Render"))
                        .tooltip(Component.literal("Modules that renders information on your screen."))

                // bat esp group
                        .group(OptionGroup.createBuilder()
                            .name(Component.literal("Bat ESP"))
                            .collapsed(false)

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
                        .build())
                // bat esp group end


                // star mob esp group start
                        .group(OptionGroup.createBuilder()
                            .name(Component.literal("Star Mob ESP"))
                            .collapsed(false)

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
                        .build())
                        // star mob esp group end

                        // player esp group start
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Player ESP"))
                                .collapsed(false)

                        // player esp start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Player ESP"))
                                .description(OptionDescription.of(Component.literal("Highlights players through walls. Wallhax! (Quakecraft ahh module)")))
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
                        .build())
                        //player esp group end

                        .build()) // render category end

                // dailies category start
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Dailies"))
                        .tooltip(Component.literal("Options for the dailies checklist feature."))

                        // dailies feature toggle start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Dailies checklist notification"))
                                .description(OptionDescription.of(Component.literal("Toggle the automatic send of the dailies checklist. You can still access it through /snowy dailies with this off, you just won't be notified.")))
                                .binding(
                                        true,
                                        () -> ModConfig.HANDLER.instance().dailiesNotification,
                                        newVal -> ModConfig.HANDLER.instance().dailiesNotification = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // dailies feature toggle end

                        // dailies send option start
                        .option(Option.<snowy.snowyaddons.config.dailies.DailiesNotificationSendMethod>createBuilder()

                                .name(Component.literal("When to send notifications"))
                                .description(OptionDescription.of(Component.literal("Change when the notifications will be sent to the user. This only works if the dailies checlist notification option is on!" +
                                        "\n     - PER_SESSION: Sends the notification once every time you open Minecraft.\n" +
                                        "\n     - ONCE_PER_DAY: Sends the notification once in the day (first time you open Minecraft). This checks system time!\n" +
                                        "\n     - WHILE_UNCOMPLETED: Sends the notification periodically while it's not marked as done. Resets every day.\n")))
                                .binding(
                                        DailiesNotificationSendMethod.PER_SESSION,
                                        () -> ModConfig.HANDLER.instance().dailiesSendMethod,
                                        newVal -> ModConfig.HANDLER.instance().dailiesSendMethod = newVal
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(DailiesNotificationSendMethod.class))
                                .build()) // dailies send option end

                        // dailies feature toggle start
                        .option(Option.<Integer>createBuilder()

                                .name(Component.literal("Notification Delay"))
                                .description(OptionDescription.of(Component.literal("Allows you to change the time between the notifications sent by the WHILE_UNCOMPLETED option. This won't affect anything if you don't have that option selected.")))
                                .binding(
                                        15,
                                        () -> ModConfig.HANDLER.instance().whileUncompletedSleep,
                                        newVal -> ModConfig.HANDLER.instance().whileUncompletedSleep = newVal
                                )
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(5, 120)
                                        .step(1)
                                        .formatValue(value -> Component.literal(value + " Minutes"))
                                )
                                .build()) // dailies feature toggle end

                        // dailies show completed toggle start
                        .option(Option.<Boolean>createBuilder()

                                .name(Component.literal("Show Completed Dailies"))
                                .description(OptionDescription.of(Component.literal("Toggle if you want to see the dailies you've completed when you run /snowy dailies.")))
                                .binding(
                                        true,
                                        () -> ModConfig.HANDLER.instance().dailiesShowCompleted,
                                        newVal -> ModConfig.HANDLER.instance().dailiesShowCompleted = newVal
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build()) // dailies show completed toggle end

                        // dailies - garden group start
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Dailies - Garden"))
                                .collapsed(true)

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Pests"))
                                        .description(OptionDescription.of(Component.literal("Shows pests in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowPests,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowPests = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Greenhouse"))
                                        .description(OptionDescription.of(Component.literal("Shows greenhouse in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowGreenhouse,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowGreenhouse = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build()) // dailies - garden group end

                        // dailies - crimson isles group start
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Dailies - Crimson Isles"))
                                .collapsed(true)

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Matriarch (Heavy Pearls)"))
                                        .description(OptionDescription.of(Component.literal("Shows Matriarch in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowMatriarch,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowMatriarch = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Reputation Quests"))
                                        .description(OptionDescription.of(Component.literal("Shows reputation quests in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowReputation,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowReputation = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Grand Exp Bottle Flip"))
                                        .description(OptionDescription.of(Component.literal("Shows Grand EXP Bottle flip in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowBottlesFlip,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowBottlesFlip = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build()) // dailies - crimson isles group end

                        // dailies - Galatea group start
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Dailies - Galatea"))
                                .collapsed(true)

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Agatha's Contest"))
                                        .description(OptionDescription.of(Component.literal("Shows Agatha's Contest in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowAgatha,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowAgatha = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build()) // dailies - Galatea group end

                        // dailies - misc group start
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Dailies - Misc."))
                                .collapsed(true)

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Rabbit Hitman"))
                                        .description(OptionDescription.of(Component.literal("Shows Rabbit Hitman in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowHitman,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowHitman = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Chocolate Factory"))
                                        .description(OptionDescription.of(Component.literal("Shows chocolate factory in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowChocoFactory,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowChocoFactory = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Huntraps"))
                                        .description(OptionDescription.of(Component.literal("Shows Huntraps in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowHuntraps,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowHuntraps = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Dungeons"))
                                        .description(OptionDescription.of(Component.literal("Shows dungeons in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowDungeons,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowDungeons = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Minions"))
                                        .description(OptionDescription.of(Component.literal("Shows minions in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowMinions,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowMinions = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()

                                        .name(Component.literal("Experimentation Table"))
                                        .description(OptionDescription.of(Component.literal("Shows Experimentation Table in the dailies message.")))
                                        .binding(
                                                true,
                                                () -> ModConfig.HANDLER.instance().dailiesShowExpTable,
                                                newVal -> ModConfig.HANDLER.instance().dailiesShowExpTable = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .build()) // dailies - crimson isles group end

                        .build()) // dailies category end

                .build().generateScreen(parent); // mc screen
    }
}
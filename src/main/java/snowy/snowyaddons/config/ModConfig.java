package snowy.snowyaddons.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import snowy.snowyaddons.config.dailies.DailiesNotificationSendMethod;

import java.awt.*;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.fromNamespaceAndPath("snowyaddons", "modconfig"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("snowyaddons.json5"))
                    .setJson5(true)
                    .build())
            .build();


    // ===================== RENDER =====================

    // BAT ESP
    @SerialEntry
    public boolean batEsp = false;
    @SerialEntry
    public Color batEspColor = new Color(255, 165, 0);

    // STAR MOB ESP
    @SerialEntry
    public boolean starMobEsp = false;
    @SerialEntry
    public Color starMobEspColor = new Color(255, 0, 0);

    // PLAYER ESP
    @SerialEntry
    public boolean playerEsp = false;
    @SerialEntry
    public Color playerEspColor = new Color(127, 0, 255);
    @SerialEntry
    public boolean selfRenderPlayerEsp = false;

    // ===================== DAILIES =====================

    // General
    @SerialEntry
    public boolean dailiesNotification = true;
    @SerialEntry
    public DailiesNotificationSendMethod dailiesSendMethod = DailiesNotificationSendMethod.PER_SESSION;
    @SerialEntry
    public int whileUncompletedSleep = 15;
    @SerialEntry
    public boolean dailiesShowCompleted = true;

    // Garden
    @SerialEntry
    public boolean dailiesShowPests = true;
    @SerialEntry
    public boolean dailiesShowGreenhouse = true;

    // Crimson isles
    @SerialEntry
    public boolean dailiesShowMatriarch = false;
    @SerialEntry
    public boolean dailiesShowReputation = true;
    @SerialEntry
    public boolean dailiesShowBottlesFlip = false;

    // Foraging
    @SerialEntry
    public boolean dailiesShowAgatha = true;

    // Misc.
    @SerialEntry
    public boolean dailiesShowHitman = false;
    @SerialEntry
    public boolean dailiesShowChocoFactory = false;
    @SerialEntry
    public boolean dailiesShowHuntraps = false;
    @SerialEntry
    public boolean dailiesShowDungeons = false;
    @SerialEntry
    public boolean dailiesShowMinions = false;
    @SerialEntry
    public boolean dailiesShowExpTable = true;

    // ===================== DUNGEONS =====================
    @SerialEntry
    public boolean m2FireFreeze = true;
    @SerialEntry
    public Color m2FfColor = new Color(255, 0, 0);

    @SerialEntry
    public boolean m2Phase2 = true;
    @SerialEntry
    public Color m2P2Color = new Color(0, 140, 255);

    @SerialEntry
    public boolean m2SenTech = true;
    @SerialEntry
    public Color m2SenTechColor = new Color(0, 215, 255);

    @SerialEntry
    public boolean m2Splits = false;

    @SerialEntry
    public boolean bcHelper = false;
    @SerialEntry
    public Color bcHelperColor = new Color(138, 245, 117);

    // ===================== FUN =====================
    @SerialEntry
    public boolean autoQuake = false;
    @SerialEntry
    public String quakePlayerName = "";

}

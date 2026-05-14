package snowy.snowyaddons.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.io.Serial;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.fromNamespaceAndPath("snowyaddons", "modconfig"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("snowyaddons.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean batEsp = false;
    @SerialEntry
    public Color batEspColor = new Color(255, 165, 0);

    @SerialEntry
    public boolean starMobEsp = false;
    @SerialEntry
    public Color starMobEspColor = new Color(255, 0, 0);

    @SerialEntry
    public boolean playerEsp = false;
    @SerialEntry
    public Color playerEspColor = new Color(127, 0, 255);
    @SerialEntry
    public boolean selfRenderPlayerEsp = false;
}

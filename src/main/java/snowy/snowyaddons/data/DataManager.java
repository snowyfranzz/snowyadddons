package snowy.snowyaddons.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataManager {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("snowyaddons_data.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static DailiesData INSTANCE = new DailiesData();

    public static class DailiesData {
        public boolean dailiesPestsState = false;
        public boolean dailiesGreenhouseState = false;
        public boolean dailiesMatriarchState = false;
        public boolean dailiesReputationState = false;
        public boolean dailiesBottleFlipState = false;
        public boolean dailiesAgathaState = false;
        public boolean dailiesHitmanState = false;
        public boolean dailiesChocoFactoryState = false;
        public boolean dailiesHuntrapsState = false;
        public boolean dailiesDungeonState = false;
        public boolean dailiesMinionsState = false;
        public boolean dailiesExpTableState = false;
    }

    public static void load() {
        if (!Files.exists(PATH)) {
            save();
            return;
        }
        try (var reader = Files.newBufferedReader(PATH)) {
            INSTANCE = GSON.fromJson(reader, DailiesData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (var writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

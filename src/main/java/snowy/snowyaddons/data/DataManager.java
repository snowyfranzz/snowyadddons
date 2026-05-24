package snowy.snowyaddons.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.time.LocalDate;

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

        public LocalDate lastDailyDate = LocalDate.now();

        public static List<Supplier<Boolean>> dailiesGlobalState = List.of(
                () -> DataManager.INSTANCE.dailiesPestsState,
                () -> DataManager.INSTANCE.dailiesGreenhouseState,
                () -> DataManager.INSTANCE.dailiesMatriarchState,
                () -> DataManager.INSTANCE.dailiesReputationState,
                () -> DataManager.INSTANCE.dailiesBottleFlipState,
                () -> DataManager.INSTANCE.dailiesAgathaState,
                () -> DataManager.INSTANCE.dailiesHitmanState,
                () -> DataManager.INSTANCE.dailiesChocoFactoryState,
                () -> DataManager.INSTANCE.dailiesHuntrapsState,
                () -> DataManager.INSTANCE.dailiesDungeonState,
                () -> DataManager.INSTANCE.dailiesMinionsState,
                () -> DataManager.INSTANCE.dailiesExpTableState
        );
    }

    // initial check if the mod should send the notification. this is expanded on for each mode on DailiesManager.java
    public static boolean notificationState(){
        boolean flagIncomplete = false;

        for(int i = 0; i < DataManager.DailiesData.dailiesGlobalState.size(); i++){
            if(!DataManager.DailiesData.dailiesGlobalState.get(i).get()){
                flagIncomplete = true;
            }
        }

        if(flagIncomplete){
            return true;
        } else {
            return false;
        }
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
        DataManager.INSTANCE.lastDailyDate = LocalDate.now();

        try (var writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

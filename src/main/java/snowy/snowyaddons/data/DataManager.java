package snowy.snowyaddons.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.config.ModConfig;

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

    public static class JsonDate {
        public int year;
        public int month;
        public int day;

        public JsonDate() {
            LocalDate now = LocalDate.now();
            this.year = now.getYear();
            this.month = now.getMonthValue();
            this.day = now.getDayOfMonth();
        }

        public JsonDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    public static class DailiesData {
        public boolean dailiesPestsState = false;
        public boolean dailiesGreenhouseState = false;
        public boolean dailiesVisitorState = false;
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

        public JsonDate lastDailyDate = new JsonDate();
        public JsonDate lastLoginDate = new JsonDate();

        public static List<Supplier<Boolean>> dailiesGlobalState = List.of(
                () -> DataManager.INSTANCE.dailiesPestsState,
                () -> DataManager.INSTANCE.dailiesGreenhouseState,
                () -> DataManager.INSTANCE.dailiesVisitorState,
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

        public record DailyTracker(boolean isCompleted, boolean isEnabled) {}

        public static List<DailyTracker> getTrackedDailies() {
            DailiesData data = DataManager.INSTANCE;
            ModConfig config = ModConfig.HANDLER.instance();

            return List.of(
                    new DailyTracker(data.dailiesPestsState, config.dailiesShowPests),
                    new DailyTracker(data.dailiesGreenhouseState, config.dailiesShowGreenhouse),
                    new DailyTracker(data.dailiesVisitorState, config.dailiesShowVisitors),
                    new DailyTracker(data.dailiesMatriarchState, config.dailiesShowMatriarch),
                    new DailyTracker(data.dailiesReputationState, config.dailiesShowReputation),
                    new DailyTracker(data.dailiesBottleFlipState, config.dailiesShowBottlesFlip),
                    new DailyTracker(data.dailiesAgathaState, config.dailiesShowAgatha),
                    new DailyTracker(data.dailiesHitmanState, config.dailiesShowHitman),
                    new DailyTracker(data.dailiesChocoFactoryState, config.dailiesShowChocoFactory),
                    new DailyTracker(data.dailiesHuntrapsState, config.dailiesShowHuntraps),
                    new DailyTracker(data.dailiesDungeonState, config.dailiesShowDungeons),
                    new DailyTracker(data.dailiesMinionsState, config.dailiesShowMinions),
                    new DailyTracker(data.dailiesExpTableState, config.dailiesShowExpTable)
            );
        }
    }

    // initial check if the mod should send the notification. this is expanded on for each mode on DailiesManager.java
    public static boolean notificationState(){
        return DailiesData.getTrackedDailies().stream()
                .filter(DailiesData.DailyTracker::isEnabled)
                .anyMatch(tracker -> !tracker.isCompleted());
    }

    public static void resetDailies(){
        JsonDate saved = INSTANCE.lastDailyDate;

        if (saved.year == 0 || saved.month == 0 || saved.day == 0) {
            INSTANCE.lastDailyDate = new JsonDate();
            return;
        }

        LocalDate savedDate = LocalDate.of(saved.year, saved.month, saved.day);

        LocalDate today = LocalDate.now();

        if (savedDate.isBefore(today)) {

            INSTANCE.dailiesPestsState = false;
            INSTANCE.dailiesGreenhouseState = false;
            INSTANCE.dailiesVisitorState = false;
            INSTANCE.dailiesMatriarchState = false;
            INSTANCE.dailiesReputationState = false;
            INSTANCE.dailiesBottleFlipState = false;
            INSTANCE.dailiesAgathaState = false;
            INSTANCE.dailiesHitmanState = false;
            INSTANCE.dailiesChocoFactoryState = false;
            INSTANCE.dailiesHuntrapsState = false;
            INSTANCE.dailiesDungeonState = false;
            INSTANCE.dailiesMinionsState = false;
            INSTANCE.dailiesExpTableState = false;

            save();
        }
    }

    public static void load() {

        INSTANCE.lastLoginDate = new JsonDate();

        if (!Files.exists(PATH)) {
            save();
            return;
        }
        try (var reader = Files.newBufferedReader(PATH)) {
            INSTANCE = GSON.fromJson(reader, DailiesData.class);

            resetDailies();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {

        if (!notificationState()){ // if all dailies are complete it saves a new date of oh all dailies are complete im so cool
            INSTANCE.lastDailyDate = new JsonDate();
        }

        try (var writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package snowy.snowyaddons.client.utils;

/**
 * Every island/zone the jukebox can attach a playlist to. Matched against the scoreboard
 * "Area:" line (see {@link GetServerInfo#getCurrentIsland()}) via simple substring aliases,
 * since Hypixel doesn't expose a proper API for this.
 */
public enum SkyblockIsland {
    PRIVATE_ISLAND("private_island", "Private Island", "YOUR ISLAND", "'S ISLAND"),
    HUB("hub", "Hub", "HUB"),
    THE_FARMING_ISLANDS("farming_islands", "The Farming Islands", "THE FARMING ISLANDS", "THE BARN", "MUSHROOM DESERT"),
    GARDEN("garden", "The Garden", "GARDEN"),
    THE_PARK("the_park", "The Park", "THE PARK", "SPOOKY FESTIVAL"),
    SPIDERS_DEN("spiders_den", "Spider's Den", "SPIDER'S DEN"),
    THE_END("the_end", "The End", "THE END"),
    CRYSTAL_HOLLOWS("crystal_hollows", "Crystal Hollows", "CRYSTAL HOLLOWS"),
    DWARVEN_MINES("dwarven_mines", "Dwarven Mines", "DWARVEN MINES"),
    DEEP_CAVERNS("deep_caverns", "Deep Caverns", "DEEP CAVERNS"),
    GOLD_MINE("gold_mine", "Gold Mine", "GOLD MINE"),
    DUNGEON_HUB("dungeon_hub", "Dungeon Hub", "DUNGEON HUB"),
    THE_CATACOMBS("the_catacombs", "The Catacombs", "THE CATACOMBS", "CATACOMBS ENTRANCE"),
    CRIMSON_ISLE("crimson_isle", "Crimson Isle", "CRIMSON ISLE"),
    KUUDRA_HOLLOW("kuudra_hollow", "Kuudra's Hollow", "KUUDRA'S HOLLOW", "KUUDRA"),
    BLAZING_FORTRESS("blazing_fortress", "Blazing Fortress", "BLAZING FORTRESS"),
    BACKWATER_BAYOU("backwater_bayou", "Backwater Bayou", "BACKWATER BAYOU"),
    THE_RIFT("the_rift", "The Rift", "THE RIFT"),
    WINTER_ISLAND("winter_island", "Jerry's Workshop", "JERRY'S WORKSHOP", "WINTER ISLAND"),
    UNKNOWN("unknown", "Unknown");

    private final String id;
    private final String displayName;
    private final String[] areaAliases;

    SkyblockIsland(String id, String displayName, String... areaAliases) {
        this.id = id;
        this.displayName = displayName;
        this.areaAliases = areaAliases;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    private boolean matchesAreaLine(String cleanUppercaseLine) {
        for (String alias : areaAliases) {
            if (cleanUppercaseLine.contains(alias)) return true;
        }
        return false;
    }

    public static SkyblockIsland fromId(String id) {
        if (id == null) return null;
        for (SkyblockIsland island : values()) {
            if (island != UNKNOWN && island.id.equalsIgnoreCase(id)) return island;
        }
        return null;
    }

    public static SkyblockIsland fromAreaLine(String cleanUppercaseAreaText) {
        for (SkyblockIsland island : values()) {
            if (island != UNKNOWN && island.matchesAreaLine(cleanUppercaseAreaText)) return island;
        }
        return UNKNOWN;
    }
}

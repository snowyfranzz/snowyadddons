# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

SnowyAddons is a Fabric mod for Minecraft 26.2 (Hypixel Skyblock) built with Java 25 and Fabric Loom. It adds QOL modules: dungeon timers/HUDs, entity ESP (via mixin), a daily-tasks tracker with chat notifications, and a per-island music jukebox. It depends on YACL (config screen library) and optionally integrates with Mod Menu.

## Commands

- Build: `./gradlew build`
- Run the client (launches a dev Minecraft instance with the mod loaded): `./gradlew runClient`
- Run the server: `./gradlew runServer`
- Clean: `./gradlew clean`

There is no test suite in this repo (no `src/test`) and no lint task configured — `./gradlew build` is the primary correctness check (compiles both source sets and validates mixins/resources).

`gradle-wrapper.jar` is gitignored by the blanket `*.jar` rule with a carved-out exception, but older checkouts may still be missing it (`Unable to access jarfile ... gradle-wrapper.jar`) — regenerate it or add the exception rather than removing `./gradlew`'s reliance on it.

Mod version, Minecraft version, and dependency versions are all set in `gradle.properties`; bump `mod_version` there when cutting a release. `fabric.mod.json` version is templated from `project.version` at build time via `processResources`, so don't hand-edit the version in `fabric.mod.json`.

## Architecture

### Two source sets: `main` vs `client`

Fabric Loom's `splitEnvironmentSourceSets()` splits code into `src/main` (common/server-safe code) and `src/client` (client-only code), both under the `snowyaddons` mod. In practice almost all logic lives in `src/client` since this is a client-side QOL mod:

- `src/main/java/snowy/snowyaddons/SnowyAddons.java` — common `ModInitializer` entrypoint. Loads `ModConfig` and `DataManager` on init.
- `src/main/java/snowy/snowyaddons/config/ModConfig.java` — the YACL-backed config object (all settings as `@SerialEntry` fields), persisted to `snowyaddons.json5` in the game config dir.
- `src/main/java/snowy/snowyaddons/data/DataManager.java` — persistent state unrelated to user settings (daily-task completion flags, per-island jukebox playlists), persisted to `snowyaddons_data.json`. Resets daily state when the saved date is before today.
- `src/client/java/snowy/snowyaddons/client/SnowyAddonsClient.java` — client entrypoint. Instantiates every module as a static field on this class ("entire mod reads the exact same instances" — see the comment there), registers commands/chat listener, and drives all modules from a single `ClientTickEvents.END_CLIENT_TICK` callback gated by `GetServerInfo.isInSkyBlock()` / `isInDungeon()` and the corresponding `ModConfig` toggle.

### Module pattern

Each feature under `src/client/.../client/modules/{dungeons,fun,dailies,jukebox}/` follows the same shape: a plain class (not a Fabric-registered singleton) with a `registerEvents()` method called once from `SnowyAddonsClient.onInitializeClient()`, and typically an `onTick()` called every client tick from the central loop when both `isInSkyBlock()`/`isInDungeon()` and the module's `ModConfig` flag are true. HUD-drawing modules also register a `HudElementRegistry` callback (e.g. `M2FireFreezeTimer.onHudRender`) using a namespaced `Identifier`. To add a new module: create the class, wire it into `SnowyAddonsClient` (static field + `registerEvents()` call + tick gate), add its toggle/settings to `ModConfig`, add it to `ModCommandRegister`'s `CONFIG_TOGGLES` map (so `/snowy toggle <module>` works — easy to forget, see Jukebox system notes below), and add a config-screen entry in `ModConfigScreen`.

### Game-state detection is scoreboard scraping

There's no Skyblock API — `GetServerInfo` (`client/utils/GetServerInfo.java`) detects Skyblock/dungeon state by reading the sidebar scoreboard title/lines and stripping color codes with a regex. Modules that need to know "am I in a dungeon / on M2 / on which island" go through this class rather than re-parsing the scoreboard themselves. `getCurrentIsland()` looks for a line containing `"AREA:"` and matches the remainder against `SkyblockIsland`'s aliases; `getSidebarLines()` returns the raw (color-stripped, not uppercased) lines for troubleshooting scoreboard-parsing bugs — see `/snowy jukebox debug`.

### Chat-driven triggers

`ChatListener` (`client/utils/listeners/ChatListener.java`) is a single Fabric chat/game-message event registration that fans out to subscribers via `ChatListener.subscribe(Consumer<String>)`. Modules subscribe in their `registerEvents()` and match on specific (uppercased, color-code-stripped) substrings of boss/system messages to trigger timers (see `M2FireFreezeTimer` matching on the Scarf boss line). When adding a new chat-triggered module, subscribe here rather than adding a new event registration.

### ESP rendering via mixin, not per-entity code

Entity glow/ESP (bat, star mobs, players) is implemented by `EntityGlowMixin` injecting into `Entity#isCurrentlyGlowing` and `Entity#getTeamColor`, reading `ModConfig` directly rather than going through a module class. Star-mob detection caches its result for 500ms (`snowy$lastStarCheckTime`) since it does a name/passenger/nearby-armor-stand search. Mixins are registered per source set in `snowyaddons.mixins.json` (main) and `snowyaddons.client.mixins.json` (client) — a new mixin class must be added to the relevant JSON's `mixins`/`client` list or it won't load.

### Commands

All client commands are registered in one place: `client/utils/command/ModCommandRegister.java`, under the `/snowy` root (`config`, `help`, `version`, `boop`, `toggle <module>`, `dailies [<option>]`, `jukebox <...>`). Module toggles and daily toggles are declared as static maps (`CONFIG_TOGGLES`, `DAILY_TOGGLES`) of string key → getter/setter pair against `ModConfig`/`DataManager`, used both for execution and Brigadier tab-completion (`suggestFromMap`). Add new toggles here rather than writing new command branches — the jukebox module initially shipped without a `CONFIG_TOGGLES` entry, which silently broke `/snowy toggle jukebox`; don't repeat that omission for future modules.

### Dailies system

Three files cooperate and must be kept in sync when adding a new tracked daily:
- `ModConfig` — `dailiesShow*` boolean (whether the daily is tracked/shown) plus category grouping.
- `DataManager.DailiesData` — `dailies*State` boolean (today's completion state), included in the `dailiesGlobalState`/`getTrackedDailies()` lists.
- `DailiesManager` — parallel `dailiesToggled`, `dailiesCompletionState`, and `dailiesNames` lists (index-aligned across all three) used to build the chat listing and drive notifications (`dailiesListBuilder`, `sendDailiesNotification`, three send modes: `PER_SESSION` / `ONCE_PER_DAY` / `WHILE_UNCOMPLETED` from `DailiesNotificationSendMethod`).
- `ModCommandRegister`'s `DAILY_TOGGLES` map for the `/snowy dailies <option>` command.

### Jukebox system

Lets users drop their own `.wav` files into `config/snowyaddons/jukebox_audio/` and build a playlist per Skyblock island, which auto-plays a few seconds after arriving on that island. Several files cooperate:

- `client/utils/SkyblockIsland.java` — enum of every island the jukebox can target, matched against the scoreboard's `Area:` line via substring aliases (`fromAreaLine`).
- `client/utils/GetServerInfo.java` — `getCurrentIsland()` (scoreboard-based island detection) and `getSidebarLines()` (raw debug dump).
- `client/utils/audio/JukeboxPlayer.java` / `JukeboxAudioLibrary.java` — WAV-only playback via `javax.sound.sampled` (no bundled ogg/mp3 decoder, so only `.wav` is supported) and folder scanning.
- `client/modules/jukebox/JukeboxManager.java` — the module: tracks island changes, runs the join-delay countdown, drives the playback queue, and shows a `SystemToast` ("Now Playing:" / filename minus extension) on track change.
- `DataManager.DailiesData.jukeboxPlaylists` — `Map<String, List<String>>` of island id → ordered filenames, persisted alongside the dailies data (despite the class name — there's only one data blob in this mod).
- `ModCommandRegister` — `/snowy jukebox <play|pause|skip|status|debug|islands|files|folder|playlist|add|remove>`.

**Known-tricky bit**: `JukeboxManager.onTick()` must ignore transient `SkyblockIsland.UNKNOWN` reads from `getCurrentIsland()` rather than treating them as "the player left the island" — the scoreboard's Area line is genuinely unpopulated for a tick or two right after joining, and treating every blip as a departure resets the join-delay countdown, which can prevent it from ever completing. Only a stable *concrete* island change should reset the queue; leaving Skyblock entirely is handled separately via `isInSkyBlock()`.

**Status as of the last session**: implemented and compiling, with two real bugs found and fixed (the `UNKNOWN`-blip reset above, and a missing `CONFIG_TOGGLES` entry for `/snowy toggle jukebox`). The `Area:` scoreboard alias matching in `SkyblockIsland`/`GetServerInfo.getCurrentIsland()` has **not** been confirmed against a live Hypixel client — it was written from general knowledge of Hypixel's scoreboard format, not verified output. If playback still doesn't start after those fixes, use `/snowy jukebox debug` to see the actual raw sidebar text Hypixel sends and adjust the aliases accordingly. Separately, the mod's `ModConfigScreen.java` was reworked to nest the Jukebox settings as a collapsed group inside the "Fun" category instead of its own top-level category, and `fabric.mod.json` gained `"custom": {"firmament:hide_from_modlist": true}`.

### Config screen

`client/config/ModConfigScreen.java` builds the YACL screen (grouped categories mirroring the `ModConfig` sections); `client/config/ModMenuIntegration.java` exposes it to Mod Menu. Any new `ModConfig` field intended to be user-facing needs a corresponding entry added there.

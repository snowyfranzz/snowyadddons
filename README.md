# ❄️ SnowyAddons
<img src='https://img.shields.io/github/downloads/snowyfranzz/snowyaddons/total'> <img src='https://img.shields.io/badge/release-1.1.0-green'>


SnowyAddons is a mod for Hypixel Skyblock that adds some useful QOL modules to enhance your experience!

> [!CAUTION]
> *Please note that you are under the risk of getting banned EVEN with it being "unbannable". *Use at your own risk.**

## 🔗 Dependencies

SnowyAddons directly depends on **YACL** for its config system. It is also recommended to use **Mod Menu** to easily access the settings menu through the "Mods" button.

## 📜 Modules and Features

### ☠️ Dungeons

| Module                   | Description                                                                  |
|:-------------------------|:-----------------------------------------------------------------------------|
| **Blood Camp Helper**    | Counts down and shows a title for when you should kill mobs to dialogue skip |
| **M2 SenTech© Timer**    | Shows a timer on screen for when to ice-spray first phase undeads            |
| **M2 Fire Freeze Timer** | Shows a timer on screen for when to fire-freeze on m2                        |
| **M2 Phase 2 Timer**     | Shows a timer on screen for when M2's second phase starts                    |

### 🔎 Render

| Module | Description |
| :--- | :--- |
| **Bat ESP** | Renders a glow around secret bats that is visible through walls. |
| **Star Mob ESP** | Renders a glow around star mobs that is visible through walls. |
| **Player ESP** | Renders a glow around players that is visible through walls. |

### 🗓️ Dailies

| Category | Tracked Tasks                                                                        |
| :--- |:-------------------------------------------------------------------------------------|
| **Farming** | Pests, Greenhouse                                                                    |
| **Crimson Isles** | Matriarch, Reputation, XP Bottles Flip                                               |
| **Galatea** | Agatha's Contests                                                                    |
| **Other** | Rabbit Hitman, Chocolate Factory, Huntraps, Dungeons, Minions, Experimentation Table |

### 🥳 Fun
| Module              | Description                                                                    |
|:--------------------|:-------------------------------------------------------------------------------|
| **Auto Quakecraft** | Automatically sends a quakecraft duel request to someone when there's downtime |

### 🎵 Jukebox
| Module      | Description                                                                                                                                          |
|:------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| **Jukebox** | Plays your own `.wav` files as a per-island playlist, starting a few seconds after you arrive on that island. Fully controllable via chat commands. |


## ⚙️ Commands
`/snowy` → Opens the mod screen.

`/snowy help` → Sends a message in chat with all commands.

`/snowy config` → Opens the config screen.

`/snowy version` → Sends the version in chat.

`/snowy boop` → Replies with a "Boop!"

`/snowy toggle <module>` → Enables / Disables a module.

`/snowy dailies` → Shows dailies list.

`/snowy dailies <daily>` → Toggles the daily between completed or uncompleted.

`/snowy jukebox play` → Resumes the currently paused track.

`/snowy jukebox pause` → Pauses the currently playing track.

`/snowy jukebox skip` → Skips to the next track in the current island's playlist.

`/snowy jukebox status` → Shows what's currently playing and on which island.

`/snowy jukebox debug` → Shows raw island-detection info (useful if playback isn't starting).

`/snowy jukebox islands` → Lists every island you can build a playlist for.

`/snowy jukebox files` → Lists the `.wav` files sitting in your jukebox audio folder.

`/snowy jukebox folder` → Opens the jukebox audio folder in your file explorer, so you can drop `.wav` files into it.

`/snowy jukebox playlist <island>` → Shows the playlist for an island.

`/snowy jukebox add <island> <file>` → Adds a `.wav` file to an island's playlist.

`/snowy jukebox remove <island> <file>` → Removes a `.wav` file from an island's playlist.

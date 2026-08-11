package snowy.snowyaddons.client.modules.jukebox;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import snowy.snowyaddons.client.utils.GetServerInfo;
import snowy.snowyaddons.client.utils.SkyblockIsland;
import snowy.snowyaddons.client.utils.audio.JukeboxAudioLibrary;
import snowy.snowyaddons.client.utils.audio.JukeboxPlayer;
import snowy.snowyaddons.config.ModConfig;
import snowy.snowyaddons.data.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Drives per-island playlist playback. Island changes are detected each tick via
 * GetServerInfo.getCurrentIsland(); once the island has been stable for the configured delay,
 * that island's playlist starts. Track completion is signalled off the audio thread by
 * JukeboxPlayer, so it's picked up here (client thread) via a polled flag rather than acted on
 * directly from the callback.
 */
public class JukeboxManager {
    private final JukeboxPlayer player = new JukeboxPlayer();
    private final AtomicBoolean trackFinished = new AtomicBoolean(false);

    private SkyblockIsland currentIsland = null;
    private SkyblockIsland pendingIsland = null;
    private int joinDelayTicks = 0;

    private List<String> currentQueue = new ArrayList<>();
    private int queueIndex = -1;
    private String currentTrackName = null;

    public void registerEvents() {
        JukeboxAudioLibrary.ensureDirExists();
        player.setOnFinished(() -> trackFinished.set(true));
    }

    public void onTick() {
        ModConfig config = ModConfig.HANDLER.instance();

        if (!config.jukeboxEnabled || !GetServerInfo.isInSkyBlock()) {
            if (player.isActive()) stopPlayback();
            currentIsland = null;
            pendingIsland = null;
            return;
        }

        if (player.isActive()) {
            player.setVolume(config.jukeboxVolume);
        }

        SkyblockIsland island = GetServerInfo.getCurrentIsland();

        if (island != currentIsland) {
            currentIsland = island;
            stopPlayback();
            pendingIsland = island;
            joinDelayTicks = Math.max(0, config.jukeboxStartDelaySeconds) * 20;
        }

        if (pendingIsland != null) {
            if (joinDelayTicks > 0) {
                joinDelayTicks--;
            } else {
                startIslandPlaylist(pendingIsland);
                pendingIsland = null;
            }
        }

        if (trackFinished.compareAndSet(true, false)) {
            advanceTrack();
        }
    }

    private void startIslandPlaylist(SkyblockIsland island) {
        Map<String, List<String>> playlists = DataManager.INSTANCE.jukeboxPlaylists;
        List<String> playlist = playlists != null ? playlists.get(island.getId()) : null;
        if (playlist == null || playlist.isEmpty()) return;

        currentQueue = new ArrayList<>(playlist);
        if (ModConfig.HANDLER.instance().jukeboxShuffle) Collections.shuffle(currentQueue);
        queueIndex = -1;
        advanceTrack();
    }

    private void advanceTrack() {
        if (currentQueue.isEmpty()) return;

        queueIndex++;
        if (queueIndex >= currentQueue.size()) {
            queueIndex = 0;
            if (ModConfig.HANDLER.instance().jukeboxShuffle) Collections.shuffle(currentQueue);
        }

        playFile(currentQueue.get(queueIndex));
    }

    private void playFile(String filename) {
        try {
            player.play(JukeboxAudioLibrary.getAudioDir().resolve(filename), ModConfig.HANDLER.instance().jukeboxVolume);
            currentTrackName = filename;

            if (ModConfig.HANDLER.instance().jukeboxAnnounceTrack) {
                announce("Now playing: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentQueue.remove(queueIndex);
            queueIndex--;
            advanceTrack();
        }
    }

    private void announce(String text) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.player.sendOverlayMessage(Component.literal("♪ " + text).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    private void stopPlayback() {
        player.stop();
        currentQueue = new ArrayList<>();
        queueIndex = -1;
        currentTrackName = null;
    }

    public void play() {
        player.resume();
    }

    public void pause() {
        player.pause();
    }

    public void skip() {
        if (currentQueue.isEmpty()) return;
        advanceTrack();
    }

    public Component getStatusMessage() {
        ModConfig config = ModConfig.HANDLER.instance();

        if (!config.jukeboxEnabled) {
            return Component.literal("[SnowyAddons] Jukebox is disabled. Enable it in /snowy config.").withStyle(ChatFormatting.DARK_RED);
        }

        String islandName = currentIsland != null ? currentIsland.getDisplayName() : "Unknown";

        if (currentTrackName == null) {
            return Component.literal("[SnowyAddons] Jukebox is idle on " + islandName + " (no playlist, or waiting to start).").withStyle(ChatFormatting.GRAY);
        }

        String state = player.isPaused() ? "Paused" : "Playing";
        return Component.literal("[SnowyAddons] " + state + ": " + currentTrackName + " (" + islandName + ")").withStyle(ChatFormatting.AQUA);
    }
}

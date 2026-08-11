package snowy.snowyaddons.client.utils.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Thin wrapper around a single javax.sound.sampled Clip. suppressFinishEvent distinguishes a
 * clip that reached its natural end (should auto-advance the playlist) from one we stopped
 * ourselves via pause()/stop() (both fire the same LineEvent.Type.STOP).
 */
public class JukeboxPlayer {
    private Clip clip;
    private volatile boolean paused = false;
    private volatile boolean suppressFinishEvent = false;
    private Runnable onFinished;

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public synchronized void play(Path file, int volumePercent) throws IOException, javax.sound.sampled.UnsupportedAudioFileException, javax.sound.sampled.LineUnavailableException {
        stop();

        try (AudioInputStream in = AudioSystem.getAudioInputStream(file.toFile())) {
            clip = AudioSystem.getClip();
            clip.open(in);
        }

        applyVolume(volumePercent);

        clip.addLineListener(event -> {
            if (event.getType() != LineEvent.Type.STOP) return;

            if (suppressFinishEvent) {
                suppressFinishEvent = false;
                return;
            }

            if (onFinished != null) onFinished.run();
        });

        clip.start();
        paused = false;
    }

    public synchronized void pause() {
        if (clip != null && clip.isRunning()) {
            suppressFinishEvent = true;
            clip.stop();
            paused = true;
        }
    }

    public synchronized void resume() {
        if (clip != null && paused) {
            clip.start();
            paused = false;
        }
    }

    public synchronized void stop() {
        if (clip != null) {
            suppressFinishEvent = true;
            clip.stop();
            clip.close();
            clip = null;
        }
        paused = false;
    }

    public synchronized void setVolume(int volumePercent) {
        applyVolume(volumePercent);
    }

    private void applyVolume(int volumePercent) {
        if (clip == null) return;
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float clamped = Math.max(0.0001f, Math.min(1f, volumePercent / 100f));
        float dB = (float) (Math.log10(clamped) * 20.0);
        dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
        gain.setValue(dB);
    }

    public synchronized boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized boolean isActive() {
        return clip != null;
    }
}

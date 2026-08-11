package snowy.snowyaddons.client.utils.audio;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Scans the user-managed audio folder inside the mod's config directory. Only WAV files are
 * supported since playback goes through javax.sound.sampled (no bundled decoders for mp3/ogg).
 */
public class JukeboxAudioLibrary {
    private static final Path AUDIO_DIR = FabricLoader.getInstance().getConfigDir()
            .resolve("snowyaddons").resolve("jukebox_audio");

    public static Path getAudioDir() {
        return AUDIO_DIR;
    }

    public static void ensureDirExists() {
        try {
            Files.createDirectories(AUDIO_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> listAudioFiles() {
        ensureDirExists();
        List<String> files = new ArrayList<>();
        try (Stream<Path> stream = Files.list(AUDIO_DIR)) {
            stream.filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.toLowerCase().endsWith(".wav"))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .forEach(files::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}

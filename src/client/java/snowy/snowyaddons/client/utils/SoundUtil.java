package snowy.snowyaddons.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;

public class SoundUtil {

    public static void playSoundCatMeow() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSoundManager() == null) return;

        SimpleSoundInstance sound = SimpleSoundInstance.forUI(
                SoundEvents.CAT_SOUNDS
                        .get(CatSoundVariants.SoundSet.CLASSIC)
                        .adultSounds()
                        .ambientSound()
                        .value(),
                1.0F,  // Pitch
                1.0F   // Volume
        );

        mc.execute(() -> mc.getSoundManager().play(sound));
    }
}

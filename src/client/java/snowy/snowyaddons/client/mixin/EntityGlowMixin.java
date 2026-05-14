package snowy.snowyaddons.client.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snowy.snowyaddons.config.ModConfig;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        Object thisEntity = (Object) (Object) this;
        ModConfig config = ModConfig.HANDLER.instance();

        if (thisEntity instanceof Bat) {
            // Now we force the glow to match the toggle (True if ON, False if OFF)
            cir.setReturnValue(config.batEsp);
            return;
        }

        if (thisEntity instanceof Enemy) {
            cir.setReturnValue(config.starMobEsp);
            return;
        }

        if (config.selfRenderPlayerEsp) { // check for self renderer. skips LocalPlayer check
            if (thisEntity instanceof Player) {
                cir.setReturnValue(config.playerEsp);
                return;
            }

        } else { // self renderer disabled -> doesn't render LocalPlayer
            if (thisEntity instanceof Player && !(thisEntity instanceof net.minecraft.client.player.LocalPlayer)) {
                cir.setReturnValue(config.playerEsp);
                return;
            }
        }

    }

    private int getDecimal(java.awt.Color c) {
        return (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void onGetTeamColor(CallbackInfoReturnable<Integer> cir) {
        if (ModConfig.HANDLER == null) return;

        Object thisEntity = (Object) (Object) this;
        ModConfig config = ModConfig.HANDLER.instance();

        if (config.batEsp && thisEntity instanceof Bat) {
            cir.setReturnValue(getDecimal(config.batEspColor));
            return;
        }
        if (config.starMobEsp && thisEntity instanceof Enemy){
            cir.setReturnValue(getDecimal(config.starMobEspColor));
            return;
        }

        if (config.selfRenderPlayerEsp) { // check for self renderer. if true skips LocalPlayer check
            if (config.playerEsp && thisEntity instanceof Player) {
                cir.setReturnValue(getDecimal(config.playerEspColor));
                return;
            }

        } else { // self renderer disabled -> doesn't render LocalPlayer
            if (config.playerEsp && thisEntity instanceof Player && !(thisEntity instanceof net.minecraft.client.player.LocalPlayer)) {
                cir.setReturnValue(getDecimal(config.playerEspColor));
                return;
            }
        }
    }
}

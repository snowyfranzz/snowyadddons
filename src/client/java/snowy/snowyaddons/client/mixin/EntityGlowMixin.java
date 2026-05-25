package snowy.snowyaddons.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snowy.snowyaddons.config.ModConfig;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityGlowMixin {

    @Shadow public abstract @Nullable Component getCustomName();
    @Shadow public abstract java.util.List<Entity> getPassengers();
    @Shadow public abstract net.minecraft.world.level.Level level();
    @Shadow public abstract AABB getBoundingBox();

    // CACHE VARIABLES
    @Unique private boolean snowy$isStarredCache = false;
    @Unique private long snowy$lastStarCheckTime = 0;

    @Unique
    private boolean snowy$isStarredMob() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - snowy$lastStarCheckTime > 500) {
            snowy$lastStarCheckTime = currentTime;
            snowy$isStarredCache = false;

            // direct name check (fallback)
            Component directName = this.getCustomName();
            if (directName != null && directName.getString().contains("✯")) {
                snowy$isStarredCache = true;
                return true;
            }

            // passenger check
            for (Entity passenger : this.getPassengers()) {
                Component passengerName = passenger.getCustomName();
                if (passengerName != null && passengerName.getString().contains("✯")) {
                    snowy$isStarredCache = true;
                    return true;
                }
            }

            // spatial search
            if (this.level() != null) {
                AABB searchBox = this.getBoundingBox().inflate(0.5, 2.5, 0.5);

                List<ArmorStand> nearbyStands = this.level().getEntitiesOfClass(ArmorStand.class, searchBox);
                for (ArmorStand stand : nearbyStands) {
                    Component standName = stand.getCustomName();
                    if (standName != null && standName.getString().contains("✯")) {
                        snowy$isStarredCache = true;
                        break;
                    }
                }
            }
        }
        return snowy$isStarredCache;
    }

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity thisEntity = (Entity) (Object) this;
        ModConfig config = ModConfig.HANDLER.instance();

        if (thisEntity instanceof Bat) {
            cir.setReturnValue(config.batEsp);
            return;
        }

        if (thisEntity instanceof Enemy || thisEntity instanceof Player) {
            if (snowy$isStarredMob() && config.starMobEsp) {
                cir.setReturnValue(true);
                return;
            }
        }

        if (thisEntity instanceof Player) {
            boolean isLocal = thisEntity instanceof LocalPlayer;
            if (config.selfRenderPlayerEsp || !isLocal) {
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

        Entity thisEntity = (Entity) (Object) this;
        ModConfig config = ModConfig.HANDLER.instance();

        if (config.batEsp && thisEntity instanceof Bat) {
            cir.setReturnValue(getDecimal(config.batEspColor));
            return;
        }

        if (config.starMobEsp && (thisEntity instanceof Enemy || thisEntity instanceof Player)) {
            if (snowy$isStarredMob()) {
                cir.setReturnValue(getDecimal(config.starMobEspColor));
                return;
            }
        }

        if (config.playerEsp && thisEntity instanceof Player) {
            boolean isLocal = thisEntity instanceof LocalPlayer;
            if (config.selfRenderPlayerEsp || !isLocal) {
                cir.setReturnValue(getDecimal(config.playerEspColor));
            }
        }
    }
}

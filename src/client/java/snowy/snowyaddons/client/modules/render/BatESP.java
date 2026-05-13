package snowy.snowyaddons.client.modules.render;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import snowy.snowyaddons.config.ModConfig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class BatESP {
    public static void renderBox(PoseStack poseStack, Entity entity, float partialTicks) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        Camera camera = client.gameRenderer.getMainCamera();
        Vec3 camPos = camera.position();
        double x = Mth.lerp(partialTicks, entity.xOld, entity.getX()) - camPos.x;
        double y = Mth.lerp(partialTicks, entity.yOld, entity.getY()) - camPos.y;
        double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ()) - camPos.z;

        AABB box = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ()).move(x, y, z);

        var source = client.renderBuffers().bufferSource();
        VertexConsumer buffer = source.getBuffer(RenderTypes.lines());

        ShapeRenderer.renderShape(
                poseStack,
                buffer,
                Shapes.create(box),
                0.0, 0.0, 0.0,
                0xFFFFA500,
                1.0f
        );
    }

    public void onRender(WorldRenderContext context) {
        if (!ModConfig.HANDLER.instance().batEsp) return;

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        float partialTicks = client.gameRenderer.getMainCamera().getPartialTickTime();

        for (Entity entity : client.level.entitiesForRendering()) {
            if (entity instanceof Bat bat) {
                renderBox(context.matrices(), bat, partialTicks);
            }
        }

        client.renderBuffers().bufferSource().endBatch(RenderTypes.lines());
    }
}

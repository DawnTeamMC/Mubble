package fr.hugman.mubble.client.renderer.entity;

import fr.hugman.mubble.client.renderer.entity.state.CartoonPose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called every frame for every living entity about to be drawn. Listeners add their squash, stretch,
 * spin and lean to the pose, which starts out neutral and is applied on top of the entity's own rotations.
 */
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface CartoonPoseCallback {
    Event<CartoonPoseCallback> EVENT = EventFactory.createArrayBacked(CartoonPoseCallback.class, callbacks -> (entity, state, partialTicks, pose) -> {
        for (var callback : callbacks) {
            callback.pose(entity, state, partialTicks, pose);
        }
    });

    void pose(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CartoonPose pose);
}

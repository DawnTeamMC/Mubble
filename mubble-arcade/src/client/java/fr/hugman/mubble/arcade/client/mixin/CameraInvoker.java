package fr.hugman.mubble.arcade.client.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraInvoker {
    @Invoker("setRotation")
    void mubble$setRotation(float yRot, float xRot);

    @Invoker("setPosition")
    void mubble$setPosition(Vec3 position);

    @Invoker("move")
    void mubble$move(float forwards, float up, float right);

    @Accessor("eyeHeight")
    float mubble$getEyeHeight();

    @Accessor("eyeHeightOld")
    float mubble$getEyeHeightOld();
}

package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.arcade.camera.ArcadeCamera;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class ArcadeCameraMixin {
    @Inject(method = "alignWithEntity", at = @At("TAIL"))
    private void mubble$orbit(float partialTicks, CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            ArcadeCamera.frame((Camera) (Object) this, player, partialTicks);
        }
    }
}

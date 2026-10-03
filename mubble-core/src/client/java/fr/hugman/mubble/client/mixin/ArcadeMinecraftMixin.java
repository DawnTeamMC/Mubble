package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.arcade.ArcadeClientInput;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ArcadeMinecraftMixin {
    /** Every frame starts here, right after the window events: the arcade actions are sampled before any tick runs. */
    @Inject(method = "runTick", at = @At("HEAD"))
    private void mubble$sampleArcadeInput(boolean advanceGameTime, CallbackInfo ci) {
        ArcadeClientInput.onFrame((Minecraft) (Object) this);
    }
}

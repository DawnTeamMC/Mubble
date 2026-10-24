package fr.hugman.mubble.platformer.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.platformer.client.PlatformerClientInput;
import fr.hugman.mubble.platformer.client.PlatformerHands;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class PlatformerMinecraftMixin {
    /** Every frame starts here, right after the window events: the platformer actions are sampled before any tick runs. */
    @Inject(method = "runTick", at = @At("HEAD"))
    private void mubble$samplePlatformerInput(boolean advanceGameTime, CallbackInfo ci) {
        PlatformerClientInput.onFrame((Minecraft) (Object) this);
    }

    /**
     * While the platformer layer drives, the hands answer to the platformer attack and use rather than the
     * vanilla ones, and the clicks the moves take never reach them, see {@link PlatformerHands}.
     */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
    private boolean mubble$platformerHands(KeyMapping key, Operation<Boolean> original) {
        var minecraft = (Minecraft) (Object) this;
        var standIn = PlatformerHands.standIn(minecraft, key);
        if (standIn == null) {
            return original.call(key);
        }
        while (original.call(key)) {
            // the vanilla key stands aside
        }
        while (original.call(standIn)) {
            if (!PlatformerHands.takeForMoves(minecraft, standIn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aiming over the shoulder, the body turns to the crosshair right before the hands use what they
     * hold, and what they reach out to is picked again from there: a loaded crossbow shoots on the click.
     */
    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void mubble$faceTheCrosshair(CallbackInfo ci) {
        var minecraft = (Minecraft) (Object) this;
        if (PlatformerCamera.onUse(minecraft)) {
            ((MinecraftInvoker) minecraft).mubble$pick(1.0F);
        }
    }

    /** Nor does holding them, until they are let go. */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
    private boolean mubble$platformerHandsHeld(KeyMapping key, Operation<Boolean> original) {
        var standIn = PlatformerHands.standIn((Minecraft) (Object) this, key);
        return standIn == null ? original.call(key) : original.call(standIn) && !PlatformerHands.kept(standIn);
    }
}

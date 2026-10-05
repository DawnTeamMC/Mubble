package fr.hugman.mubble.arcade.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.hugman.mubble.arcade.client.ArcadeClientInput;
import fr.hugman.mubble.arcade.client.ArcadeHands;
import net.minecraft.client.KeyMapping;
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

    /** The clicks of attack and use the arcade moves take never reach the hands, see {@link ArcadeHands}. */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
    private boolean mubble$shareTheHandsWithTheMoves(KeyMapping key, Operation<Boolean> original) {
        while (original.call(key)) {
            if (!ArcadeHands.takeForMoves((Minecraft) (Object) this, key)) {
                return true;
            }
        }
        return false;
    }

    /** Nor does holding them, until they are let go. */
    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
    private boolean mubble$keepHeldKeysFromTheHands(KeyMapping key, Operation<Boolean> original) {
        return original.call(key) && !ArcadeHands.kept((Minecraft) (Object) this, key);
    }
}

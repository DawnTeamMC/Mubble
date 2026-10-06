package fr.hugman.mubble.arcade.client.compat.controlify.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.controlify.bindings.InputBindingImpl;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.input.InputComponent;
import fr.hugman.mubble.arcade.client.compat.controlify.ArcadeControllerLayout;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** A binding of Controlify's on a button of the arcade layout reads as unbound while the layout is in use, see {@link ArcadeControllerLayout#yields}. */
@Mixin(value = InputBindingImpl.class, remap = false)
public abstract class InputBindingImplMixin {
    @Shadow
    @Final
    private Identifier id;

    @Shadow
    @Final
    @Nullable
    private InputComponent inputComponent;

    @ModifyReturnValue(method = "boundInput", at = @At("RETURN"))
    private Input mubble$giveWayToTheArcadeLayout(Input bound) {
        return this.inputComponent != null && ArcadeControllerLayout.yields(this.inputComponent, this.id, bound) ? EmptyInput.INSTANCE : bound;
    }
}

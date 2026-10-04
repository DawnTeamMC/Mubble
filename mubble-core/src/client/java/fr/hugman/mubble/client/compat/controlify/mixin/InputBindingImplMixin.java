package fr.hugman.mubble.client.compat.controlify.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.bindings.InputBindingImpl;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.input.InputComponent;
import fr.hugman.mubble.client.arcade.compat.controlify.ArcadeControllerLayout;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Every tick, each Controlify binding reads the state of the input it is bound to: while the arcade
 * layer drives, a vanilla binding reads the input the arcade layout gives it instead, see
 * {@link ArcadeControllerLayout}.
 */
@Mixin(value = InputBindingImpl.class, remap = false)
public abstract class InputBindingImplMixin {
    @Shadow
    @Final
    private Identifier id;

    @Shadow
    @Final
    private InputComponent inputComponent;

    @ModifyExpressionValue(method = "pushState", at = @At(value = "INVOKE", target = "Ldev/isxander/controlify/bindings/InputBindingImpl;boundInput()Ldev/isxander/controlify/bindings/input/Input;"))
    private Input mubble$arcadeLayout(Input bound) {
        return ArcadeControllerLayout.inputFor(this.inputComponent, this.id, bound);
    }
}

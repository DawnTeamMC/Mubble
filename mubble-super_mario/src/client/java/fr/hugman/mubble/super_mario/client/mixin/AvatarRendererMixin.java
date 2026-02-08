package fr.hugman.mubble.super_mario.client.mixin;

import fr.hugman.mubble.super_mario.client.renderer.SuperMarioRenderTypes;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private RenderType super_mario$renderHand(Identifier texture) {
        var powerUp = Minecraft.getInstance().player.getPowerUp();
        if (powerUp.isPresent()) {
            if (powerUp.get().is(SuperMarioPowerUpKeys.GOLD)) {
                return SuperMarioRenderTypes.getGoldenEntity(texture);
            }
        }
        return RenderTypes.entityTranslucent(texture);
    }
}

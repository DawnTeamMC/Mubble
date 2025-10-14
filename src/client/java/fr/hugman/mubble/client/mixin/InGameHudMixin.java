package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.gui.hud.PowerUpHudRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Final
	@Shadow
	private MinecraftClient client;

	@Inject(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud;renderBossBarHud(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
	private void mubble$addPowerUpLayer(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		PowerUpHudRendering.renderPowerUpLayer(this.client, context);
	}
}
package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.gui.hud.PowerUpHudRendering;import fr.hugman.mubble.client.gui.hud.PowerUpPropertiesHudRendering;import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {
	@Final
	@Shadow
	private Minecraft minecraft;

	@Inject(method="extractRenderState", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Hud;extractBossOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"))
	private void mubble$addPowerUpLayer(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		PowerUpHudRendering.renderPowerUpLayer(this.minecraft, graphics);
	}

	@Inject(method="extractRenderState", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Hud;extractCrosshair(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"))
	private void mubble$addPowerUpPropertiesLayer(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		PowerUpPropertiesHudRendering.renderChargesLayer(this.minecraft, graphics);
	}
}
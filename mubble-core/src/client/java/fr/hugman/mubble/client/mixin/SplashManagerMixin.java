package fr.hugman.mubble.client.mixin;

import fr.hugman.mubble.client.gui.screens.MubbleSplashes;
import net.minecraft.client.resources.SplashManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
	@Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private void mubble$addExtraSplashes(CallbackInfoReturnable<List<String>> cir) {
		List<String> splashes = new ArrayList<>(cir.getReturnValue());
		splashes.addAll(MubbleSplashes.all());
		cir.setReturnValue(splashes);
	}
}

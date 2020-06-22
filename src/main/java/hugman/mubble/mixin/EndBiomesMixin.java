package hugman.mubble.mixin;

import hugman.mubble.init.world.MubbleBiomes;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheEndBiomeSource.class)
public class EndBiomesMixin {
	@Shadow
	private SimplexNoiseSampler noise;

	@Inject(method = "getBiomeForNoiseGen", at = @At(value = "HEAD"), cancellable = true)
	private void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> info) {
		int i = biomeX >> 2;
		int j = biomeZ >> 2;
		if (!((long)i * (long)i + (long)j * (long)j <= 4096L)) {
			float f = TheEndBiomeSource.getNoiseAt(this.noise, i * 2 + 1, j * 2 + 1);
			if(f > 30.0F) {
				info.setReturnValue(Biomes.END_HIGHLANDS);
			}
			if(f > 50.0F) {
				info.setReturnValue(MubbleBiomes.DARK_AMARANTH_FOREST);
			}
		}
	}
}

package fr.hugman.mubble.client.particle;

import fr.hugman.mubble.core.particles.MubbleParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

public class MubbleParticleResources {
	public static void register() {
		var instance = ParticleProviderRegistry.getInstance();

		instance.register(MubbleParticleTypes.GOLD_SPARK, GoldSparkParticle.Provider::new);
	}
}

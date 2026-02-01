package fr.hugman.mubble.super_mario.client.particle;

import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

public class SuperMarioParticleResources {
	public static void register() {
		var instance = ParticleProviderRegistry.getInstance();

		instance.register(SuperMarioParticleTypes.COIN_SPARKLE, CoinSparkleParticle.Provider::new);
		instance.register(SuperMarioParticleTypes.RED_COIN_SPARKLE, CoinSparkleParticle.Provider::new);
		instance.register(SuperMarioParticleTypes.BLUE_COIN_SPARKLE, CoinSparkleParticle.Provider::new);
		instance.register(SuperMarioParticleTypes.FLOWER_COIN_SPARKLE, CoinSparkleParticle.Provider::new);
	}
}

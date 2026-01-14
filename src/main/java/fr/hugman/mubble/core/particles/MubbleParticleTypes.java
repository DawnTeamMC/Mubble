package fr.hugman.mubble.core.particles;

import fr.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class MubbleParticleTypes {
	public static final SimpleParticleType COIN_SPARKLE = register("coin_sparkle");
	public static final SimpleParticleType RED_COIN_SPARKLE = register("red_coin_sparkle");
	public static final SimpleParticleType BLUE_COIN_SPARKLE = register("blue_coin_sparkle");
	public static final SimpleParticleType FLOWER_COIN_SPARKLE = register("flower_coin_sparkle");

	private static SimpleParticleType register(String name) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mubble.id(name), FabricParticleTypes.simple());
	}
	private static SimpleParticleType register(String name, boolean overrideLimiter) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mubble.id(name), FabricParticleTypes.simple(overrideLimiter));
	}
}

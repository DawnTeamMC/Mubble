package fr.hugman.mubble.core.particles;

import fr.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class MubbleParticleTypes {
	public static final SimpleParticleType GOLD_SPARK = register("gold_spark");

	private static SimpleParticleType register(String name) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mubble.id(name), FabricParticleTypes.simple());
	}
	private static SimpleParticleType register(String name, boolean overrideLimiter) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mubble.id(name), FabricParticleTypes.simple(overrideLimiter));
	}
}

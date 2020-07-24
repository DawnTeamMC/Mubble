package hugman.mubble.util.creator;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.biomes.v1.FabricBiomes;
import net.fabricmc.fabric.api.biomes.v1.NetherBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BiomeEntry {
	private Biome biome;

	private final String name;
	private final Biome baseBiome;
	private final SpawnDimension spawnDimension;

	private final OverworldClimate climate;
	private final double weight;
	private final boolean isSpawnBiome;

	private final Biome.MixedNoisePoint noises;

	private BiomeEntry(String name, Biome baseBiome, SpawnDimension spawnDimension, OverworldClimate climate, double weight, boolean isSpawnBiome, Biome.MixedNoisePoint noises) {
		this.name = name;
		this.baseBiome = baseBiome;
		this.spawnDimension = spawnDimension;
		this.climate = climate;
		this.weight = weight;
		this.isSpawnBiome = isSpawnBiome;
		this.noises = noises;
	}

	private BiomeEntry register() {
		this.biome = Registry.register(BuiltinRegistries.BIOME, new Identifier(Mubble.MOD_ID, name), baseBiome);
		switch(this.spawnDimension)
		{
			case NONE:
			default:
				break;
			case OVERWORLD_CONTINENTAL:
				OverworldBiomes.addContinentalBiome(biome, climate, weight);
				if(isSpawnBiome) FabricBiomes.addSpawnBiome(biome);
			case THE_NETHER:
				NetherBiomes.addNetherBiome(biome, noises);
		}
		return this;
	}

	public static class Builder {
		private final String name;
		private final Biome baseBiome;
		private SpawnDimension spawnDimension;

		private OverworldClimate climate;
		private double weight;
		private boolean isSpawnBiome;

		private Biome.MixedNoisePoint noises;

		public Builder(String name, Biome baseBiome) {
			this.name = name;
			this.baseBiome = baseBiome;
			this.spawnDimension = SpawnDimension.NONE;
			this.climate = null;
			this.weight = 0D;
			this.isSpawnBiome = false;
			this.noises = null;
		}

		/**
		 * Adds the biome to the overworld continental generation.
		 * @param climate The biome climate.
		 * @param weight The biome weight.
		 * @param isSpawnBiome Defines if the player should be able to naturally spawn in the biome.
		 */
		public Builder addToOverworldContinental(OverworldClimate climate, double weight, boolean isSpawnBiome) {
			this.spawnDimension = SpawnDimension.OVERWORLD_CONTINENTAL;
			this.climate = climate;
			this.weight = weight;
			this.isSpawnBiome = isSpawnBiome;
			return this;
		}

		/**
		 * Adds the biome to the nether generation.
		 */
		public Builder addToNether(float temperature, float humidity, float altitude, float weirdness, float weight) {
			this.spawnDimension = SpawnDimension.THE_NETHER;
			this.noises = new Biome.MixedNoisePoint(temperature, humidity, altitude, weirdness, weight);
			return this;
		}

		/**
		 * Builds the entry and registers the biome with all its settings.
		 */
		public BiomeEntry build() {
			return new BiomeEntry(this.name, this.baseBiome, this.spawnDimension, this.climate, this.weight, this.isSpawnBiome, this.noises).register();
		}
	}

	public Biome getBiome() {
		return biome;
	}

	public enum SpawnDimension {
		NONE,
		OVERWORLD_CONTINENTAL,
		THE_NETHER,
		THE_END
	}
}

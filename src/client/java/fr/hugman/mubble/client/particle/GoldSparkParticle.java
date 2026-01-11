package fr.hugman.mubble.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GoldSparkParticle extends SingleQuadParticle {
	protected GoldSparkParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}

	@Override
	protected Layer getLayer() {
		return SingleQuadParticle.Layer.OPAQUE;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(final SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(
				SimpleParticleType options,
				ClientLevel level,
				double x,
				double y,
				double z,
				double xAux,
				double yAux,
				double zAux,
				RandomSource random
		) {
			return new GoldSparkParticle(level, x, y, z, this.sprite.get(random));
		}
	}

}

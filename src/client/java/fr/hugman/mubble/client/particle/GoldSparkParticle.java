package fr.hugman.mubble.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GoldSparkParticle extends SingleQuadParticle {
	protected boolean rollDirection;
	protected float rollSpeed;

	protected GoldSparkParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}

	@Override
	public void tick() {
		super.tick();

		oRoll = roll;
		roll += rollSpeed;

		var progress = age / (float) lifetime;
		rollSpeed = (1 - progress) * 0.5f * (rollDirection ? -1 : 1);
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
			var particle = new GoldSparkParticle(level, x, y, z, this.sprite.get(random));

			particle.hasPhysics = false;
			particle.lifetime = 5 + random.nextInt(8);
			particle.rollDirection = random.nextBoolean();
			particle.roll = random.nextFloat() * (float) (Math.PI * 2);

			particle.xd = random.nextGaussian() * 0.03;
			particle.zd = random.nextGaussian() * 0.03;
			particle.yd = 0.04 + random.nextFloat() * 0.025;

			return particle;
		}
	}

}

package fr.hugman.mubble.super_mario.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class CoinSparkleParticle extends SingleQuadParticle {
	protected boolean rollDirection = false;
	protected float rollAcceleration = 0.0f;
	protected float rollSpeed;

	protected CoinSparkleParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
	}

	@Override
	public void tick() {
		super.tick();

		oRoll = roll;
		roll += rollSpeed;

		var progress = age / (float) lifetime;
		rollSpeed = (1 - progress) * rollAcceleration * (rollDirection ? -1 : 1);
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}

    @Override
    public float getQuadSize(float partialTickTime) {
		var quadSize = super.getQuadSize(partialTickTime);
        if(age == 0) {
            return Mth.lerp(partialTickTime, 0, quadSize);
        }
        if(age == lifetime) {
            return Mth.lerp(partialTickTime, quadSize, 0);
        }
		return quadSize;
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
			var particle = new CoinSparkleParticle(level, x, y, z, this.sprite.get(random));

			particle.hasPhysics = false;
			particle.lifetime = 5 + random.nextInt(3);
			particle.rollDirection = random.nextBoolean();
			particle.rollAcceleration = 0.5f;
			particle.roll = random.nextFloat() * (float) (Math.PI * 2);
			particle.oRoll = particle.roll - particle.rollAcceleration * (particle.rollDirection ? -1 : 1);

			particle.xd = random.nextGaussian() * 0.05;
			particle.zd = random.nextGaussian() * 0.05;
			particle.yd = 0.04 + random.nextFloat() * 0.025;

			return particle;
		}
	}

}

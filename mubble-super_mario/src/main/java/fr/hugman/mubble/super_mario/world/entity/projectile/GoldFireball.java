package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeKeys;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.level.GoldenServerExplosion;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class GoldFireball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/gold_fireball"));

    public GoldFireball(EntityType<? extends GoldFireball> type, Level level) {
        super(type, level);
    }

    public GoldFireball(Level level, LivingEntity owner) {
        super(SuperMarioEntityTypes.GOLD_FIREBALL, level, owner);
    }

    public GoldFireball(double x, double y, double z, Level level) {
        super(SuperMarioEntityTypes.GOLD_FIREBALL, x, y, z, level);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SuperMarioSounds.FIREBALL_HIT_BLOCK; //TODO change
    }

    @Override
    protected ParticleOptions getDeathParticle() {
        return ParticleTypes.FLAME; //TODO change
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Direction face = result.getDirection();
        if (face == Direction.UP) {
            this.reboundUp();
        } else {
            this.finalHit();
        }
    }

	@Override
	protected void finalHit(SoundEvent deathSound) {
		super.finalHit(deathSound);
        if(this.level() instanceof ServerLevel serverLevel) {
            GoldenServerExplosion.create(serverLevel, this, this.damageSources().source(SuperMarioDamageTypeKeys.GOLD_FIREBALL, this, this.getOwner()), null, this.getX(), this.getY(0.0625F), this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
        }
	}

	@Override
    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }
}
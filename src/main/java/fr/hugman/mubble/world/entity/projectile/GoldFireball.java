package fr.hugman.mubble.world.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.references.MubbleDamageTypeKeys;
import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.level.GoldenServerExplosion;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GoldFireball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(Mubble.id("entity/gold_fireball"));

    public GoldFireball(EntityType<? extends GoldFireball> type, Level level) {
        super(type, level);
    }

    public GoldFireball(Level level, LivingEntity owner) {
        super(MubbleEntityTypes.GOLD_FIREBALL, level, owner);
    }

    public GoldFireball(double x, double y, double z, Level level) {
        super(MubbleEntityTypes.GOLD_FIREBALL, x, y, z, level);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MubbleSounds.FIREBALL_HIT_BLOCK; //TODO change
    }

    @Override
    protected ParticleOptions getDeathParticle() {
        return ParticleTypes.FLAME; //TODO change
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        float damage = entity.fireImmune() ? 1.0F : 3.0F;

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.setLastHurtMob(entity);
        }

        if (!entity.fireImmune()) {
            entity.igniteForSeconds(5);
        }
        entity.hurt(this.damageSources().source(MubbleDamageTypeKeys.GOLD_FIREBALL, this, this.getOwner()), damage);
		//TODO change sound
		this.finalHit(MubbleSounds.FIREBALL_HIT_ENTITY);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Direction face = result.getDirection();
        if (face == Direction.UP) {
            Vec3 motion = this.getDeltaMovement().subtract(0.0D, this.getDeltaMovement().y * 1.25D, 0.0D);
            double minY = 0.4D;
            if (motion.y < minY) {
                motion = motion.with(Direction.Axis.Y, minY);
            }
            this.setDeltaMovement(motion);
        } else {
            this.finalHit();
        }
    }

	@Override
	protected void finalHit(SoundEvent deathSound) {
		super.finalHit(deathSound);
        if(this.level() instanceof ServerLevel serverLevel) {
            GoldenServerExplosion.create(serverLevel, this, this.damageSources().source(MubbleDamageTypeKeys.GOLD_FIREBALL, this, this.getOwner()), null, this.getX(), this.getY(0.0625F), this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
        }
	}

	@Override
    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }
}
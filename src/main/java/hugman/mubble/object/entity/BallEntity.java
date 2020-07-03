package hugman.mubble.object.entity;

import hugman.mubble.init.MubbleSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public abstract class BallEntity extends ThrownItemEntity {
	protected int reboundingAmount = 3;

	public BallEntity(EntityType<? extends BallEntity> entityType, World world) {
		super(entityType, world);
	}

	public BallEntity(EntityType<? extends BallEntity> entityType, World world, LivingEntity owner) {
		super(entityType, owner, world);
	}

	public BallEntity(EntityType<? extends BallEntity> entityType, World world, double x, double y, double z) {
		super(entityType, x, y, z, world);
	}

	protected abstract SoundEvent getDeathSound();

	protected abstract ParticleEffect getDeathParticle();

	protected abstract boolean onEntityImpact(EntityHitResult result);

	protected abstract boolean onBlockImpact(BlockHitResult result);

	@Override
	public void writeCustomDataToTag(CompoundTag compound) {
		super.writeCustomDataToTag(compound);
		compound.putInt("ReboundingAmount", reboundingAmount);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag compound) {
		super.readCustomDataFromTag(compound);
		this.reboundingAmount = compound.getInt("ReboundingAmount");
	}

	@Override
	protected void onCollision(HitResult result) {
		boolean removeOnImpact = true;
		if(result.getType() == HitResult.Type.ENTITY) {
			removeOnImpact = onEntityImpact((EntityHitResult) result);
		}
		else if(result.getType() == HitResult.Type.BLOCK) {
			removeOnImpact = onBlockImpact((BlockHitResult) result);
		}
		boolean cantRebound = reboundingAmount <= 0;
		if(removeOnImpact || cantRebound) {
			if(!this.world.isClient) {
				this.world.sendEntityStatus(this, (byte) 3);
				this.remove();
			}
			if(!removeOnImpact && cantRebound) {
				this.world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			}
		}
		else {
			reboundingAmount--;
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleStatus(byte state) {
		if(state == 3) {
			this.spawnDeathParticles();
		}
	}

	protected void spawnDeathParticles() {
		for(int i = 0; i < 8; ++i) {
			float s1 = random.nextFloat() * 0.2F - 0.1F;
			float s2 = random.nextFloat() * 0.2F - 0.1F;
			float s3 = random.nextFloat() * 0.2F - 0.1F;
			world.addParticle(getDeathParticle(), this.getX(), this.getY(), this.getZ(), s1, s2, s3);
		}
	}

	@Override
	public Packet<?> createSpawnPacket() {
		Entity entity = this.getOwner();
		return new EntitySpawnS2CPacket(this, entity == null ? 0 : entity.getEntityId());
	}
}

package com.hugman.mubble.object.entity;

import com.hugman.mubble.init.MubbleEntityPack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class CustomTNTEntity extends Entity {
	private static final TrackedData<Integer> FUSE = DataTracker.registerData(CustomTNTEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Float> STRENGHT = DataTracker.registerData(CustomTNTEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private BlockState customTile = Blocks.SAND.getDefaultState();
	private int fuse = 80;
	private float strenght = 4.0F;
	private LivingEntity causingEntity;

	public CustomTNTEntity(EntityType<? extends CustomTNTEntity> type, World worldIn) {
		super(type, worldIn);
		this.inanimate = true;
	}

	public CustomTNTEntity(BlockState customTileIn, World worldIn, double x, double y, double z, int fuse, float strength, LivingEntity igniter) {
		this(MubbleEntityPack.CUSTOM_TNT, worldIn);
		this.customTile = customTileIn;
		this.updatePosition(x, y, z);
		float f = (float) (Math.random() * (double) ((float) Math.PI * 2F));
		this.setVelocity(-((float) Math.sin(f)) * 0.02F, 0.2F, -((float) Math.cos(f)) * 0.02F);
		this.setFuse(fuse);
		this.setStrenght(strength);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.causingEntity = igniter;
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(FUSE, fuse);
		this.dataTracker.startTracking(STRENGHT, strenght);
	}

	@Override
	protected boolean canClimb() {
		return false;
	}

	@Override
	public boolean collides() {
		return !this.removed;
	}

	@Override
	public void tick() {
		if(!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
		}
		this.move(MovementType.SELF, this.getVelocity());
		this.setVelocity(this.getVelocity().multiply(0.98D));
		if(this.onGround) {
			this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
		}
		--this.fuse;
		if(this.fuse <= 0) {
			this.remove();
			if(!this.world.isClient) {
				this.explode();
			}
		}
		else {
			this.updateWaterState();
			if(this.world.isClient) {
				this.world.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	private void explode() {
		this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), this.strenght, Explosion.DestructionType.BREAK);
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag compound) {
		compound.put("BlockState", NbtHelper.fromBlockState(this.customTile));
		compound.putShort("Fuse", (short) this.getFuse());
		compound.putFloat("Strenght", this.getStrenght());
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag compound) {
		this.customTile = NbtHelper.toBlockState(compound.getCompound("BlockState"));
		if(this.customTile.getBlock() == Blocks.AIR) {
			this.customTile = Blocks.TNT.getDefaultState();
		}
		this.setFuse(compound.getShort("Fuse"));
		this.setStrenght(compound.getFloat("Strenght"));
	}

	public LivingEntity getCausingEntity() {
		return this.causingEntity;
	}

	public void setFuse(int fuseIn) {
		this.dataTracker.set(FUSE, fuseIn);
		this.fuse = fuseIn;
	}

	public int getFuse() {
		return this.fuse;
	}

	public BlockState getBlockState() {
		return this.customTile;
	}

	public void setStrenght(float strenghtIn) {
		this.dataTracker.set(STRENGHT, strenghtIn);
		this.strenght = strenghtIn;
	}

	public float getStrenght() {
		return this.strenght;
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> key) {
		if(FUSE.equals(key)) {
			this.fuse = this.getFuseDataManager();
		}
	}

	public int getFuseDataManager() {
		return this.dataTracker.get(FUSE);
	}

	@Override
	public void populateCrashReport(CrashReportSection category) {
		super.populateCrashReport(category);
		category.add("Immitating BlockState", this.customTile.toString());
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}
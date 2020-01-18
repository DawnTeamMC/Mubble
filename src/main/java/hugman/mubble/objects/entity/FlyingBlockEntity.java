package hugman.mubble.objects.entity;

import java.util.List;

import com.google.common.collect.Lists;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.block.FlyingBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AirBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Packet;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class FlyingBlockEntity extends Entity
{
	private BlockState flyTile = MubbleBlocks.WHITE_BALLOON.getDefaultState();
	private int flyTime;
	private boolean shouldDropItem = true;
	private boolean dontSetBlock;
	private boolean hurtEntities;
	private int flyHurtMax = 40;
	private float flyHurtAmount = 2.0F;
	private CompoundTag tileEntityData;
	protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FlyingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

	public FlyingBlockEntity(EntityType<? extends FlyingBlockEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public FlyingBlockEntity(World worldIn, double x, double y, double z, BlockState flyingBlockState)
	{
		this(MubbleEntities.FLYING_BLOCK, worldIn);
		this.flyTile = flyingBlockState;
		this.inanimate = true;
		this.updatePosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.setOrigin(new BlockPos(this));
	}
	
	public void setOrigin(BlockPos pos)
	{
		this.dataTracker.set(ORIGIN, pos);
	}

	@Environment(EnvType.CLIENT)
	public BlockPos getOrigin()
	{
		return this.dataTracker.get(ORIGIN);
	}
	
	@Override
	protected void initDataTracker()
	{
		this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
	}
	
	@Override
	public boolean collides()
	{
		return isAlive();
	}
	
	@Override
	public void tick()
	{
		if(this.flyTile.getBlock() instanceof AirBlock)
		{
			this.remove();
		}
		else
		{
			this.prevX = this.getX();
			this.prevY = this.getY();
			this.prevZ = this.getZ();
			Block block = this.flyTile.getBlock();
			if (this.flyTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this);
				if (this.world.getBlockState(blockpos).getBlock() == block)
				{
					this.world.removeBlock(blockpos, false);
				}
				else if (!this.world.isClient)
				{
					this.remove();
					return;
				}
			}
			if(!this.hasNoGravity())
			{
				this.setVelocity(this.getVelocity().add(0.0D, 0.01D, 0.0D));
			}
			this.move(MovementType.SELF, this.getVelocity());
			if (!this.world.isClient)
			{
				BlockPos blockpos1 = new BlockPos(this);
				boolean flag = this.flyTile.getBlock() instanceof ConcretePowderBlock;
				boolean flag1 = flag && this.world.getFluidState(blockpos1).matches(FluidTags.WATER);
				double d0 = this.getVelocity().lengthSquared();
	            if (flag && d0 > 1.0D)
	            {
	                BlockHitResult blockraytraceresult = this.world.rayTrace(new RayTraceContext(new Vec3d(this.prevX, this.prevY, this.prevZ), new Vec3d(this.getX(), this.getY(), this.getZ()), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY, this));
	                if (blockraytraceresult.getType() != HitResult.Type.MISS && this.world.getFluidState(blockraytraceresult.getBlockPos()).matches(FluidTags.WATER))
	                {
	                   blockpos1 = blockraytraceresult.getBlockPos();
	                   flag1 = true;
	                }
	             }
				if(FlyingBlock.canFlyThrough(this.world.getBlockState(blockpos1.up())) && !flag1)
				{
					if(!this.world.isClient && (this.flyTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.flyTime > 600))
					{
						if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						{
							this.dropItem(block);
						}
						this.remove();
					}
				}
				else
				{
					BlockState blockstate = this.world.getBlockState(blockpos1);
					this.setVelocity(this.getVelocity().multiply(0.7D, 0.4D, 0.7D));
					if(blockstate.getBlock() != Blocks.MOVING_PISTON)
					{
						this.remove();
						if(!this.dontSetBlock)
						{
							boolean flag2 = blockstate.canReplace(new AutomaticItemPlacementContext(this.world, blockpos1, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
							boolean flag3 = this.flyTile.canPlaceAt(this.world, blockpos1);
							if(flag2 && flag3)
							{
								if(this.flyTile.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockpos1).getFluid() == Fluids.WATER)
								{
									this.flyTile = this.flyTile.with(Properties.WATERLOGGED, Boolean.valueOf(true));
								}
								
								if(this.world.setBlockState(blockpos1, this.flyTile, 3))
								{
									if(block instanceof FlyingBlock)
									{
										((FlyingBlock) block).onEndFlying(this.world, blockpos1, this.flyTile, blockstate);
									}
									
									if(this.tileEntityData != null && flyTile instanceof BlockEntityProvider)
									{
										BlockEntity tileEntity = this.world.getBlockEntity(blockpos1);
										if(tileEntity != null)
										{
											CompoundTag compoundNbt = tileEntity.toTag(new CompoundTag());
											
											for(String s : this.tileEntityData.getKeys())
											{
												Tag inbt = this.tileEntityData.get(s);
												if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
												{
		        	                            	compoundNbt.put(s, inbt.copy());
		        	                            }
											}
											
											tileEntity.fromTag(compoundNbt);
											tileEntity.markDirty();
										}
									}
								}
								else if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
								{
									this.dropItem(block);
								}
							}
							else if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
							{
								this.dropItem(block);
							}
						}
						else if(block instanceof FlyingBlock)
						{
							((FlyingBlock) block).onBroken(this.world, blockpos1);
						}
					}
				}
			}
			this.setVelocity(this.getVelocity().multiply(0.98D));
		}
	}

	public boolean handleFallDamage(float distance, float damageMultiplier)
	{
		if (this.hurtEntities)
		{
			int i = MathHelper.ceil(distance - 1.0F);
			if (i > 0)
			{
				List<Entity> list = Lists.newArrayList(this.world.getEntities(this, this.getBoundingBox()));
				boolean flag = this.flyTile.matches(BlockTags.ANVIL);
				DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

				for(Entity entity : list)
				{
					entity.damage(damagesource, (float)Math.min(MathHelper.floor((float)i * this.flyHurtAmount), this.flyHurtMax));
				}

				if (flag && (double)this.random.nextFloat() < (double)0.05F + (double)i * 0.05D)
				{
					BlockState iblockstate = AnvilBlock.getLandingState(this.flyTile);
					if (iblockstate == null)
					{
						this.dontSetBlock = true;
					}
					else
					{
						this.flyTile = iblockstate;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag compound)
	{
		compound.put("BlockState", NbtHelper.fromBlockState(this.flyTile));
		compound.putInt("Time", this.flyTime);
		compound.putBoolean("DropItem", this.shouldDropItem);
		compound.putBoolean("HurtEntities", this.hurtEntities);
		compound.putFloat("FlyHurtAmount", this.flyHurtAmount);
		compound.putInt("FlyHurtMax", this.flyHurtMax);
		if (this.tileEntityData != null)
		{
			compound.put("TileEntityData", this.tileEntityData);
		}
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag compound)
	{
		this.flyTile = NbtHelper.toBlockState(compound.getCompound("BlockState"));
		this.flyTime = compound.getInt("Time");
		if (compound.contains("HurtEntities", 99)) {
			this.hurtEntities = compound.getBoolean("HurtEntities");
			this.flyHurtMax = compound.getInt("FlyHurtMax");
		}
		else if(this.flyTile.matches(BlockTags.ANVIL))
		{
			this.hurtEntities = true;
		}
		if(compound.contains("DropItem", 99))
		{
			this.shouldDropItem = compound.getBoolean("DropItem");
		}
		if(compound.contains("TileEntityData", 10))
		{
			this.tileEntityData = compound.getCompound("TileEntityData");
		}
		if(this.flyTile.getBlock() instanceof AirBlock)
		{
			this.flyTile = MubbleBlocks.WHITE_BALLOON.getDefaultState();
		}

	}

	@Environment(EnvType.CLIENT)
	public World getWorldObj()
	{
		return this.world;
	}
	
	public void setHurtEntities(boolean hurtEntitiesIn)
	{
		this.hurtEntities = hurtEntitiesIn;
	}
	
	@Environment(EnvType.CLIENT)
	public boolean canRenderOnFire()
	{
		return false;
	}
	
	@Override
	public void populateCrashReport(CrashReportSection category)
	{
		super.populateCrashReport(category);
		category.add("Immitating BlockState", this.flyTile.toString());
	}
	
	public BlockState getBlockState()
	{
		return this.flyTile;
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return new EntitySpawnS2CPacket(this);
	}
}
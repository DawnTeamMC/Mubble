package hugman.mubble.objects.entity;

import java.util.List;

import com.google.common.collect.Lists;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.block.FlyingBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class FlyingBlockEntity extends Entity implements IEntityAdditionalSpawnData
{
	private BlockState flyTile = MubbleBlocks.WHITE_BALLOON.getDefaultState();
	private int flyTime;
	private boolean shouldDropItem = true;
	private boolean dontSetBlock;
	private boolean hurtEntities;
	private int flyHurtMax = 40;
	private float flyHurtAmount = 2.0F;
	private CompoundNBT tileEntityData;
	protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(FlyingBlockEntity.class, DataSerializers.BLOCK_POS);

	public FlyingBlockEntity(EntityType<? extends FlyingBlockEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public FlyingBlockEntity(World worldIn, double x, double y, double z, BlockState flyingBlockState)
	{
		this(MubbleEntities.FLYING_BLOCK, worldIn);
		this.flyTile = flyingBlockState;
		this.preventEntitySpawning = true;
		this.setPosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
		this.setMotion(Vec3d.ZERO);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.setOrigin(new BlockPos(this));
	}

	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
	
	public void setOrigin(BlockPos pos)
	{
		this.dataManager.set(ORIGIN, pos);
	}

	@OnlyIn(Dist.CLIENT)
	public BlockPos getOrigin()
	{
		return this.dataManager.get(ORIGIN);
	}
	
	@Override
	protected boolean canClimb()
	{
		return false;
	}
	
	@Override
	protected void registerData()
	{
		this.dataManager.register(ORIGIN, BlockPos.ZERO);
	}
	
	@Override
	public boolean canBeCollidedWith()
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
			Block block = this.flyTile.getBlock();
			if (this.flyTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this);
				if (this.world.getBlockState(blockpos).getBlock() == block)
				{
					this.world.removeBlock(blockpos, false);
				}
				else if (!this.world.isRemote)
				{
					this.remove();
					return;
				}
			}
			if(!this.hasNoGravity())
			{
				this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D));
			}
			this.move(MoverType.SELF, this.getMotion());
			if (!this.world.isRemote)
			{
				BlockPos blockpos1 = new BlockPos(this);
				boolean flag = this.flyTile.getBlock() instanceof ConcretePowderBlock;
				boolean flag1 = flag && this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER);
				double d0 = this.getMotion().lengthSquared();
	            if (flag && d0 > 1.0D)
	            {
	                BlockRayTraceResult blockraytraceresult = this.world.rayTraceBlocks(new RayTraceContext(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), this.getPositionVec(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));
	                if (blockraytraceresult.getType() != RayTraceResult.Type.MISS && this.world.getFluidState(blockraytraceresult.getPos()).isTagged(FluidTags.WATER))
	                {
	                   blockpos1 = blockraytraceresult.getPos();
	                   flag1 = true;
	                }
	             }
				if(FlyingBlock.canFlyThrough(this.world.getBlockState(blockpos1.up())) && !flag1)
				{
					if(!this.world.isRemote && (this.flyTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.flyTime > 600))
					{
						if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
						{
							this.entityDropItem(block);
						}
						this.remove();
					}
				}
				else
				{
					BlockState blockstate = this.world.getBlockState(blockpos1);
					this.setMotion(this.getMotion().mul(0.7D, 0.4D, 0.7D));
					if(blockstate.getBlock() != Blocks.MOVING_PISTON)
					{
						this.remove();
						if(!this.dontSetBlock)
						{
							boolean flag2 = blockstate.isReplaceable(new DirectionalPlaceContext(this.world, blockpos1, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
							boolean flag3 = this.flyTile.isValidPosition(this.world, blockpos1);
							if(flag2 && flag3)
							{
								if(this.flyTile.has(BlockStateProperties.WATERLOGGED) && this.world.getFluidState(blockpos1).getFluid() == Fluids.WATER)
								{
									this.flyTile = this.flyTile.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
								}
								
								if(this.world.setBlockState(blockpos1, this.flyTile, 3))
								{
									if(block instanceof FlyingBlock)
									{
										((FlyingBlock)block).onEndFlying(this.world, blockpos1, this.flyTile, blockstate);
									}
									
									if(this.tileEntityData != null && this.flyTile.hasTileEntity())
									{
										TileEntity tileEntity = this.world.getTileEntity(blockpos1);
										if(tileEntity != null)
										{
											CompoundNBT compoundNbt = tileEntity.write(new CompoundNBT());
											
											for(String s : this.tileEntityData.keySet())
											{
												INBT inbt = this.tileEntityData.get(s);
												if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
												{
		        	                            	compoundNbt.put(s, inbt.copy());
		        	                            }
											}
											
											tileEntity.read(compoundNbt);
											tileEntity.markDirty();
										}
									}
								}
								else if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
								{
									this.entityDropItem(block);
								}
							}
							else if(this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
							{
								this.entityDropItem(block);
							}
						}
						else if(block instanceof FlyingBlock)
						{
							((FlyingBlock)block).onBroken(this.world, blockpos1);
						}
					}
				}
			}
			this.setMotion(this.getMotion().scale(0.98D));
		}
	}
	
	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier)
	{
		if (this.hurtEntities)
		{
			int i = MathHelper.ceil(distance - 1.0F);
			if (i > 0)
			{
				List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()));
				boolean flag = this.flyTile.isIn(BlockTags.ANVIL);
				DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

				for(Entity entity : list)
				{
					entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * this.flyHurtAmount), this.flyHurtMax));
				}

				if (flag && (double)this.rand.nextFloat() < (double)0.05F + (double)i * 0.05D)
				{
					BlockState iblockstate = AnvilBlock.damage(this.flyTile);
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
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.put("BlockState", NBTUtil.writeBlockState(this.flyTile));
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
	protected void readAdditional(CompoundNBT compound)
	{
		this.flyTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
		this.flyTime = compound.getInt("Time");
		if (compound.contains("HurtEntities", 99)) {
			this.hurtEntities = compound.getBoolean("HurtEntities");
			this.flyHurtMax = compound.getInt("FlyHurtMax");
		}
		else if(this.flyTile.isIn(BlockTags.ANVIL))
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

	@OnlyIn(Dist.CLIENT)
	public World getWorldObj()
	{
		return this.world;
	}
	
	public void setHurtEntities(boolean hurtEntitiesIn)
	{
		this.hurtEntities = hurtEntitiesIn;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderOnFire()
	{
		return false;
	}
	
	@Override
	public void fillCrashReport(CrashReportCategory category)
	{
		super.fillCrashReport(category);
		category.addDetail("Immitating BlockState", this.flyTile.toString());
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(Block.getStateId(flyTile));
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		this.flyTile = Block.getStateById(additionalData.readInt());
	}
	
	public BlockState getBlockState()
	{
		return this.flyTile;
	}

	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
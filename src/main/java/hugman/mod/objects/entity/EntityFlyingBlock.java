package hugman.mod.objects.entity;

import java.util.List;

import com.google.common.collect.Lists;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleEntities;
import hugman.mod.objects.block.BlockFlying;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityFlyingBlock extends Entity implements IEntityAdditionalSpawnData
{
	public IBlockState flyTile = MubbleBlocks.WHITE_BALLOON.getDefaultState();
	public int flyTime;
	public boolean shouldDropItem = true;
	private boolean dontSetBlock;
	private boolean hurtEntities;
	private int flyHurtMax = 40;
	private float flyHurtAmount = 2.0F;
	public NBTTagCompound tileEntityData;
	protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFlyingBlock.class, DataSerializers.BLOCK_POS);

	public EntityFlyingBlock(World worldIn)
	{
		super(MubbleEntities.FLYING_BLOCK, worldIn);
	}

	public EntityFlyingBlock(World worldIn, double x, double y, double z, IBlockState flyingBlockState)
	{
		this(worldIn);
		this.flyTile = flyingBlockState;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.setPosition(x, y + (double)((1.0F - this.height) / 2.0F), z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
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
	
	public void setOrigin(BlockPos p_184530_1_)
	{
		this.dataManager.set(ORIGIN, p_184530_1_);
	}

	@OnlyIn(Dist.CLIENT)
	public BlockPos getOrigin()
	{
		return this.dataManager.get(ORIGIN);
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	protected void registerData()
	{
		this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return isAlive();
	}
	
	@Override
	public void tick()
	{
		if (this.flyTile == Blocks.AIR) this.remove();
		else
		{
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			Block block = this.flyTile.getBlock();
			if (this.flyTime++ == 0)
			{
				BlockPos blockpos = new BlockPos(this);
				if (this.world.getBlockState(blockpos).getBlock() == block)
				{
					this.world.removeBlock(blockpos);
				}
				else if (!this.world.isRemote)
				{
					this.remove();
					return;
				}
			}
			if (!this.hasNoGravity()) this.motionY += (double)0.01F;
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			if (!this.world.isRemote)
			{
				BlockPos blockpos1 = new BlockPos(this);
				boolean flag = this.flyTile.getBlock() instanceof BlockConcretePowder;
				boolean flag1 = flag && this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER);
				double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
				if (flag && d0 > 1.0D)
				{
					RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), RayTraceFluidMode.SOURCE_ONLY);
					if (raytraceresult != null && this.world.getFluidState(raytraceresult.getBlockPos()).isTagged(FluidTags.WATER))
					{
						blockpos1 = raytraceresult.getBlockPos();
						flag1 = true;
					}
				}
				if (!this.onGround && !flag1)
				{
					if (this.flyTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.flyTime > 600)
					{
						if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) this.entityDropItem(block);
						this.remove();
					}
				}
				IBlockState iblockstate = this.world.getBlockState(blockpos1);
				if (this.world.isAirBlock(new BlockPos(this.posX, this.posY + (double)0.99F, this.posZ))) //Forge: Don't indent below.
				if (!flag1 && BlockFlying.canFlyThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY + (double)0.99F, this.posZ))))
				{
					this.onGround = false;
					return;
				}

				this.motionX *= (double)0.7F;
				this.motionZ *= (double)0.7F;
				this.motionY *= -0.2D;
				if (iblockstate.getBlock() != Blocks.MOVING_PISTON)
				{
					this.remove();
					if (!this.dontSetBlock)
					{
						if (iblockstate.getMaterial().isReplaceable() && (flag1 || !BlockFlying.canFlyThrough(this.world.getBlockState(blockpos1.up()))) && this.world.setBlockState(blockpos1, this.flyTile, 3))
						{
							if (block instanceof BlockFlying) ((BlockFlying)block).onEndFlying(this.world, blockpos1, this.flyTile, iblockstate);
							if (this.tileEntityData != null && this.flyTile.hasTileEntity())
							{
								TileEntity tileentity = this.world.getTileEntity(blockpos1);
								if (tileentity != null)
								{
									NBTTagCompound nbttagcompound = tileentity.write(new NBTTagCompound());
									for(String s : this.tileEntityData.keySet())
									{
										INBTBase inbtbase = this.tileEntityData.getTag(s);
										if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) nbttagcompound.setTag(s, inbtbase.copy());
									}
									tileentity.read(nbttagcompound);
									tileentity.markDirty();
								}
							}
						}
						else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) this.entityDropItem(block);
					}
					else if (block instanceof BlockFlying) ((BlockFlying)block).onBroken(this.world, blockpos1);
				}
			}
			this.motionX *= (double)0.98F;
			this.motionY *= (double)0.98F;
			this.motionZ *= (double)0.98F;
		}
	}

	public void fly(float distance, float damageMultiplier)
	{
		if (this.hurtEntities)
		{
			int i = MathHelper.ceil(distance + 1.0F);
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
					IBlockState iblockstate = BlockAnvil.damage(this.flyTile);
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

	}

	@Override
	protected void writeAdditional(NBTTagCompound compound)
	{
		compound.setTag("BlockState", NBTUtil.writeBlockState(this.flyTile));
		compound.setInt("Time", this.flyTime);
		compound.setBoolean("DropItem", this.shouldDropItem);
		compound.setBoolean("HurtEntities", this.hurtEntities);
		compound.setFloat("FlyHurtAmount", this.flyHurtAmount);
		compound.setInt("FlyHurtMax", this.flyHurtMax);
		if (this.tileEntityData != null)
		{
			compound.setTag("TileEntityData", this.tileEntityData);
		}
	}
	
	@Override
	protected void readAdditional(NBTTagCompound compound)
	{
		this.flyTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
		this.flyTime = compound.getInt("Time");
		if (compound.contains("HurtEntities", 99)) {
			this.hurtEntities = compound.getBoolean("HurtEntities");
			this.flyHurtMax = compound.getInt("FlyHurtMax");
		}
		else if (this.flyTile.isIn(BlockTags.ANVIL))
		{
			this.hurtEntities = true;
		}
		if (compound.contains("DropItem", 99)) {
			this.shouldDropItem = compound.getBoolean("DropItem");
		}
		if (compound.contains("TileEntityData", 10)) {
			this.tileEntityData = compound.getCompound("TileEntityData");
		}
		if (this.flyTile == Blocks.AIR) {
			this.flyTile = Blocks.SAND.getDefaultState();
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

	/**
	 * Return whether this entity should be rendered as on fire.
	 */
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
	
	public IBlockState getBlockState()
	{
		return this.flyTile;
	}

	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}
}
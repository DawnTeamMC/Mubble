package hugman.mod.objects.entity;

import javax.annotation.Nullable;

import hugman.mod.init.MubbleEntities;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCustomTNT extends Entity implements IEntityAdditionalSpawnData
{
	private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
	private static final DataParameter<Float> STRENGHT = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.FLOAT);
	private IBlockState customTile = Blocks.TNT.getDefaultState();
	private int fuse = 80;
	private float strenght = 4.0F;
	@Nullable
	private EntityLivingBase tntPlacedBy;
	
	public EntityCustomTNT(World worldIn)
	{
		super(MubbleEntities.CUSTOM_TNT, worldIn);
		this.preventEntitySpawning = true;
		this.isImmuneToFire = true;
		this.setSize(0.98F, 0.98F);
	}
	
	public EntityCustomTNT(IBlockState customTileIn, World worldIn, double x, double y, double z, @Nullable EntityLivingBase igniter)
	{
		this(worldIn);
		this.customTile = customTileIn;
		this.setPosition(x, y, z);
	    float f = (float)(Math.random() * (double)((float)Math.PI * 2F));
	    this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
	    this.motionY = (double)0.2F;
	    this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
	    this.setFuse(fuse);
	    this.setStrenght(strenght);
	    this.prevPosX = x;
	    this.prevPosY = y;
	    this.prevPosZ = z;
	    this.tntPlacedBy = igniter;
	}
	
	@Override
	protected void registerData()
	{
		this.dataManager.register(FUSE, fuse);
		this.dataManager.register(STRENGHT, strenght);
	}

	@Override
	public void tick()
	{
		if(this.customTile == Blocks.AIR) this.remove();
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (!this.hasNoGravity()) this.motionY -= (double)0.04F;
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if (this.onGround)
		{
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}
		--this.fuse;
		if (this.fuse <= 0)
		{
			this.remove();
			if (!this.world.isRemote) this.explode();
		}
		else
		{
			this.handleWaterMovement();
			this.world.spawnParticle(Particles.SMOKE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}
	
	private void explode()
	{
		this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, this.strenght, true);
	}
	
	@Override
	protected void writeAdditional(NBTTagCompound compound)
	{
		compound.setTag("BlockState", NBTUtil.writeBlockState(this.customTile));
		compound.setShort("Fuse", (short)this.getFuse());
		compound.setFloat("Strenght", (float)this.getStrenght());
	}
	
	@Override
	protected void readAdditional(NBTTagCompound compound)
	{
		this.customTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
		if (this.customTile == Blocks.AIR)
		{
			this.customTile = Blocks.TNT.getDefaultState();
		}
		this.setFuse(compound.getShort("Fuse"));
		this.setStrenght(compound.getFloat("Strenght"));
	}
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if(FUSE.equals(key)) this.fuse = this.getFuseDataManager();
	}

	public int getFuse()
	{
		return this.fuse;
	}
	
	public void setFuse(int fuseIn)
	{
		this.dataManager.set(FUSE, fuseIn);
		this.fuse = fuseIn;
	}
	
	public int getFuseDataManager()
	{
		return this.dataManager.get(FUSE);
	}
	
	public IBlockState getBlockState()
	{
		return this.customTile;
	}
	
	public void setStrenght(float strenghtIn)
	{
		this.dataManager.set(STRENGHT, strenghtIn);
		this.strenght = strenghtIn;
	}
	
	public float getStrenght()
	{
		return this.strenght;
	}
	  
	@Nullable
	public EntityLivingBase getTntPlacedBy()
	{
		return this.tntPlacedBy;
	}
	
	@Override
	public void fillCrashReport(CrashReportCategory category)
	{
		super.fillCrashReport(category);
		category.addDetail("Immitating BlockState", this.customTile.toString());
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(Block.getStateId(customTile));
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		this.customTile = Block.getStateById(additionalData.readInt());
	}
}
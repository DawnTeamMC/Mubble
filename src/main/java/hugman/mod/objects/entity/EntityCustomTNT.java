package hugman.mod.objects.entity;

import javax.annotation.Nullable;

import hugman.mod.init.MubbleEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCustomTNT extends Entity implements IEntityAdditionalSpawnData
{
	private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(TNTEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Float> STRENGHT = EntityDataManager.createKey(TNTEntity.class, DataSerializers.FLOAT);
	private BlockState customTile = Blocks.TNT.getDefaultState();
	private int fuse = 80;
	private float strenght = 4.0F;
	@Nullable
	private LivingEntity tntPlacedBy;
	
	public EntityCustomTNT(EntityType<? extends EntityCustomTNT> type, World worldIn)
	{
		super(type, worldIn);
		this.preventEntitySpawning = true;
	}
	
	public EntityCustomTNT(BlockState customTileIn, World worldIn, double x, double y, double z, @Nullable LivingEntity igniter)
	{
		this(MubbleEntities.CUSTOM_TNT, worldIn);
		this.customTile = customTileIn;
		this.setPosition(x, y, z);
	    float f = (float)(Math.random() * (double)((float)Math.PI * 2F));
	    this.setMotion((double)(-((float)Math.sin((double)f)) * 0.02F), (double)0.2F, (double)(-((float)Math.cos((double)f)) * 0.02F));
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
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (!this.hasNoGravity())
		{
			this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
		}
		this.move(MoverType.SELF, this.getMotion());
		this.setMotion(this.getMotion().scale(0.98D));
		if (this.onGround)
		{
			this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
		}

		--this.fuse;
		if (this.fuse <= 0)
		{
			this.remove();
			if (!this.world.isRemote)
			{
				this.explode();
			}
		}
		else
		{
			this.handleWaterMovement();
			this.world.addParticle(ParticleTypes.SMOKE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}
	
	private void explode()
	{
		this.world.createExplosion(this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, this.strenght, Explosion.Mode.BREAK);
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.put("BlockState", NBTUtil.writeBlockState(this.customTile));
		compound.putShort("Fuse", (short)this.getFuse());
		compound.putFloat("Strenght", (float)this.getStrenght());
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.customTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
		if (this.customTile.getBlock() == Blocks.AIR)
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
	
	public BlockState getBlockState()
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
	public LivingEntity getTntPlacedBy()
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
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return new SSpawnObjectPacket(this);
	}
}
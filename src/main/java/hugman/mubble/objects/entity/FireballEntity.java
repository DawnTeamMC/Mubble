package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireballEntity extends ThrownItemEntity
{
	public FireballEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public FireballEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.FIREBALL, owner, world);
	}
	
	public FireballEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.FIREBALL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.FIREBALL;
	}

	@Override
	protected void onCollision(HitResult result)
	{
		if(result.getType() == HitResult.Type.ENTITY)
		{
			Entity entity = ((EntityHitResult) result).getEntity();
			int i = entity instanceof BlazeEntity ? 0 : 3;
			entity.setOnFireFor(5);
			entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float) i);
		}
		
		if(result.getType() == HitResult.Type.BLOCK)
		{
			onBlockImpact(((BlockHitResult) result).getBlockPos());
		}
		
		if(!this.world.isClient)
		{
			this.world.sendEntityStatus(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		Entity entity = this.getOwner();
		return new EntitySpawnS2CPacket(this, entity == null ? 0 : entity.getEntityId());
	}
	
	private void onBlockImpact(BlockPos pos)
	{
		BlockState state = this.world.getBlockState(pos);
		if(state.getBlock().matches(BlockTags.ICE))
		{
			if(this.world.dimension.doesWaterVaporize())
			{
				this.world.removeBlock(pos, false);
			}
			else
			{
				this.world.setBlockState(pos, Blocks.WATER.getDefaultState());
				this.world.updateNeighbor(pos, Blocks.WATER, pos);
			}
		}
	}
}

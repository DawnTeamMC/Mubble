package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CloudFlowerBlock extends FlowerBlock
{
    public CloudFlowerBlock(Effect stewEffect, int stewEffectDuration, Block.Properties builder)
    {
        super(stewEffect, stewEffectDuration, builder);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	Vec3d vec3d = entityIn.getMotion();
    	if(entityIn instanceof LivingEntity)
    	{
    		LivingEntity entity = (LivingEntity)entityIn;
    		ItemStack armor = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
    		if(MubbleTags.Items.CROWNS.contains(armor.getItem()) && !entity.isSneaking())
    		{
        		if(!entity.isSprinting())
        		{
        			entity.setMotion(vec3d.x, (this.RANDOM.nextInt(31) + 40) / 100D, vec3d.z);
        		}
    			else
    			{
    				entity.setMotion(vec3d.x, 0.7D, vec3d.z);
    			}
        		entity.fallDistance = 0f;
    		}
    	}
    	if(entityIn instanceof ItemEntity)
    	{
    		ItemEntity entity = (ItemEntity)entityIn;
        	if(MubbleTags.Items.CROWNS.contains(entity.getItem().getItem()))
        	{
        		entity.setMotion(vec3d.x, 0.3D, vec3d.z);
        	}
        	if(MubbleTags.Items.WEIGHT_LIGHT.contains(entity.getItem().getItem()))
        	{
        		entity.setMotion(vec3d.x, 0.1D, vec3d.z);
        	}
    	}
    }
}

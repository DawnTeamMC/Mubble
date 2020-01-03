package hugman.mubble.objects.block;

import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CloudBlock extends AbstractGlassBlock implements IBeaconBeamColorProvider
{
	private final DyeColor color;
	
    public CloudBlock(DyeColor colorIn)
    {
        super(Properties.create(Material.PLANTS, colorIn).sound(SoundType.CLOTH).hardnessAndResistance(0f).doesNotBlockMovement());
    	this.color = colorIn;
    }

	@Override
	public DyeColor getColor()
	{
		return this.color;
	}
    
    @Override
    public PushReaction getPushReaction(BlockState state)
    {
    	return PushReaction.DESTROY;
    }
    
    @Override
    public boolean isSolid(BlockState state)
    {
    	return false;
    }
    
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
       return false;
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

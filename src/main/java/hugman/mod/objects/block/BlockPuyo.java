package hugman.mod.objects.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockPuyo extends BlockDirectional
{
    public BlockPuyo(EnumDyeColor color)
    {
        super(color.getTranslationKey() + "_puyo", Properties.create(Material.CLAY, color).slipperiness(0.8F).sound(SoundType.SLIME), ItemGroup.DECORATIONS);
    }
    
    public BlockPuyo(String color)
    {
        super(color + "_puyo", Properties.create(Material.CLAY).slipperiness(0.8F).sound(SoundType.SLIME), ItemGroup.DECORATIONS);
    }
    
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	if (entityIn.isSneaking()) super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    	else entityIn.fall(fallDistance, 0.0F);
    }
    
    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
    	if (entityIn.isSneaking()) super.onLanded(worldIn, entityIn);
    	else if (entityIn.motionY < 0.0D)
    	{
    		entityIn.motionY = -entityIn.motionY;
    		if (!(entityIn instanceof EntityLivingBase)) entityIn.motionY *= 0.8D;
        }
	}
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.isSneaking())
        {
        	double d0 = 0.4D + Math.abs(entityIn.motionY) * 0.2D;
            entityIn.motionX *= d0;
            entityIn.motionZ *= d0;
        }
    }
}

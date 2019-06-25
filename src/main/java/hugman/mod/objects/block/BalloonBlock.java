package hugman.mod.objects.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BalloonBlock extends FlyingBlock
{
    public BalloonBlock(DyeColor color)
    {
        super(Properties.create(Material.WOOL, color).hardnessAndResistance(0F).sound(SoundType.CLOTH));
    }
    
    @Override
	public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
	}
    
    @OnlyIn(Dist.CLIENT)
    public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return 1.0F;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
	}

    public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
    {
       return false;
    }
}

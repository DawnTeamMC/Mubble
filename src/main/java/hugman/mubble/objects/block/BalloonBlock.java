package hugman.mubble.objects.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BalloonBlock extends FlyingBlock
{
    public BalloonBlock(DyeColor color)
    {
        super(Properties.create(Material.WOOL, color).hardnessAndResistance(0F).sound(SoundType.CLOTH).nonOpaque());
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state1, BlockState state2, Direction direction)
    {
       return state2.getBlock() == this ? true : false;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return 1.0F;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
	}

    @Override
    public boolean canSuffocate(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
    {
       return false;
    }
}

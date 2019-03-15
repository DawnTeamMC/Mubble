package hugman.mod.objects.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockBalloon extends BlockFlying
{
    public BlockBalloon(EnumDyeColor color)
    {
        super(color.getTranslationKey() + "_balloon", Properties.create(Material.CLOTH, color).hardnessAndResistance(0F).sound(SoundType.CLOTH));
    }
    
    @Override
    public boolean propagatesSkylightDown(IBlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
	}

    @Override
	public int quantityDropped(IBlockState state, Random random)
    {
    	return 0;
	}
    
    @Override
	public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
	}
    
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
     
	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
}

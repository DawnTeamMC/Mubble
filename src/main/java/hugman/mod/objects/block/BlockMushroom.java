package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Reference;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockMushroom extends net.minecraft.block.BlockMushroom
{
    public BlockMushroom(EnumDyeColor color)
    {
        super(Properties.create(Material.PLANTS, color).doesNotBlockMovement().hardnessAndResistance(0).needsRandomTick().sound(SoundType.PLANT));
        setRegistryName(Reference.MOD_ID, color.getTranslationKey() + "_mushroom");
        MubbleBlocks.register(this);
    }
    
    public BlockMushroom(String color, int light)
    {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).needsRandomTick().sound(SoundType.PLANT).lightValue(light));
        setRegistryName(Reference.MOD_ID, color + "_mushroom");
        MubbleBlocks.register(this);
    }
    
    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
    	return false;
    }
    
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	return;
    }
    
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
    	return false;
    }
    
    @Override
    public boolean generateBigMushroom(IWorld worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	return false;
    }
}

package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class EmptyBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public EmptyBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.5F, 6.0F));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onBlockAdded(BlockState p_220082_1_, World worldIn, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_)
    {
    	if (!worldIn.isRemote && worldIn.isBlockPowered(pos)) playSound(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_)
    {
    	if (!worldIn.isRemote && worldIn.isBlockPowered(pos)) playSound(worldIn, pos);
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos, p_220069_6_);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(!worldIn.isRemote && entityIn.getMotion().getY() > 0.0D)
    	{
        	Random rand = new Random();
        	playSound(worldIn, pos);
    		if(rand.nextInt(15) == 0) worldIn.destroyBlock(pos, true);
    	}
    }
    
    public void playSound(World worldIn, BlockPos pos)
    {
    	worldIn.playSound((PlayerEntity)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, MubbleSounds.BLOCK_EMPTY_BLOCK_HIT, SoundCategory.BLOCKS, 1f, 1f);
    }
}

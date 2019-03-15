package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockEmpty extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public BlockEmpty()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.5F, 6.0F));
        setRegistryName(Mubble.MOD_ID, "empty_block");
        MubbleBlocks.register(this);
    }
    
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return SHAPE;
    }
    
    @Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
    {
    	if (!worldIn.isRemote && worldIn.isBlockPowered(pos)) playSound(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (!worldIn.isRemote && worldIn.isBlockPowered(pos)) playSound(worldIn, pos);
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(!worldIn.isRemote && entityIn.motionY > 0.0D)
    	{
        	Random rand = new Random();
        	playSound(worldIn, pos);
    		if(rand.nextInt(15) == 0) worldIn.destroyBlock(pos, true);
    	}
    }
    
    public void playSound(World worldIn, BlockPos pos)
    {
    	worldIn.playSound((EntityPlayer)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, MubbleSounds.BLOCK_EMPTY_BLOCK_HIT, SoundCategory.BLOCKS, 1f, 1f);
    }
}

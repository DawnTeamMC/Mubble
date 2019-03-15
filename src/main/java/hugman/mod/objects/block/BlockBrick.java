package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSoundTypes;
import hugman.mod.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBrick extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public BlockBrick(String name)
    {
        super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.0F, 6.0F).sound(MubbleSoundTypes.BRICK_BLOCK));
        setRegistryName(Mubble.MOD_ID, name);
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
    	if (worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (worldIn.isBlockPowered(pos)) loot(worldIn, pos);
    }
    
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
		if(!worldIn.isRemote && entityIn.motionY > 0.0D)
		{
			if(this == MubbleBlocks.BRICK_BLOCK)
			{
				Random rand = new Random();
	            switch (rand.nextInt(2))
	            {
	            	case 0: loot(worldIn, pos);
	    					break;
	            	case 1: worldIn.destroyBlock(pos, false);
							break;
	            }
			}
			else if (this == MubbleBlocks.GOLDEN_BRICK_BLOCK) loot(worldIn, pos);
		}
    }
    
    public void loot(World worldIn, BlockPos pos)
	{
        IBlockState empty_block = MubbleBlocks.EMPTY_BLOCK.getDefaultState();
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D + 0.6D;
        final double z = pos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
        	if(this == MubbleBlocks.BRICK_BLOCK) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN)));
        	if(this == MubbleBlocks.GOLDEN_BRICK_BLOCK) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN, rand.nextInt(5) + 3)));
            worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, MubbleSounds.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlockState(pos, empty_block);
        }
	}
}

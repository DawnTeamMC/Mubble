package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSoundTypes;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrick extends BlockBase implements IHasModel
{
	private static final AxisAlignedBB BRICK_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.05D, 0.0D, 1.0D, 1.0D, 1.0D);
	
	/** 
	 * Open class - can be initialized for multiple blocks with variables.
	 */
	public BlockBrick(String name)
	{
		super(name, Material.ROCK, 1.5f, 20f, MubbleSoundTypes.BRICK_BLOCK);
	}
	
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return BRICK_BLOCK_AABB;
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(this == MubbleBlocks.BRICK_BLOCK)
		{
			int drop = rand.nextInt(3);
			if(drop == 2) return MubbleItems.YELLOW_COIN;
			else return Item.getItemFromBlock(this);
		}
		if(this == MubbleBlocks.GOLDEN_BRICK_BLOCK) return MubbleItems.YELLOW_COIN;
		else return Item.getItemFromBlock(this);
	}
	
	@Override
	public int quantityDropped(Random rand)
	{
		if(this == MubbleBlocks.GOLDEN_BRICK_BLOCK) return rand.nextInt(4) + 1;
		else return 1;
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos blockpos, IBlockState state, Entity entityIn)
	{
		if(!worldIn.isRemote && entityIn.motionY > 0.0D)
		{
			worldIn.destroyBlock(blockpos, true);
		}
	}
}

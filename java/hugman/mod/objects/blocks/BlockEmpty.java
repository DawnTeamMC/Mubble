package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BlockInit;
import hugman.mod.init.ItemInit;
import hugman.mod.init.SoundTypeInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEmpty extends BlockBase implements IHasModel
{
	private static final AxisAlignedBB EMPTY_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.05D, 0.0D, 1.0D, 1.0D, 1.0D);
	
	/** 
	 * Open class - can be initialized for multiple items with variables.
	 */
	public BlockEmpty()
	{
		super("empty_block", Material.IRON, 1.5f, 30f, SoundType.METAL);
	}
	
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return EMPTY_BLOCK_AABB;
    }
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos blockpos, IBlockState state, Entity entityIn)
	{
		Random rand = new Random();
        final double x = blockpos.getX() + 0.5D;
        final double y = blockpos.getY() + 0.5D;
        final double z = blockpos.getZ() + 0.5D;
		if(!worldIn.isRemote && entityIn.motionY > 0.0D)
		{
			worldIn.playSound((EntityPlayer)null, x, y, z, SoundHandler.BLOCK_EMPTY_BLOCK_BREAK, SoundCategory.BLOCKS, 1f, 1f);
			if(rand.nextInt(15) == 0) worldIn.destroyBlock(blockpos, true);
		}
	}
}

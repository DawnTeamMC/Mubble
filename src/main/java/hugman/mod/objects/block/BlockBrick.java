package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleSoundTypes;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
    
	public void loot(World worldIn, BlockPos blockpos, IBlockState state)
	{
        IBlockState empty_block = MubbleBlocks.EMPTY_BLOCK.getDefaultState();
        final double x = blockpos.getX() + 0.5D;
        final double y = blockpos.getY() + 0.5D + 0.6D;
        final double z = blockpos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
        	if(this == MubbleBlocks.BRICK_BLOCK) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN)));
        	if(this == MubbleBlocks.GOLDEN_BRICK_BLOCK) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(MubbleItems.YELLOW_COIN, rand.nextInt(5) + 3)));
            worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, SoundHandler.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlockState(blockpos, empty_block);
        }
        else
        {
        	Random rand = new Random();
        	for (int i = 0; i < rand.nextInt(3) + 1; i++)
        	{
            	worldIn.spawnParticle(EnumParticleTypes.CRIT, x + (rand.nextInt(7) - 3) / 10D, y + 0.3D, z + (rand.nextInt(7) - 3) / 10D, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D, 0);
            }
        }
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos blockpos, IBlockState state, Entity entityIn)
	{
		if(!worldIn.isRemote && entityIn.motionY > 0.0D)
		{
            Random rand = new Random();
            switch (rand.nextInt(2))
            {
            	case 0: loot(worldIn, blockpos, state);
    					break;
            	case 1: worldIn.destroyBlock(blockpos, false);
						break;
            }
		}
	}
}

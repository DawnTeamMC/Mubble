package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.init.BlockInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuestion extends BlockBase implements IHasModel
{   
	private static final AxisAlignedBB QUESTION_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.05D, 0.0D, 1.0D, 1.0D, 1.0D);

	/**
	 * Static class - can only be initialized once.
	 */
	public BlockQuestion()
	{
		super("question_block", Material.IRON, 1.5f, 30f, SoundType.METAL);
	}
	
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return QUESTION_BLOCK_AABB;
    }
	
	public void loot(World worldIn, BlockPos blockpos, IBlockState state)
	{
        IBlockState empty_block = BlockInit.EMPTY_BLOCK.getDefaultState();
        final double x = blockpos.getX() + 0.5D;
        final double y = blockpos.getY() + 0.5D + 0.6D;
        final double z = blockpos.getZ() + 0.5D;
        if (!worldIn.isRemote)
        {
        	Random rand = new Random();
    		int loot = rand.nextInt(11);
        	if (loot >= 0 && loot <= 3) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.YELLOW_COIN, rand.nextInt(12) + 1)));
            else if (loot >= 3 && loot <= 7 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.RED_COIN, rand.nextInt(2) + 1)));
            else if (loot == 8 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.BLUE_COIN)));
            if (loot >= 0 && loot <= 8) worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, SoundHandler.BLOCK_QUESTION_BLOCK_LOOT_COIN, SoundCategory.BLOCKS, 1f, 1f);
            else if (loot >= 9)
            {
        		loot = rand.nextInt(7);
        		if (loot >= 0 && loot <= 2 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.SUPER_MUSHROOM)));
        		else if (loot >= 3 && loot <= 5 ) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(ItemInit.CAPE_FEATHER)));
        		else if (loot == 6) worldIn.spawnEntity(new EntityItem(worldIn, x, y, z, new ItemStack(CostumeInit.SUPER_CROWN)));
            	worldIn.playSound((EntityPlayer)null, x, y - 0.6D, z, SoundHandler.BLOCK_QUESTION_BLOCK_LOOT_POWER_UP, SoundCategory.BLOCKS, 1f, 1f);
            }
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
    public void onBlockAdded(World worldIn, BlockPos blockpos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.isBlockPowered(blockpos))
            {
            	this.loot(worldIn, blockpos, state);
            }
        }
    }
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos blockpos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.isBlockPowered(blockpos))
            {
            	loot(worldIn, blockpos, state);
                worldIn.scheduleUpdate(blockpos, this, 2);
            }
        }
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockpos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote)
		{
			loot(worldIn, blockpos, state);
		}
        return true;
    }
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos blockpos, IBlockState state, Entity entityIn)
	{
		if(!worldIn.isRemote && entityIn.motionY > 0.0D)
		{
			loot(worldIn, blockpos, state);
		}
	}
}

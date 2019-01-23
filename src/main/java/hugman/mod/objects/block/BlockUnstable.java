package hugman.mod.objects.block;

import java.util.Timer;
import java.util.TimerTask;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnstable extends BlockBase implements IHasModel
{
	private Timer timer = new Timer();
	private TimerTask destroyTask, placeTask, preSoundTask;

    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockUnstable(String name, Material material, float hardness, float resistance, SoundType sound, int light)
	{
		super(name, material, hardness, resistance, sound, light);
	}
	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockUnstable(String name, Material material, float hardness, float resistance, SoundType sound)
	{
		super(name, material, hardness, resistance, sound);
	}
	
	public void destroy(World worldIn, BlockPos pos)
    {
		destroyTask = new TimerTask()
		{
			@Override
			public void run()
			{
				worldIn.destroyBlock(pos, false);
			}
		};
		timer.schedule(destroyTask, 200);
    }
	
	public void rewind(World worldIn, BlockPos pos)
	{
		IBlockState block = worldIn.getBlockState(pos);
		double x = pos.getX() + 0.5D;
		double y = pos.getY() + 0.5D;
		double z = pos.getZ() + 0.5D;
		worldIn.setBlockToAir(pos);
		worldIn.playSound((EntityPlayer)null, x, y, z, SoundHandler.BLOCK_REWIND_BLOCK_DISAPPEAR, SoundCategory.BLOCKS, 1f, 1f);
		preSoundTask = new TimerTask()
		{
			@Override
			public void run()
			{
            	worldIn.playSound((EntityPlayer)null, x, y, z, SoundHandler.BLOCK_REWIND_BLOCK_REAPPEAR, SoundCategory.BLOCKS, 1f, 1f);
			}
		};
		placeTask = new TimerTask()
		{
			@Override
			public void run()
			{
				worldIn.destroyBlock(pos, true);
				worldIn.setBlockState(pos, block);
			}
		};
		timer.schedule(preSoundTask, 1800);
		timer.schedule(placeTask, 2000);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        entityIn.fall(fallDistance, 1.0F);
		if(!worldIn.isRemote && this == MubbleBlocks.UNSTABLE_STONE) destroy(worldIn, pos);
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
		if(!worldIn.isRemote && this == MubbleBlocks.UNSTABLE_STONE) destroy(worldIn, pos);
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (this == MubbleBlocks.REWIND_BLOCK)
		{
			if (!worldIn.isRemote) rewind(worldIn, pos);
			return true;
		}
		return false;
	}
	
	@Override
    public void onBlockAdded(World worldIn, BlockPos blockpos, IBlockState state)
    {
        if (!worldIn.isRemote && this == MubbleBlocks.REWIND_BLOCK)
        {
            if (worldIn.isBlockPowered(blockpos))
            {
            	rewind(worldIn, blockpos);
            }
        }
    }
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos blockpos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote && this == MubbleBlocks.REWIND_BLOCK)
        {
            if (worldIn.isBlockPowered(blockpos))
            {
            	rewind(worldIn, blockpos);
                worldIn.scheduleUpdate(blockpos, this, 2);
            }
        }
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        if (this == MubbleBlocks.UNSTABLE_STONE) return BlockRenderLayer.CUTOUT_MIPPED;
        else return BlockRenderLayer.SOLID;
    }
}

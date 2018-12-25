package hugman.mod.objects.blocks;

import java.util.Timer;
import java.util.TimerTask;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUnstable extends BlockBase implements IHasModel
{
	private Timer timer = new Timer();
	private TimerTask destroyTask;
	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockUnstable(String name)
	{
		super(name, Material.ROCK, 0.1f, 1f, SoundType.STONE);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		destroy(worldIn, pos);
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
		destroy(worldIn, pos);
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
		if(!worldIn.isRemote) timer.schedule(destroyTask, 100);
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}

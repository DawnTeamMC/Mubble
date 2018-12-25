package hugman.mod.objects.blocks;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hugman.mod.init.BlockInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import io.netty.util.Timeout;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
		super(name, Material.ROCK, 0.1f, 10f, SoundType.STONE);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		if(!worldIn.isRemote) timer.schedule(destroyTask, 5);
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
		if(!worldIn.isRemote) timer.schedule(destroyTask, 5);
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
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}

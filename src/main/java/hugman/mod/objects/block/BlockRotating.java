package hugman.mod.objects.block;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotating extends BlockBase implements IHasModel
{   
	/**
	 * Static class - can only be initialized once.
	 */
	public BlockRotating()
	{
		super("rotating_block", Material.ROCK, 1.5f, 20f, SoundType.STONE);
	}

	@Override
    public void onLanded(World worldIn, Entity entityIn)
    {
		if(entityIn.isSneaking() && entityIn.motionY < -0.1)
		{
			if(!worldIn.isRemote)  worldIn.destroyBlock(new BlockPos(entityIn).down(), false);
			entityIn.motionY = 0.625D;
		}
    }
}

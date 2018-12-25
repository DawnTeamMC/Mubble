package hugman.mod.objects.blocks;

import java.util.Random;

import hugman.mod.init.BlockInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUnstable extends BlockBase implements IHasModel
{
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
		worldIn.destroyBlock(pos, false);
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
		worldIn.destroyBlock(pos, false);
    }
}

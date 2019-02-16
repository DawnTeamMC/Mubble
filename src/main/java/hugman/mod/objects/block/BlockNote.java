package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
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

public class BlockNote extends BlockBase implements IHasModel
{	
    /** 
     * Open class - can be initialized for multiple items with variables.
     */
	public BlockNote(String name)
	{
		super(name, Material.ROCK, 1.4f, 10f, SoundType.STONE);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        entityIn.fall(fallDistance, 0.0F);
    }
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
		if (Math.abs(entityIn.motionY) < 0.45D) entityIn.motionY = 0.5D;
        super.onEntityWalk(worldIn, pos, entityIn);
    }

	@Override
    public void onLanded(World worldIn, Entity entityIn)
    {
		launch(worldIn, entityIn);
	}
	
	public void launch(World worldIn, Entity entityIn)
    {
		BlockPos pos = new BlockPos(entityIn).down();
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D;
        final double z = pos.getZ() + 0.5D;
        Random rand = new Random();
        if(entityIn instanceof EntityLivingBase)
        {
        	if(!entityIn.isSneaking())
    		{
    	        worldIn.playSound((EntityPlayer)null, x, y, z, SoundHandler.BLOCK_NOTE_BLOCK_JUMP_HIGH, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(5) + 1; i++) {
    	        	worldIn.spawnParticle(EnumParticleTypes.NOTE, x + (rand.nextInt(7) - 3) / 10D, y + 0.6D, z + (rand.nextInt(7) - 3) / 10D, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D, 0);
            	}
    			if(this == MubbleBlocks.NOTE_BLOCK) entityIn.motionY = 0.9D;
    			if(this == MubbleBlocks.SUPER_NOTE_BLOCK) entityIn.motionY = 1.5D;
    		}
    		else if(entityIn.isSneaking())
    		{
    			worldIn.playSound((EntityPlayer)null, x, y, z, SoundHandler.BLOCK_NOTE_BLOCK_JUMP_SMALL, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(1) + 1; i++) {
    	        	worldIn.spawnParticle(EnumParticleTypes.NOTE, x, y + 0.6D, z, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D, 0);
            	}
    	        entityIn.motionY = 0.5D;
    		}
    		else
    		{
    			entityIn.motionY = 0D;
    		}
        }
	}
}

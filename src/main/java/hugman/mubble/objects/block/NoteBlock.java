package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class NoteBlock extends Block
{    
    public NoteBlock(Properties builder)
    {
        super(builder);
    }
    
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	entityIn.fall(fallDistance, 0.0F);
    }
    
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
    	if (Math.abs(entityIn.getMotion().getY()) < 0.45D) entityIn.setMotion(entityIn.getMotion().getX(), 0.5D, entityIn.getMotion().getZ());
    }
    
    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn)
    {
    	launch(entityIn.world, entityIn);
    }
    
    public void launch(World worldIn, Entity entityIn)
    {
    	Vec3d vec3d = entityIn.getMotion();
        if(entityIn instanceof LivingEntity)
        {
            SoundEvent lowJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_SMB;
            SoundEvent highJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_SMB;
    		if(this == MubbleBlocks.SMB_NOTE_BLOCK || this == MubbleBlocks.SMB_SUPER_NOTE_BLOCK)
    		{
    			lowJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_SMB;
    			highJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_SMB;
    		}
    		else if(this == MubbleBlocks.SMB3_NOTE_BLOCK || this == MubbleBlocks.SMB3_SUPER_NOTE_BLOCK)
    		{
    			
    		}
    		else if(this == MubbleBlocks.SMW_NOTE_BLOCK || this == MubbleBlocks.SMW_SUPER_NOTE_BLOCK)
    		{
    			
    		}
    		else if(this == MubbleBlocks.NSMBU_NOTE_BLOCK || this == MubbleBlocks.NSMBU_SUPER_NOTE_BLOCK)
    		{
    			lowJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_LOW_NSMBU;
    			highJumpSound = MubbleSounds.BLOCK_NOTE_BLOCK_JUMP_HIGH_NSMBU;
    		}
        	BlockPos pos = new BlockPos(entityIn).down();
            final double x = pos.getX() + 0.5D;
            final double y = pos.getY() + 0.5D;
            final double z = pos.getZ() + 0.5D;
            Random rand = new Random();
        	if(!entityIn.isSneaking())
    		{
    	        worldIn.playSound((PlayerEntity)null, x, y, z, highJumpSound, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(5) + 1; i++)
    	        {
    	        	worldIn.addParticle(ParticleTypes.NOTE, x + (rand.nextInt(7) - 3) / 10D, y + 0.6D, z + (rand.nextInt(7) - 3) / 10D, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
            	}
    			entityIn.setMotion(vec3d.x, getProperLaunchMotion(), vec3d.z);
    		}
    		else
    		{
    			worldIn.playSound((PlayerEntity)null, x, y, z, lowJumpSound, SoundCategory.BLOCKS, 1f, 1f);
    	        for (int i = 0; i < rand.nextInt(1) + 1; i++)
    	        {
    	        	worldIn.addParticle(ParticleTypes.NOTE, x, y + 0.6D, z, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
            	}
    	        entityIn.setMotion(vec3d.x, 0.5D, vec3d.z);
    		}
        }
    }
    
    public double getProperLaunchMotion()
    {
    	return 0.9D;
	}
}

package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EmptyBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public EmptyBlock()
    {
        super(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(1.5F, 6.0F));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(!worldIn.isClient && entityIn.getVelocity().getY() > 0.0D)
    	{
            SoundEvent hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMB;
    		if(this == MubbleBlocks.SMB_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMB;
    		}
    		else if(this == MubbleBlocks.SMB3_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMB3;
    		}
    		else if(this == MubbleBlocks.SMW_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMW;
    		}
    		else if(this == MubbleBlocks.NSMBU_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_NSMBU;
    		}
    		worldIn.playSound((PlayerEntity) null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, hitSound, SoundCategory.BLOCKS, 1f, 1f);
    	}
    }
}

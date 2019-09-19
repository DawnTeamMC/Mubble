package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class EmptyBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public EmptyBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.5F, 6.0F));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
    	if(!worldIn.isRemote && entityIn.getMotion().getY() > 0.0D)
    	{
            SoundEvent hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMB;
    		if(this == MubbleBlocks.SMB_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_SMB;
    		}
    		else if(this == MubbleBlocks.SMB3_EMPTY_BLOCK)
    		{
    			
    		}
    		else if(this == MubbleBlocks.SMW_EMPTY_BLOCK)
    		{
    			
    		}
    		else if(this == MubbleBlocks.NSMBU_EMPTY_BLOCK)
    		{
    			hitSound = MubbleSounds.BLOCK_EMPTY_BLOCK_HIT_NSMBU;
    		}
    		worldIn.playSound((PlayerEntity)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, hitSound, SoundCategory.BLOCKS, 1f, 1f);
    	}
    }
}

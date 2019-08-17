package hugman.mubble.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class RotatingBlock extends Block
{	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public RotatingBlock()
    {
        super(Properties.from(Blocks.STONE));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn)
    {
    	Vec3d vec3d = entityIn.getMotion();
    	if(entityIn.isSneaking() && vec3d.y < -0.1)
    	{
    		if(!entityIn.world.isRemote) entityIn.world.destroyBlock(new BlockPos(entityIn).down(), false);
    		entityIn.setMotion(vec3d.x, 0.625D, vec3d.z);
    	}
    	else super.onLanded(worldIn, entityIn);
    }
    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_)
    {
		if(!worldIn.isRemote && worldIn.isBlockPowered(pos))
		{
			worldIn.destroyBlock(pos, false);
		}
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
		if(!worldIn.isRemote && entityIn.getMotion().y > 0.0D)
		{
			worldIn.destroyBlock(pos, false);
		}
    }
}

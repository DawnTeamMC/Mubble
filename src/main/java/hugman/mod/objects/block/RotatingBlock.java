package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;

public class RotatingBlock extends Block
{	
    public RotatingBlock()
    {
        super(Properties.from(Blocks.STONE));
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
}

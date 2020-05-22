package hugman.mubble.objects.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RotatingBlock extends Block
{	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);
	
    public RotatingBlock(BlockSoundGroup soundType)
    {
        super(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).sounds(soundType));
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public void onEntityLand(BlockView worldIn, Entity entityIn)
    {
    	Vec3d vec3d = entityIn.getVelocity();
    	if(entityIn.isSneaking() && vec3d.y < -0.1)
    	{
    		if(!entityIn.world.isClient) entityIn.world.removeBlock(new BlockPos(entityIn).down(), false);
    		entityIn.setVelocity(vec3d.x, 0.625D, vec3d.z);
    	}
    	else super.onEntityLand(worldIn, entityIn);
    }
    
    @Override
    public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean moved)
    {
		if(!worldIn.isClient && worldIn.isReceivingRedstonePower(pos))
		{
			worldIn.removeBlock(pos, false);
		}
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
		if(!worldIn.isClient && entityIn.getVelocity().y > 0.0D)
		{
			worldIn.removeBlock(pos, false);
		}
    }
}

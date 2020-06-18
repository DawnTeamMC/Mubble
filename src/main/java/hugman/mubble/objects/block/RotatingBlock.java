package hugman.mubble.objects.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RotatingBlock extends Block {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.05D, 0.0D, 16.0D, 16.0D, 16.0D);

	public RotatingBlock(BlockSoundGroup soundType) {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).sounds(soundType));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		if(entity.isSneaking() && vec3d.y < -0.1) {
			if(!entity.world.isClient) {
				entity.world.removeBlock(entity.getBlockPos().down(), false);
			}
			entity.setVelocity(vec3d.x, 0.625D, vec3d.z);
		}
		else {
			super.onEntityLand(world, entity);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
		if(!world.isClient && world.isReceivingRedstonePower(pos)) {
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient && entity.getVelocity().y > 0.0D) {
			world.removeBlock(pos, false);
		}
	}
}

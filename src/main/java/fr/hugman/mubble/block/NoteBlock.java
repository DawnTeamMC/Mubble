package fr.hugman.mubble.block;

import fr.hugman.mubble.block.bump.BumpConfig;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Napero
 * @author Hugman
 * @since v4.0.0
 */
public class NoteBlock extends MarioBumpableBlock {
	private final SoundEvent lowJumpSound;
	private final SoundEvent highJumpSound;

	public NoteBlock(SoundEvent lowJumpSound, SoundEvent highJumpSound, Settings settings) {
		super(BumpConfig.NOTHING, settings);
		this.lowJumpSound = lowJumpSound;
		this.highJumpSound = highJumpSound;
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		// Do not apply fall damage
	}

	@Override
	public void onEntityLand(BlockView view, Entity entity) {
		// TODO: make a new interface for falling hittable blocks
		World world = entity.getEntityWorld();
		if(world.isClient()) {
			return;
		}

		BlockPos pos = entity.getBlockPos().down();
		BlockState state = world.getBlockState(pos);

		this.onHit(world, pos, state, entity, new BlockHitResult(entity.getPos(), Direction.UP, pos, false));
	}

	@Override
	public void onBump(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		// Only play the sound if the block is not going to bounce up
		if(blockEntity.getBumpDirection() != Direction.DOWN) {
			this.playGenericBumpSound(blockEntity);
		}
	}

	@Override
	public void onBumpMiddle(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		if(blockEntity.getWorld() != null && blockEntity.getBumpDirection() == Direction.DOWN) {
			this.launchEntitiesOnTop(blockEntity.getWorld(), blockEntity.getPos());
		}
		super.onBumpMiddle(world, pos, state, blockEntity);
	}

	@Override
	public void launchEntitiesOnTop(World world, BlockPos pos) {
		super.launchEntitiesOnTop(world, pos);
		// TODO: add falling blocks

		// Only play high sound if all entities are sneaking
		boolean shouldPlayHighSound = false;
		List<Entity> entities = world.getOtherEntities(null, new Box(pos.up()));
		for(Entity entity : entities) {
			if(!entity.isSneaking()) {
				shouldPlayHighSound = true;
				break;
			}
		}
		Vec3d center = pos.toCenterPos();
		this.addParticles(world, pos);
		world.playSound(null, center.getX(), center.getY(), center.getZ(), shouldPlayHighSound ? this.highJumpSound : this.lowJumpSound, SoundCategory.BLOCKS, 1F, 1F);
	}

	public void launchEntity(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		entity.setVelocity(vec3d.x, entity.isSneaking() ? 0.5D : 0.9D, vec3d.z);
		entity.velocityDirty = true;
	}

	public void addParticles(World world, BlockPos blockPos) {
		Vec3d center = blockPos.toCenterPos();
		Random random = world.getRandom();
		for(int i = 0; i < random.nextInt(5) + 1; i++) {
			double x = center.getX() + (random.nextInt(7) - 3) / 10D;
			double y = center.getY() + 0.6F;
			double z = center.getZ() + (random.nextInt(7) - 3) / 10D;
			double color = random.nextInt(2) * 0.2D + 0.1D;
			world.addParticle(ParticleTypes.NOTE, x, y, z, color, 1.0D, 1.0D);
		}
	}
}

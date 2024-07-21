package fr.hugman.mubble.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.registry.MubbleSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Generic bumpable block with an additional sound effect when bumped.
 * <p>It also launches entities on top of the block when bumped from the bottom.
 *
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public class DecoratedBumpableBlock extends BumpableBlock {
	public static final MapCodec<DecoratedBumpableBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			BlockState.CODEC.fieldOf("default_bumped_state").forGetter((block) -> block.defaultBumpedState),
			createSettingsCodec()
	).apply(instance, DecoratedBumpableBlock::new));

	public DecoratedBumpableBlock(@Nullable BlockState defaultBumpedState, Settings settings) {
		super(defaultBumpedState, settings);
	}

	@Override
	protected MapCodec<? extends DecoratedBumpableBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void onBump(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		super.onBump(world, pos, state, blockEntity);
		this.playGenericBumpSound(blockEntity);
	}

	@Override
	public void onBumpMiddle(World world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
		super.onBumpMiddle(world, pos, state, blockEntity);
		if(blockEntity.getWorld() != null && blockEntity.getBumpDirection() == Direction.UP) {
			this.launchEntitiesOnTop(blockEntity.getWorld(), blockEntity.getPos());
		}
	}

	public void playGenericBumpSound(BumpableBlockEntity entity) {
		World world = entity.getWorld();
		Vec3d pos = entity.getPos().toCenterPos();
		if(world != null) {
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MubbleSounds.BUMPABLE_BLOCK_BUMP, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	/**
	 * Launches entities on top of the block.
	 */
	public void launchEntitiesOnTop(World world, BlockPos pos) {
		List<Entity> entities = world.getOtherEntities(null, new Box(pos.up()));
		for(Entity entity : entities) {
			launchEntity(entity);
		}
	}

	public void launchEntity(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		entity.setVelocity(vec3d.x, 0.3D, vec3d.z);
		entity.velocityDirty = true;
		// TODO: add a damage type and a gamerule for harming entities
	}
}

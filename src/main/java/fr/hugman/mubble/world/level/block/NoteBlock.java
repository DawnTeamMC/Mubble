package fr.hugman.mubble.world.level.block;

import fr.hugman.mubble.world.level.block.entity.BumpableBlockEntity;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * @author Napero
 * @author Hugman
 * @since v4.0.0
 */
public class NoteBlock extends DecoratedBumpableBlock {
    private final SoundEvent lowJumpSound;
    private final SoundEvent highJumpSound;

    public NoteBlock(SoundEvent lowJumpSound, SoundEvent highJumpSound, Properties settings) {
        super(null, settings);
        this.lowJumpSound = lowJumpSound;
        this.highJumpSound = highJumpSound;
    }

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		// No fall damage
	}

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter view, Entity entity) {
        // TODO: make a new interface for falling hittable blocks
        Level level = entity.level();
        if (level.isClientSide()) {
            super.updateEntityMovementAfterFallOn(view, entity);
        }

        BlockPos pos = entity.blockPosition().below();
        BlockState state = level.getBlockState(pos);

        this.onHit(level, state, entity, new BlockHitResult(entity.position(), Direction.UP, pos, false));

        super.updateEntityMovementAfterFallOn(view, entity);
    }

	@Override
	public void playGenericBumpSound(BumpableBlockEntity entity) {
		// Only play the sound if the block is not going to bounce up
		if (entity.getBumpDirection() == Direction.DOWN) {
			return;
		}
		super.playGenericBumpSound(entity);
	}

	@Override
    public void onBumpMiddle(Level level, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        if (blockEntity.getLevel() != null && blockEntity.getBumpDirection() == Direction.DOWN) {
            this.launchEntitiesOnTop(blockEntity.getLevel(), blockEntity.getBlockPos());
        }
        super.onBumpMiddle(level, pos, state, blockEntity);
    }

    @Override
    public void launchEntitiesOnTop(Level level, BlockPos pos) {
        super.launchEntitiesOnTop(level, pos);
        // TODO: add falling blocks

        // Only play high sound if all entities are sneaking
        boolean shouldPlayHighSound = false;
        List<Entity> entities = level.getEntities(null, new AABB(pos.above()));
        for (Entity entity : entities) {
            if (!entity.isShiftKeyDown()) {
                shouldPlayHighSound = true;
                break;
            }
        }
        Vec3 center = pos.getCenter();
        this.addParticles(level, pos);
        level.playSound(null, center.x(), center.y(), center.z(), shouldPlayHighSound ? this.highJumpSound : this.lowJumpSound, SoundSource.BLOCKS, 1F, 1F);
    }

    public void launchEntity(Entity entity) {
        Vec3 vec3d = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3d.x, entity.isShiftKeyDown() ? 0.5D : 0.9D, vec3d.z);
        entity.needsSync = true;
    }

    public void addParticles(Level level, BlockPos blockPos) {
        Vec3 center = blockPos.getCenter();
        RandomSource random = level.getRandom();
        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            double x = center.x() + (random.nextInt(7) - 3) / 10D;
            double y = center.y() + 0.6F;
            double z = center.z() + (random.nextInt(7) - 3) / 10D;
            double color = random.nextInt(2) * 0.2D + 0.1D;
            level.addParticle(ParticleTypes.NOTE, x, y, z, color, 1.0D, 1.0D);
        }
    }
}

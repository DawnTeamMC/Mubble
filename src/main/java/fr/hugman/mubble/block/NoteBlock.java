package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.BumpedBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * @author Napero
 * @author Hugman
 * @since v4.0.0
 */
public class NoteBlock extends MarioBumpableBlock {
    private final SoundEvent lowJumpSound;
    private final SoundEvent highJumpSound;

    public NoteBlock(SoundEvent lowJumpSound, SoundEvent highJumpSound, Settings builder) {
        super(builder);
        this.lowJumpSound = lowJumpSound;
        this.highJumpSound = highJumpSound;
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        // Do not apply fall damage
    }

    @Override
    public void onEntityLand(BlockView view, Entity entity) {
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getBlockPos().down();

        if(!world.isClient()) {
            BumpedBlockEntity.bump(world, pos, new BlockHitResult(entity.getPos(), Direction.UP, pos, false));
        }
    }

    @Override
    public BlockState onBumpCompleted(BumpedBlockEntity entity) {
        return entity.getBlockState();
    }

    @Override
    public void onBumpPeak(BumpedBlockEntity entity) {
        this.launchEntitiesOnTop(entity.getWorld(), entity.getPos());
        if(entity.getWorld() != null && entity.getBumpDirection() == Direction.DOWN) {
            this.addParticles(entity.getWorld(), entity.getPos());
        }
    }

    @Override
    public void launchEntitiesOnTop(World world, BlockPos pos) {
        super.launchEntitiesOnTop(world, pos);

        // Only play high sound if all entities are sneaking
        boolean shouldPlayHighSound = false;
        for(Entity entity : this.getEntitiesOnTop(world, pos)) {
            if(!entity.isSneaking()) {
                shouldPlayHighSound = true;
                break;
            }
        }
        Vec3d center = pos.toCenterPos();
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

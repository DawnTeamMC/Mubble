package fr.hugman.mubble.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class NoteBlock extends Block implements UnderHittableBlock {

    private final SoundEvent lowJumpSound;
    private final SoundEvent highJumpSound;

    public NoteBlock(SoundEvent lowJumpSound, SoundEvent highJumpSound, Settings builder) {
        super(builder);
        this.lowJumpSound = lowJumpSound;
        this.highJumpSound = highJumpSound;
    }

    @Override
    public void onHitFromUnder(BlockState state, World world, BlockPos pos, BlockHitResult hitResult) {
        if(!world.isClient()) {
            this.trigger(world, pos, lowJumpSound);
        }
    }

    @Override
    public void onEntityLand(BlockView worldIn, Entity entityIn) {
        launch(entityIn.world, entityIn);
    }

    public void trigger(World worldIn, BlockPos pos, SoundEvent sound) {
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 0.5D;
        final double z = pos.getZ() + 0.5D;
        final int rand = worldIn.random.nextInt(16);
        if (worldIn instanceof ServerWorld) {
            worldIn.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 1F, 1F);
            ((ServerWorld) worldIn).spawnParticles(ParticleTypes.NOTE, x, y + 0.6F, z, 3, 0.2F, 0.1F, 0.2F, 0.0D);
        }
    }

    public void launch(World worldIn, Entity entityIn) {
        Vec3d vec3d = entityIn.getVelocity();
        if(entityIn instanceof LivingEntity) {
            BlockPos pos = entityIn.getBlockPos().down();
            if(!entityIn.isSneaking()) {
                trigger(worldIn, pos, highJumpSound);
                entityIn.setVelocity(vec3d.x, getProperLaunchMotion(), vec3d.z);
            }
            else {
                trigger(worldIn, pos, lowJumpSound);
                entityIn.setVelocity(vec3d.x, 0.5D, vec3d.z);
            }
        }
    }

    public double getProperLaunchMotion() {
        return 0.9D;
    }


}

package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleBlockPack;
import com.hugman.mubble.init.MubbleSoundPack;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class NoteBlock extends Block {
	public NoteBlock(Settings builder) {
		super(builder);
	}

	@Override
	public void onLandedUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.handleFallDamage(fallDistance, 0.0F);
	}

	@Override
	public void onSteppedOn(World worldIn, BlockPos pos, Entity entityIn) {
		if(Math.abs(entityIn.getVelocity().getY()) < 0.45D) {
			entityIn.setVelocity(entityIn.getVelocity().getX(), 0.5D, entityIn.getVelocity().getZ());
		}
	}

	@Override
	public void onEntityLand(BlockView worldIn, Entity entityIn) {
		launch(entityIn.world, entityIn);
	}

	public void launch(World worldIn, Entity entityIn) {
		Vec3d vec3d = entityIn.getVelocity();
		if(entityIn instanceof LivingEntity) {
			SoundEvent lowJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB;
			SoundEvent highJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB;
			if(this == MubbleBlockPack.SMB_NOTE_BLOCK || this == MubbleBlockPack.SMB_SUPER_NOTE_BLOCK) {
				lowJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB;
				highJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB;
			}
			else if(this == MubbleBlockPack.SMB3_NOTE_BLOCK || this == MubbleBlockPack.SMB3_SUPER_NOTE_BLOCK) {
				lowJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB3;
				highJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMB3;
			}
			else if(this == MubbleBlockPack.SMW_NOTE_BLOCK || this == MubbleBlockPack.SMW_SUPER_NOTE_BLOCK) {
				lowJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMW;
				highJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_SMW;
			}
			else if(this == MubbleBlockPack.NSMBU_NOTE_BLOCK || this == MubbleBlockPack.NSMBU_SUPER_NOTE_BLOCK) {
				lowJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_LOW_NSMBU;
				highJumpSound = MubbleSoundPack.BLOCK_NOTE_BLOCK_JUMP_HIGH_NSMBU;
			}
			BlockPos pos = entityIn.getBlockPos().down();
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.5D;
			final double z = pos.getZ() + 0.5D;
			Random rand = new Random();
			if(!entityIn.isSneaking()) {
				worldIn.playSound(null, x, y, z, highJumpSound, SoundCategory.BLOCKS, 1f, 1f);
				for(int i = 0; i < rand.nextInt(5) + 1; i++) {
					worldIn.addParticle(ParticleTypes.NOTE, x + (rand.nextInt(7) - 3) / 10D, y + 0.6D, z + (rand.nextInt(7) - 3) / 10D, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
				}
				entityIn.setVelocity(vec3d.x, getProperLaunchMotion(), vec3d.z);
			}
			else {
				worldIn.playSound(null, x, y, z, lowJumpSound, SoundCategory.BLOCKS, 1f, 1f);
				for(int i = 0; i < rand.nextInt(1) + 1; i++) {
					worldIn.addParticle(ParticleTypes.NOTE, x, y + 0.6D, z, (rand.nextInt(7) - 3) / 10D, 0.2D, (rand.nextInt(7) - 3) / 10D);
				}
				entityIn.setVelocity(vec3d.x, 0.5D, vec3d.z);
			}
		}
	}

	public double getProperLaunchMotion() {
		return 0.9D;
	}
}

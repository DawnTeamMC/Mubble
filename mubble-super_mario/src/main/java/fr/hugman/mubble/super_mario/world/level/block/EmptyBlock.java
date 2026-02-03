package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author Hugman
 * @since v4.0.0
 */
public class EmptyBlock extends Block implements HittableBlock {
    public EmptyBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void onHit(Level level, BlockState state, Entity entity, BlockHitResult hit) {
        var pos = hit.getLocation();
        if (level != null) {
            level.playSound(null, pos.x(), pos.y(), pos.z(), SuperMarioSounds.BUMPABLE_BLOCK_BUMP, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}

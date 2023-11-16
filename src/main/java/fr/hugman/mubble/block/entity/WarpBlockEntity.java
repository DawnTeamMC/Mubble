package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class WarpBlockEntity extends BlockEntity {
    public WarpBlockEntity(BlockPos pos, BlockState state) {
        super(Mubble.WARP_BLOCK_ENTITY, pos, state);
    }
}

package fr.hugman.mubble.block.entity;

import fr.hugman.mubble.registry.SuperMario;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author MaxBrick
 * @since v4.0.0
 */

public class WarpBlockEntity extends BlockEntity {
    // Destination position defaults to the block above warp pipe's current position
    public int destX = pos.getX();
    public int destY = pos.getY() + 1;
    public int destZ = pos.getZ();

    public WarpBlockEntity(BlockPos pos, BlockState state) {
        super(SuperMario.WARP_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("destx", destX);
        nbt.putInt("desty", destY);
        nbt.putInt("destz", destZ);

        super.writeNbt(nbt);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        destX = nbt.getInt("destx");
        destY = nbt.getInt("desty");
        destZ = nbt.getInt("destz");
    }

}

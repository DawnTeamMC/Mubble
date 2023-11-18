package fr.hugman.mubble.block;

import fr.hugman.mubble.block.entity.WarpBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MaxBrick
 * @since v4.0.0
 */
public class WarpBlock extends Block implements BlockEntityProvider {
    public WarpBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WarpBlockEntity(pos, state);
    }

    /*
    Any entity that steps on the block will teleport to the block entity's destination.
    This is a very crappy solution and should be changed asap.
     */
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WarpBlockEntity){
                WarpBlockEntity warpBlockEntity = (WarpBlockEntity) blockEntity;

                entity.teleport(warpBlockEntity.destX, warpBlockEntity.destY, warpBlockEntity.destZ);
            }
        }
    }
}

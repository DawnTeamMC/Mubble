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

    //Players need to crouch to enter pipe, hence the separate event caller thing
    //I don't know how to properly center a location, so I added .5 to x and z
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient){

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof WarpBlockEntity warpBlockEntity){

                if(entity.isPlayer() && entity.isInSneakingPose()) {
                    entity.teleport(
                            warpBlockEntity.getDestinationPos().getX() + 0.5,
                            warpBlockEntity.getDestinationPos().getY() + 0.6,
                            warpBlockEntity.getDestinationPos().getZ() + 0.5
                    );
                }
            }
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient){
            super.onLandedUpon(world, state, pos, entity, fallDistance);
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof WarpBlockEntity warpBlockEntity){

                if(!entity.isPlayer()) {
                    entity.teleport(
                            warpBlockEntity.getDestinationPos().getX() + 0.5,
                            warpBlockEntity.getDestinationPos().getY() + 0.5,
                            warpBlockEntity.getDestinationPos().getZ() + 0.5
                    );
                }
            }
        }
    }
}

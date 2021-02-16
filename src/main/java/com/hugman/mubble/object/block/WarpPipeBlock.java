package com.hugman.mubble.object.block;

import com.hugman.mubble.object.block.block_entity.WarpPipeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WarpPipeBlock extends Block implements BlockEntityProvider {
    public WarpPipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new WarpPipeBlockEntity();
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        WarpPipeBlockEntity blockEntity = (WarpPipeBlockEntity) world.getBlockEntity(pos);

        if(!world.isClient && entity.isInSneakingPose()) {
            entity.teleport(blockEntity.getDestinationX(), blockEntity.getDestinationY(), blockEntity.getDestinationZ());
        }
    }
}

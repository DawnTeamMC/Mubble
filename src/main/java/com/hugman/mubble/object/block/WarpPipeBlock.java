package com.hugman.mubble.object.block;

import com.hugman.mubble.init.MubbleItems;
import com.hugman.mubble.object.block.block_entity.WarpPipeBlockEntity;
import com.hugman.mubble.object.item.WarpPipeLinkItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean moved) {
        if(!worldIn.isClient()) {
            WarpPipeBlockEntity blockEntity = (WarpPipeBlockEntity) worldIn.getBlockEntity(pos);

            blockEntity.setDestinationX(pos.getX());
            blockEntity.setDestinationY(pos.getY());
            blockEntity.setDestinationZ(pos.getZ());
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient() && player.isHolding(MubbleItems.WARP_PIPE_LINK)) {
            WarpPipeBlockEntity blockEntity = (WarpPipeBlockEntity) world.getBlockEntity(pos);
            WarpPipeLinkItem item = (WarpPipeLinkItem) player.getItemsHand();

            if(item.getOriginY() == -65) {
                item.setOriginX(pos.getX());
                item.setOriginY(pos.getY());
                item.setOriginZ(pos.getZ());
            } else {
                blockEntity.setDestinationX(item.getOriginX());
                blockEntity.setDestinationY(item.getOriginY());
                blockEntity.setDestinationZ(item.getOriginZ());
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }
}

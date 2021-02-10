package com.hugman.mubble.object.block;

import com.hugman.mubble.init.data.MubbleStats;
import com.hugman.mubble.object.block.block_entity.WarpPipeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WarpPipeBlock extends Block implements BlockEntityProvider {
    public WarpPipeBlock(Block.Settings builder) { super(builder); }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        if (entity.isSneaky()) {
            entity.setPos(1, 1, 1);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new WarpPipeBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient) {
            return ActionResult.SUCCESS;
        }
        else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
        }
        return ActionResult.CONSUME;
    }
}

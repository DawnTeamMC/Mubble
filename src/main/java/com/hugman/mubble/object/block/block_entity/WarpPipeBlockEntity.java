package com.hugman.mubble.object.block.block_entity;

import com.hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class WarpPipeBlockEntity extends BlockEntity {
    private int destinationX;
    private int destinationY;
    private int destinationZ;

    public WarpPipeBlockEntity() {
        super(MubbleBlocks.WARP_PIPE_ENTITY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putInt("destinationX", destinationX);
        tag.putInt("destinationY", destinationY);
        tag.putInt("destinationZ", destinationZ);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        destinationX = tag.getInt("destinationX");
        destinationY = tag.getInt("destinationY");
        destinationZ = tag.getInt("destinationZ");

    }
    public int getDestinationX() {
        return this.destinationX;
    }
    public int getDestinationY() {
        return this.destinationY;
    }
    public int getDestinationZ() {
        return this.destinationZ;
    }
}
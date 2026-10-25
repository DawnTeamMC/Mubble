package fr.hugman.mubble.super_mario.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.renderer.blockentity.state.SnakeMovingBlockRenderState;
import fr.hugman.mubble.super_mario.client.sound.SnakeSounds;
import fr.hugman.mubble.super_mario.world.level.block.SnakeMovement;
import fr.hugman.mubble.super_mario.world.level.block.entity.SnakeMovingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Draws the two ends of a running snake between the blocks they are travelling from and to, and the
 * tremble that runs through the whole of it at the end of its path.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class SnakeMovingBlockRenderer implements BlockEntityRenderer<SnakeMovingBlockEntity, SnakeMovingBlockRenderState> {
    /** How far a trembling block strays from its cell, in blocks. */
    private static final double SHAKE_AMPLITUDE = 0.04D;

    @Override
    public SnakeMovingBlockRenderState createRenderState() {
        return new SnakeMovingBlockRenderState();
    }

    /**
     * Kept on even when the section it stands in is off screen, so that the looping sound following
     * the head carries on while the player is looking the other way. There are only ever two of
     * these per snake.
     */
    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public void extractRenderState(SnakeMovingBlockEntity blockEntity, SnakeMovingBlockRenderState state, float partialTicks,
                                   Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        BlockPos pos = blockEntity.getBlockPos();
        state.block = blockEntity.getLevel() instanceof ClientLevel level && !blockEntity.getMovedState().isAir()
                ? SnakeBlockModels.extract(level, pos, blockEntity.getMovedState())
                : null;

        if (blockEntity.getMovement() == SnakeMovement.SHAKE) {
            this.extractShake(blockEntity, state, partialTicks, pos);
            return;
        }

        Direction direction = blockEntity.getDirection();
        float offset = blockEntity.getOffset(partialTicks);
        state.offsetX = direction.getStepX() * offset;
        state.offsetY = direction.getStepY() * offset;
        state.offsetZ = direction.getStepZ() * offset;

        if (blockEntity.getMovement() == SnakeMovement.HEAD && blockEntity.getSnakeId() != 0) {
            SnakeSounds.heardAt(blockEntity.getSnakeId(), blockEntity.getSpeed(),
                    pos.getX() + 0.5D + state.offsetX, pos.getY() + 0.5D + state.offsetY, pos.getZ() + 0.5D + state.offsetZ);
        }
    }

    /**
     * A tremble, not a wobble: the offset is a pair of fast sine waves out of step with each other,
     * seeded off the block position so that no two blocks of the snake shake alike.
     */
    private void extractShake(SnakeMovingBlockEntity blockEntity, SnakeMovingBlockRenderState state, float partialTicks, BlockPos pos) {
        if (blockEntity.getLevel() == null) {
            return;
        }
        double time = blockEntity.getLevel().getGameTime() + partialTicks;
        double seed = pos.asLong() * 0.37D;
        state.offsetX = Math.sin(time * 2.7D + seed) * SHAKE_AMPLITUDE;
        state.offsetY = Math.sin(time * 3.1D + seed * 1.7D) * SHAKE_AMPLITUDE * 0.5D;
        state.offsetZ = Math.sin(time * 2.3D + seed * 2.3D) * SHAKE_AMPLITUDE;
    }

    @Override
    public void submit(SnakeMovingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.block == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(state.offsetX, state.offsetY, state.offsetZ);
        submitNodeCollector.submitMovingBlock(poseStack, state.block, 0);
        poseStack.popPose();
    }
}

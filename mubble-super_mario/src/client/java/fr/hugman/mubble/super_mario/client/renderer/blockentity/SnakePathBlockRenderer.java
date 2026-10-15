package fr.hugman.mubble.super_mario.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.client.renderer.blockentity.state.SnakePathRenderState;
import fr.hugman.mubble.super_mario.world.level.block.SnakePathBlock;
import fr.hugman.mubble.super_mario.world.level.block.entity.SnakePathBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Draws the route a snake is going to walk, for the players who are allowed to see it.
 *
 * <h2>Why a block entity renderer</h2>
 * A path block is only there for whoever is building the snake, and a chunk mesh is baked once and
 * handed to every player, so the model cannot be left to the chunk. That leaves two ways of drawing
 * it every frame instead, and this is the cheaper of the two.
 * <p>
 * The alternative — a pass of our own over the level renderer — would have to answer "which path
 * blocks are in view" by itself, which means keeping an index of every path block in render distance
 * and chasing it through block updates, chunk loads, chunk unloads and section palettes. The game
 * already keeps exactly that index for block entities and hands it over culled, so a block entity is
 * the same drawing for a fraction of the code. The cost is that a path block is a block entity:
 * bounded at 120 per snake, carrying no data at all, and skipped before extraction by
 * {@link #shouldRender} on every frame where nobody is holding a snake block.
 * <p>
 * The third option, rebuilding the chunk meshes whenever the held item changes, was dropped: it
 * makes the common case free but stutters the whole view every time the player scrolls past a snake
 * block on their hotbar.
 *
 * @author Hugman
 * @since v4.0.0
 */
@Environment(EnvType.CLIENT)
public class SnakePathBlockRenderer implements BlockEntityRenderer<SnakePathBlockEntity, SnakePathRenderState> {
    @Override
    public SnakePathRenderState createRenderState() {
        return new SnakePathRenderState();
    }

    @Override
    public boolean shouldRender(SnakePathBlockEntity blockEntity, Vec3 cameraPosition) {
        return SnakePathBlock.isVisibleTo(Minecraft.getInstance().player)
                && BlockEntityRenderer.super.shouldRender(blockEntity, cameraPosition);
    }

    @Override
    public void extractRenderState(SnakePathBlockEntity blockEntity, SnakePathRenderState state, float partialTicks,
                                   Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.block = blockEntity.getLevel() instanceof ClientLevel level
                ? SnakeBlockModels.extract(level, blockEntity.getBlockPos(), blockEntity.getBlockState())
                : null;
    }

    @Override
    public void submit(SnakePathRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.block != null) {
            submitNodeCollector.submitMovingBlock(poseStack, state.block, 0);
        }
    }
}

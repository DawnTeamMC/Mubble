package fr.hugman.mubble.platformer.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jspecify.annotations.Nullable;

/**
 * The read-only questions the simulation asks the world.
 * <p>
 * Every answer only depends on the blocks and on the player's collision context, which both sides
 * know alike: that is what keeps the step deterministic. Entities are left out on purpose, since a
 * boat or a shulker can sit a few ticks apart on the client and the server.
 */
public final class PlatformerWorld {
    /** How far below the feet a block still counts as supporting them, the way vanilla reads its friction. */
    private static final double SUPPORT_OFFSET = 0.500001D;

    private final Player player;

    public PlatformerWorld(Player player) {
        this.player = player;
    }

    /**
     * The level the player is in now. Read every time rather than kept: a server player stays the same
     * object when it changes dimension.
     */
    public Level level() {
        return this.player.level();
    }

    public Player player() {
        return this.player;
    }

    public EntityDimensions dimensions(Pose pose) {
        return this.player.getDimensions(pose);
    }

    public AABB box(Vec3 position, Pose pose) {
        return this.dimensions(pose).makeBoundingBox(position);
    }

    /**
     * Whether anything solid overlaps {@code box}: blocks, the world border, and the entities vanilla
     * collisions stop the player at, such as boats, shulkers or cloud platforms. Those are asked the
     * same question vanilla asks them, so one only solid from above, or for some players, is solid
     * here exactly when it is for the player's own moves.
     */
    public boolean collides(AABB box) {
        if (!this.level().noBlockCollision(this.player, box) || !this.level().getWorldBorder().isWithinBounds(box)) {
            return true;
        }
        for (var shape : this.level().getEntityCollisions(this.player, box)) {
            // touching is not overlapping, as for blocks
            if (shape.bounds().intersects(box)) {
                return true;
            }
        }
        return false;
    }

    /**
     * How far {@code box} can move down before landing on something, block or solid entity, up to {@code max}.
     *
     * @return the distance, or a negative number when nothing is within reach
     */
    public double distanceToGround(AABB box, double max) {
        var shapes = this.level().getCollisions(this.player, box.expandTowards(0.0D, -max, 0.0D));
        double allowed = Shapes.collide(Direction.Axis.Y, box, shapes, -max);
        double distance = -allowed;
        return distance < max - 1.0E-7D ? distance : -1.0D;
    }

    /** How far {@code box} can move along {@code axis}, up to {@code distance}, which may be negative, among blocks and solid entities. */
    public double sweep(AABB box, Direction.Axis axis, double distance) {
        var shapes = this.level().getCollisions(this.player, box.expandTowards(
                axis == Direction.Axis.X ? distance : 0.0D,
                axis == Direction.Axis.Y ? distance : 0.0D,
                axis == Direction.Axis.Z ? distance : 0.0D));
        return Shapes.collide(axis, box, shapes, distance);
    }

    public BlockState blockState(BlockPos pos) {
        return this.level().getBlockState(pos);
    }

    public boolean is(BlockPos pos, TagKey<Block> tag) {
        return this.level().getBlockState(pos).is(tag);
    }

    /**
     * What the player can climb at {@code position}, as vanilla has it: a block tagged climbable
     * (ladders, vines, scaffolding...), or an open trapdoor over a ladder facing the same way. Null
     * when there is nothing to climb.
     */
    @Nullable
    public BlockState climbable(Vec3 position) {
        var pos = BlockPos.containing(position);
        var state = this.blockState(pos);
        if (state.is(BlockTags.CLIMBABLE)) {
            return state;
        }
        if (state.getBlock() instanceof TrapDoorBlock && state.getValue(TrapDoorBlock.OPEN)) {
            var below = this.blockState(pos.below());
            if (below.is(Blocks.LADDER) && below.getValue(LadderBlock.FACING) == state.getValue(TrapDoorBlock.FACING)) {
                return state;
            }
        }
        return null;
    }

    /**
     * The block whose friction, speed and jump factors apply to the player standing at {@code position},
     * as vanilla's {@code getBlockPosBelowThatAffectsMyMovement}: the block the feet rest on, the one
     * closest to the player when they rest on several, so that the edge of a block of ice still slides
     * with the center over air. Walls and fence gates count as they are, any other block through the
     * one {@link #SUPPORT_OFFSET} below the feet in its column. With nothing under the feet, it is the
     * block {@link #SUPPORT_OFFSET} below the center.
     */
    public BlockPos supportingPos(Vec3 position) {
        var support = this.supportingBlock(position);
        if (support == null) {
            return BlockPos.containing(position.x, position.y - SUPPORT_OFFSET, position.z);
        }
        var state = this.blockState(support);
        if (state.is(BlockTags.WALLS) || state.getBlock() instanceof FenceGateBlock) {
            return support;
        }
        return support.atY(Mth.floor(position.y - SUPPORT_OFFSET));
    }

    /**
     * The block the feet rest on, as vanilla's {@code checkSupportingBlock} finds it, or null. Vanilla's
     * {@code Level.findSupportingBlock} measures distances from where the entity stands now, which the
     * server only knows up to the last validated step: this measures them from {@code position} instead.
     */
    @Nullable
    private BlockPos supportingBlock(Vec3 position) {
        var box = this.box(position, Pose.STANDING);
        var feet = new AABB(box.minX, box.minY - 1.0E-6D, box.minZ, box.maxX, box.minY, box.maxZ);
        var collisions = new BlockCollisions<>(this.level(), this.player, feet, false, (pos, shape) -> pos);
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;
        while (collisions.hasNext()) {
            var pos = collisions.next();
            double distance = pos.distToCenterSqr(position);
            if (distance < closestDistance || distance == closestDistance && (closest == null || closest.compareTo(pos) < 0)) {
                closest = pos.immutable();
                closestDistance = distance;
            }
        }
        return closest;
    }

    /** The friction of the block under {@code position}, with the friction modifier attribute applied like vanilla does. */
    public float friction(Vec3 position) {
        float friction = this.blockState(this.supportingPos(position)).getBlock().getFriction();
        float modifier = (float) this.player.getAttributeValue(Attributes.FRICTION_MODIFIER);
        return Math.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }

    /** The speed factor of the blocks at and under {@code position}: soul sand, honey... */
    public float speedFactor(Vec3 position) {
        float here = this.blockState(BlockPos.containing(position)).getBlock().getSpeedFactor();
        return here == 1.0F ? this.blockState(this.supportingPos(position)).getBlock().getSpeedFactor() : here;
    }

    /** The jump factor of the blocks at and under {@code position}: honey... */
    public float jumpFactor(Vec3 position) {
        float here = this.blockState(BlockPos.containing(position)).getBlock().getJumpFactor();
        return here == 1.0F ? this.blockState(this.supportingPos(position)).getBlock().getJumpFactor() : here;
    }

    /**
     * How deep in water {@code box} stands: from its bottom up to the highest water surface it
     * touches, as vanilla measures the fluid height of an entity. 0 out of water.
     */
    public double waterDepth(AABB box) {
        var inner = box.deflate(0.001D);
        int minX = Mth.floor(inner.minX);
        int maxX = Mth.ceil(inner.maxX);
        int minY = Mth.floor(inner.minY);
        int maxY = Mth.ceil(inner.maxY);
        int minZ = Mth.floor(inner.minZ);
        int maxZ = Mth.ceil(inner.maxZ);
        var level = this.level();
        var pos = new BlockPos.MutableBlockPos();
        double surface = Double.NEGATIVE_INFINITY;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    pos.set(x, y, z);
                    var fluid = level.getFluidState(pos);
                    if (fluid.is(FluidTags.WATER)) {
                        double height = y + fluid.getHeight(level, pos);
                        if (height >= inner.minY) {
                            surface = Math.max(surface, height);
                        }
                    }
                }
            }
        }
        return surface == Double.NEGATIVE_INFINITY ? 0.0D : surface - inner.minY;
    }
}

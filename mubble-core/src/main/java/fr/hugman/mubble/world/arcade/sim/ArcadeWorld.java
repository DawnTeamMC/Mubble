package fr.hugman.mubble.world.arcade.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

/**
 * The read-only questions the simulation asks the world.
 * <p>
 * Every answer only depends on the blocks and on the player's collision context, which both sides
 * know alike: that is what keeps the step deterministic. Entities are left out on purpose, since a
 * boat or a shulker can sit a few ticks apart on the client and the server.
 */
public final class ArcadeWorld {
    /** How far below the feet a block still counts as supporting them, the way vanilla reads its friction. */
    private static final double SUPPORT_OFFSET = 0.500001D;

    private final Player player;

    public ArcadeWorld(Player player) {
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

    public BlockPos supportingPos(Vec3 position) {
        return BlockPos.containing(position.x, position.y - SUPPORT_OFFSET, position.z);
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
}

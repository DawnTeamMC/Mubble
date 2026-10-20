package fr.hugman.mubble.super_mario.world.level.block.entity;

import fr.hugman.mubble.super_mario.world.level.block.SnakeMovement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The head and the tail of a running snake, while they are between two blocks.
 * <p>
 * Everything in the middle of a snake is an ordinary solid block that never moves, so only these two
 * ends need a collision box that slides. The head grows into the cell it occupies, coming in from the
 * block behind it; the tail slides forward out of its own cell and into the body in front of it,
 * which is what frees the space behind a snake one block at a time.
 *
 * <h2>Pushing entities</h2>
 * The approach is lifted from {@code PistonMovingBlockEntity}, down to the constants, because every
 * part of it is there for a reason that applies here too:
 * <ul>
 * <li>The push is <em>a move, never a velocity</em>. {@code entity.move(MoverType.PISTON, …)} shoves
 *     the entity exactly far enough to clear the block and nothing more, so a player in the middle of
 *     a jump or a sprint keeps the momentum they had. Writing to {@code setDeltaMovement} instead
 *     would throw away whatever the player was doing, which is precisely what wrecks parkour.
 * <li>{@code MoverType.PISTON} is not decoration: {@code Entity.limitPistonMovement} clamps the total
 *     a block may shove an entity along one axis to 0.51 per tick, and resets that budget every tick.
 *     It is what keeps two blocks pushing from opposite sides from flinging anything.
 * <li>{@link #NOCLIP} is why the entity can be pushed at all. Without it the block doing the pushing
 *     is part of the world the entity collides against, and the move it is given is immediately
 *     cancelled by the very shape it is being pushed out of.
 * <li>Both sides tick. A player owns their own movement on their client and the server only corrects
 *     them, so a push applied on the server alone is fought by the client every tick and the player
 *     rubber-bands. Pistons tick on the client for the same reason, and so does this.
 * </ul>
 * Entities are never carried: there is no counterpart to the piston's honey block, and standing on
 * top of a snake is not an intersection, so the body slides away underneath and the player has to
 * keep walking.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SnakeMovingBlockEntity extends BlockEntity {
    /** Extra nudge past the exact clearance, so the entity ends up outside and not touching. */
    private static final double PUSH_OFFSET = 0.01D;
    /** Damage dealt each tick to an entity that has nowhere left to be pushed. */
    private static final float CRUSH_DAMAGE = 2.0F;
    /** Ticks a finished block waits for the manager before putting itself away. */
    private static final int ORPHAN_TICKS = 20;
    /** The same, for a trembling one: it is meant to stand there for a couple of seconds. */
    private static final int SHAKE_ORPHAN_TICKS = 200;

    /**
     * The direction the block currently being moved is travelling in, while it is being moved.
     * <p>
     * Read back by {@link #getCollisionShape}: the entity being pushed out of this block must not
     * collide with it on the way out.
     */
    private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);

    private static final String MOVED_STATE_KEY = "moved_state";
    private static final String FINAL_STATE_KEY = "final_state";
    private static final String DIRECTION_KEY = "direction";
    private static final String MOVEMENT_KEY = "movement";
    private static final String TICKS_PER_BLOCK_KEY = "ticks_per_block";
    private static final String PROGRESS_KEY = "progress";
    private static final String SNAKE_ID_KEY = "snake_id";

    private BlockState movedState = Blocks.AIR.defaultBlockState();
    private BlockState finalState = Blocks.AIR.defaultBlockState();
    private Direction direction = Direction.NORTH;
    private SnakeMovement movement = SnakeMovement.HEAD;
    private int ticksPerBlock = 1;
    private float progress;
    private float progressO;
    private int snakeId;
    private int orphanTicks;

    public SnakeMovingBlockEntity(BlockPos pos, BlockState state) {
        super(SuperMarioBlockEntityTypes.SNAKE_MOVING_BLOCK, pos, state);
    }

    public SnakeMovingBlockEntity(BlockPos pos, BlockState state, BlockState movedState, BlockState finalState,
                                  Direction direction, SnakeMovement movement, int ticksPerBlock, int snakeId) {
        this(pos, state);
        this.movedState = movedState;
        this.finalState = finalState;
        this.direction = direction;
        this.movement = movement;
        this.ticksPerBlock = Math.max(1, ticksPerBlock);
        this.snakeId = snakeId;
    }

    /*=====================*/
    /*  GETTERS & SETTERS  */
    /*=====================*/

    public BlockState getMovedState() {
        return this.movedState;
    }

    public BlockState getFinalState() {
        return this.finalState;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public SnakeMovement getMovement() {
        return this.movement;
    }

    public int getSnakeId() {
        return this.snakeId;
    }

    public float getProgress(float partialTick) {
        return Mth.lerp(Math.min(partialTick, 1.0F), this.progressO, this.progress);
    }

    /** Whether this block has reached the cell it was heading for and is only waiting to be tidied up. */
    public boolean isDone() {
        return this.progress >= 1.0F;
    }

    /**
     * How far along its direction the rendered block sits, in blocks.
     * <p>
     * A head starts a whole block behind the cell it occupies and arrives at zero; a tail starts
     * where it is and leaves a whole block forward; a shaking block never goes anywhere.
     */
    public float getOffset(float partialTick) {
        return this.offsetOf(this.getProgress(partialTick));
    }

    /**
     * The same, for collision and pushing, which run on the position the block has reached this tick
     * rather than the one being drawn.
     * <p>
     * Those are not the same number: the renderer interpolates from last tick's progress towards
     * this one, so it trails by up to a tick. Colliding against the drawn position leaves whatever
     * stands on the block a step inside it by the time the step ends, and vanilla answers an entity
     * inside a solid block by shoving it out sideways and overwriting its horizontal speed.
     */
    private float collisionOffset() {
        return this.offsetOf(this.progress);
    }

    private float offsetOf(float progress) {
        if (!this.movement.travels()) {
            return 0.0F;
        }
        return this.movement == SnakeMovement.HEAD ? progress - 1.0F : progress;
    }

    /*========*/
    /*  DATA  */
    /*========*/

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(MOVED_STATE_KEY, BlockState.CODEC, this.movedState);
        output.store(FINAL_STATE_KEY, BlockState.CODEC, this.finalState);
        output.store(DIRECTION_KEY, Direction.LEGACY_ID_CODEC, this.direction);
        output.store(MOVEMENT_KEY, SnakeMovement.CODEC, this.movement);
        output.putInt(TICKS_PER_BLOCK_KEY, this.ticksPerBlock);
        output.putFloat(PROGRESS_KEY, this.progressO);
        output.putInt(SNAKE_ID_KEY, this.snakeId);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.movedState = input.read(MOVED_STATE_KEY, BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState());
        this.finalState = input.read(FINAL_STATE_KEY, BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState());
        this.direction = input.read(DIRECTION_KEY, Direction.LEGACY_ID_CODEC).orElse(Direction.NORTH);
        this.movement = input.read(MOVEMENT_KEY, SnakeMovement.CODEC).orElse(SnakeMovement.HEAD);
        this.ticksPerBlock = Math.max(1, input.getIntOr(TICKS_PER_BLOCK_KEY, 1));
        this.progress = input.getFloatOr(PROGRESS_KEY, 0.0F);
        this.progressO = this.progress;
        this.snakeId = input.getIntOr(SNAKE_ID_KEY, 0);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /*===========*/
    /*  TICKING  */
    /*===========*/

    public static void tick(Level level, BlockPos pos, BlockState state, SnakeMovingBlockEntity entity) {
        entity.progressO = entity.progress;
        if (!entity.movement.travels() || entity.isDone()) {
            // The manager swaps a block for the real thing on the tick it is done with it. If it
            // never does — the record was lost, say — the block tidies itself away rather than
            // staying in the world as an invisible wall.
            if (!level.isClientSide() && ++entity.orphanTicks > entity.orphanLimit()) {
                entity.place(level, pos);
            }
            return;
        }

        float newProgress = Math.min(1.0F, entity.progress + 1.0F / entity.ticksPerBlock);
        moveCollidedEntities(level, pos, newProgress, entity);
        entity.progress = newProgress;
    }

    private int orphanLimit() {
        return this.movement.travels() ? ORPHAN_TICKS : SHAKE_ORPHAN_TICKS;
    }

    /** Replaces this block with whatever the snake leaves behind here. */
    public void place(Level level, BlockPos pos) {
        this.setRemoved();
        level.removeBlockEntity(pos);
        if (level.getBlockState(pos).getBlock() != this.getBlockState().getBlock()) {
            return;
        }
        // The last line of defence for anything the step did not quite finish shoving clear: lifting
        // it keeps a snake rising under a player feeling like a lift, where vanilla would shove it
        // out sideways and wipe its horizontal speed on the way.
        Block.pushEntitiesUp(Blocks.AIR.defaultBlockState(), this.finalState, level, pos);
        level.setBlock(pos, this.finalState, Block.UPDATE_ALL);
    }

    /*===========*/
    /*  SHAPES   */
    /*===========*/

    public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
        if (this.movement.travels() && this.progress < 1.0F && NOCLIP.get() == this.direction) {
            return Shapes.empty();
        }
        float offset = this.collisionOffset();
        return this.movedState.getCollisionShape(level, pos)
                .move(this.direction.getStepX() * offset, this.direction.getStepY() * offset, this.direction.getStepZ() * offset);
    }

    /*===========*/
    /*  PUSHING  */
    /*===========*/

    /**
     * Shoves whatever stands in the way of the block between where it is and where it will be after
     * this tick.
     */
    private static void moveCollidedEntities(Level level, BlockPos pos, float newProgress, SnakeMovingBlockEntity self) {
        Direction movement = self.direction;
        double step = newProgress - self.progress;
        VoxelShape shape = self.movedState.getCollisionShape(level, pos);
        if (shape.isEmpty()) {
            return;
        }

        AABB bounds = self.offsetToProgress(pos, shape.bounds());
        List<Entity> entities = level.getEntities(null, sweptArea(bounds, movement, step).minmax(bounds));
        if (entities.isEmpty()) {
            return;
        }

        List<AABB> boxes = shape.toAabbs();
        for (Entity entity : entities) {
            if (entity.getPistonPushReaction() == PushReaction.IGNORE) {
                continue;
            }
            double push = 0.0D;
            for (AABB box : boxes) {
                AABB swept = sweptArea(self.offsetToProgress(pos, box), movement, step);
                if (swept.intersects(entity.getBoundingBox())) {
                    push = Math.max(push, clearance(swept, movement, entity.getBoundingBox()));
                    if (push >= step) {
                        break;
                    }
                }
            }
            if (push <= 0.0D) {
                continue;
            }
            pushEntity(movement, entity, Math.min(push, step) + PUSH_OFFSET);
            crushIfStillInside(level, pos, newProgress, self, entity);
        }
    }

    private static void pushEntity(Direction movement, Entity entity, double distance) {
        NOCLIP.set(movement);
        Vec3 before = entity.position();
        boolean wasOnGround = entity.onGround();
        entity.move(MoverType.PISTON, new Vec3(distance * movement.getStepX(), distance * movement.getStepY(), distance * movement.getStepZ()));
        entity.applyEffectsFromBlocks(before, entity.position());
        // The push is the block's doing, not the entity's: leaving it in the movement log would have
        // the entity pay for it in step sounds and fall damage.
        entity.removeLatestMovementRecording();
        // Being carried by the ground is not leaving it. Entity.move clears the grounded flag on any
        // move that does not end in a collision below, and a player counted as airborne cannot jump:
        // a snake lifting someone would otherwise take their jump away for as long as it carried them.
        if (wasOnGround) {
            entity.setOnGround(true);
        }
        NOCLIP.set(null);
    }

    /**
     * Hurts an entity the block is closing in on with nowhere left to go. Without this, anything
     * squeezed between a snake and a wall would simply be shoved through it.
     */
    private static void crushIfStillInside(Level level, BlockPos pos, float newProgress, SnakeMovingBlockEntity self, Entity entity) {
        if (level.isClientSide()) {
            return;
        }
        float offset = self.movement == SnakeMovement.HEAD ? newProgress - 1.0F : newProgress;
        AABB after = self.movedState.getCollisionShape(level, pos).bounds()
                .move(pos.getX(), pos.getY(), pos.getZ())
                .move(self.direction.getStepX() * offset, self.direction.getStepY() * offset, self.direction.getStepZ() * offset);
        if (after.intersects(entity.getBoundingBox())) {
            entity.hurt(level.damageSources().inWall(), CRUSH_DAMAGE);
        }
    }

    /** The block's box, placed where the block is right now. */
    private AABB offsetToProgress(BlockPos pos, AABB box) {
        float offset = this.collisionOffset();
        return box.move(pos.getX() + this.direction.getStepX() * offset,
                pos.getY() + this.direction.getStepY() * offset,
                pos.getZ() + this.direction.getStepZ() * offset);
    }

    /**
     * The slab of space the leading face of {@code box} sweeps through when it travels {@code amount}
     * along {@code direction}.
     */
    private static AABB sweptArea(AABB box, Direction direction, double amount) {
        double delta = amount * direction.getAxisDirection().getStep();
        double min = Math.min(delta, 0.0D);
        double max = Math.max(delta, 0.0D);
        return switch (direction) {
            case WEST -> new AABB(box.minX + min, box.minY, box.minZ, box.minX + max, box.maxY, box.maxZ);
            case EAST -> new AABB(box.maxX + min, box.minY, box.minZ, box.maxX + max, box.maxY, box.maxZ);
            case DOWN -> new AABB(box.minX, box.minY + min, box.minZ, box.maxX, box.minY + max, box.maxZ);
            case UP -> new AABB(box.minX, box.maxY + min, box.minZ, box.maxX, box.maxY + max, box.maxZ);
            case NORTH -> new AABB(box.minX, box.minY, box.minZ + min, box.maxX, box.maxY, box.minZ + max);
            case SOUTH -> new AABB(box.minX, box.minY, box.maxZ + min, box.maxX, box.maxY, box.maxZ + max);
        };
    }

    /** How far {@code box} has to travel along {@code direction} to leave {@code outOf} behind. */
    private static double clearance(AABB outOf, Direction direction, AABB box) {
        return switch (direction) {
            case EAST -> outOf.maxX - box.minX;
            case WEST -> box.maxX - outOf.minX;
            case UP -> outOf.maxY - box.minY;
            case DOWN -> box.maxY - outOf.minY;
            case SOUTH -> outOf.maxZ - box.minZ;
            case NORTH -> box.maxZ - outOf.minZ;
        };
    }
}

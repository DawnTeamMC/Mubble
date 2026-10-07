package fr.hugman.mubble.arcade.gym;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The movement gym: labelled lanes measuring every number of the default profile, built in place by
 * {@code /mubble arcade gym}.
 * <p>
 * Each lane runs along +x from its start, a sign at its start telling what it measures. Lanes are 6
 * blocks apart along +z. The figures are the ones of {@code mubble:trial}: a jump of 2.2 blocks
 * clears a 2 block wall but not a 3 block one, a double jump clears 3, and so on.
 */
public final class ArcadeGym {
    /** Blocks between two lanes. */
    public static final int LANE_SPACING = 6;
    /** How wide a lane is. */
    public static final int LANE_WIDTH = 4;
    public static final int LENGTH = 64;

    private static final BlockState FLOOR = Blocks.SMOOTH_STONE.defaultBlockState();
    private static final BlockState WALL = Blocks.STONE_BRICKS.defaultBlockState();
    private static final BlockState MARK = Blocks.GOLD_BLOCK.defaultBlockState();

    private ArcadeGym() {
    }

    /** Every lane, in the order they are laid out along +z. */
    public enum Lane {
        GAPS("Gaps", "2 to 8 blocks"),
        WALLS("Walls", "2 to 6 high"),
        LEDGES("Ledges", "2 to 6 high"),
        STAIRS("Stair descent", "roll and slide"),
        WALL_JUMP("Wall jump shaft", "3 wide, 12 high"),
        NO_WALL_JUMP("No wall jump", "packed ice"),
        BOUNCE("Bounce", "slime"),
        MOMENTUM("Keeps momentum", "ice"),
        VAULT("Vault", "1 high");

        private final String title;
        private final String detail;

        Lane(String title, String detail) {
            this.title = title;
            this.detail = detail;
        }

        /** Where the lane starts, relative to the origin of the gym. */
        public BlockPos start(BlockPos origin) {
            return origin.offset(0, 0, this.ordinal() * LANE_SPACING);
        }
    }

    public static void build(ServerLevel level, BlockPos origin) {
        for (var lane : Lane.values()) {
            build(level, origin, lane);
        }
    }

    /** Builds one lane, floor included, clearing the space above it. */
    public static void build(ServerLevel level, BlockPos origin, Lane lane) {
        var start = lane.start(origin);
        fill(level, start.offset(-2, 0, 0), start.offset(LENGTH, 14, LANE_WIDTH - 1), Blocks.AIR.defaultBlockState());
        fill(level, start.offset(-2, -1, 0), start.offset(LENGTH, -1, LANE_WIDTH - 1), FLOOR);
        sign(level, start.offset(-2, 0, 0), lane.title, lane.detail);
        switch (lane) {
            case GAPS -> gaps(level, start);
            case WALLS -> walls(level, start, false);
            case LEDGES -> walls(level, start, true);
            case STAIRS -> stairs(level, start);
            case WALL_JUMP -> shaft(level, start, WALL);
            case NO_WALL_JUMP -> shaft(level, start, Blocks.PACKED_ICE.defaultBlockState());
            case BOUNCE -> fill(level, start.offset(4, -1, 0), start.offset(8, -1, LANE_WIDTH - 1), Blocks.SLIME_BLOCK.defaultBlockState());
            case MOMENTUM -> fill(level, start.offset(0, -1, 0), start.offset(LENGTH, -1, LANE_WIDTH - 1), Blocks.ICE.defaultBlockState());
            case VAULT -> {
                for (int x = 6; x < LENGTH; x += 8) {
                    fill(level, start.offset(x, 0, 0), start.offset(x, 0, LANE_WIDTH - 1), WALL);
                }
            }
        }
    }

    /** Gaps of 2 to 8 blocks, each between two 4 block long platforms. */
    private static void gaps(ServerLevel level, BlockPos start) {
        int x = 0;
        for (int gap = 2; gap <= 8; gap++) {
            x += 4;
            fill(level, start.offset(x, -1, 0), start.offset(x + gap - 1, -1, LANE_WIDTH - 1), Blocks.AIR.defaultBlockState());
            fill(level, start.offset(x - 1, -1, 0), start.offset(x - 1, -1, LANE_WIDTH - 1), MARK);
            sign(level, start.offset(x - 1, 0, 0), "Gap", gap + " blocks");
            x += gap;
        }
    }

    /** Walls, or ledges with room on top, of 2 to 6 blocks, every 8 blocks. */
    private static void walls(ServerLevel level, BlockPos start, boolean ledges) {
        int x = 6;
        for (int height = 2; height <= 6; height++) {
            int depth = ledges ? 3 : 1;
            fill(level, start.offset(x, 0, 0), start.offset(x + depth - 1, height - 1, LANE_WIDTH - 1), WALL);
            sign(level, start.offset(x - 2, 0, 0), ledges ? "Ledge" : "Wall", height + " high");
            x += depth + 8;
        }
    }

    /** A run up, then stairs going down 10 blocks. */
    private static void stairs(ServerLevel level, BlockPos start) {
        int top = 10;
        fill(level, start.offset(0, -1, 0), start.offset(8, top - 1, LANE_WIDTH - 1), WALL);
        var stair = Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);
        for (int step = 0; step < top; step++) {
            int x = 9 + step;
            fill(level, start.offset(x, -1, 0), start.offset(x, top - step - 2, LANE_WIDTH - 1), WALL);
            fill(level, start.offset(x, top - step - 1, 0), start.offset(x, top - step - 1, LANE_WIDTH - 1), stair);
        }
        sign(level, start.offset(0, top, 0), "Stairs", top + " down");
    }

    /** Two parallel walls 3 blocks apart, 12 high. */
    private static void shaft(ServerLevel level, BlockPos start, BlockState wall) {
        fill(level, start.offset(6, 0, 0), start.offset(6, 11, LANE_WIDTH - 1), wall);
        fill(level, start.offset(10, 0, 0), start.offset(10, 11, LANE_WIDTH - 1), wall);
    }

    private static void fill(ServerLevel level, BlockPos from, BlockPos to, BlockState state) {
        for (var pos : BlockPos.betweenClosed(from, to)) {
            level.setBlock(pos, state, 2);
        }
    }

    private static void sign(ServerLevel level, BlockPos pos, String title, String detail) {
        level.setBlock(pos, Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, 4), 2);
        if (level.getBlockEntity(pos) instanceof SignBlockEntity sign) {
            sign.setText(sign.getFrontText().setMessage(0, Component.literal(title)).setMessage(1, Component.literal(detail)), true);
        }
    }
}

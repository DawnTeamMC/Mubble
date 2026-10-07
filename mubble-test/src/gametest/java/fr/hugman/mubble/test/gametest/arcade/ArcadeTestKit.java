package fr.hugman.mubble.test.gametest.arcade;

import fr.hugman.mubble.arcade.tags.ArcadeMoveTags;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePrediction;
import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.access.AccessMode;
import fr.hugman.mubble.arcade.access.ArcadeSource;
import fr.hugman.mubble.arcade.access.ArcadeSources;
import fr.hugman.mubble.arcade.access.MoveSelector;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.arcade.sim.MoveContext;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Driving the arcade layer from a test: a player standing in for a client, and the input frames it
 * would send.
 * <p>
 * The stand-in is a mock player that is not in the level: it moves through the very same vanilla
 * collisions as a client does, with nothing in the level pushing it around or ticking it behind the
 * test's back.
 */
public final class ArcadeTestKit {
    /** The lane every arcade movement test runs in: 10 wide, 12 high, 48 long. */
    public static final String LANE = "mubble-gametest:arcade_lane";
    public static final int LANE_WIDTH = 10;
    public static final int LANE_LENGTH = 48;
    public static final Identifier SOURCE = Identifier.fromNamespaceAndPath("mubble-gametest", "arcade");

    private ArcadeTestKit() {
    }

    /** Floors the whole lane at y = 0. */
    public static void floor(GameTestHelper helper) {
        floor(helper, Blocks.STONE.defaultBlockState());
    }

    public static void floor(GameTestHelper helper, BlockState state) {
        fill(helper, new BlockPos(0, 0, 0), new BlockPos(LANE_WIDTH - 1, 0, LANE_LENGTH - 1), state);
    }

    public static void fill(GameTestHelper helper, BlockPos from, BlockPos to, BlockState state) {
        for (var pos : BlockPos.betweenClosed(from, to)) {
            helper.setBlock(pos, state);
        }
    }

    /** Gives {@code player} every move of {@code profile}, forced. */
    public static void force(Player player, ResourceKey<ArcadeProfile> profile) {
        ArcadeSources.add(player, new ArcadeSource(SOURCE, Optional.of(profile), Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), AccessMode.FORCE), 100, Optional.empty()));
        ArcadeController.of(player).tick();
    }

    /** A survival mock player standing in for a client at the center of {@code pos}, all moves of {@code profile} forced. */
    public static Player client(GameTestHelper helper, BlockPos pos, ResourceKey<ArcadeProfile> profile) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var absolute = helper.absoluteVec(Vec3.atBottomCenterOf(pos));
        player.setPos(absolute);
        player.setOldPosAndRot();
        player.setYRot(0.0F);
        player.setYHeadRot(0.0F);
        force(player, profile);
        return player;
    }

    /** One tick of the client: the layer is brought up to date, then the step runs through vanilla collisions. */
    public static MoveContext step(Player player, ArcadeInputFrame frame) {
        var controller = ArcadeController.of(player);
        controller.tick();
        player.setOldPosAndRot();
        return ArcadePrediction.step(player, controller, frame).context();
    }

    /** Builds input frames, coupled to a view looking south (+z), which is where the stick points by default. */
    public static final class Frames {
        private int tick;
        private float stickX;
        private float stickZ;
        private float yaw;
        private int held;
        private boolean coupled = true;

        /** Frames taken with the orbit camera on, rather than the first person view. */
        public Frames orbit() {
            this.coupled = false;
            return this;
        }

        public Frames stick(float x, float z) {
            this.stickX = x;
            this.stickZ = z;
            return this;
        }

        public Frames forward() {
            return this.stick(0.0F, 1.0F);
        }

        public Frames release() {
            return this.stick(0.0F, 0.0F);
        }

        public Frames yaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public Frames hold(int actions) {
            this.held |= actions;
            return this;
        }

        public Frames letGo(int actions) {
            this.held &= ~actions;
            return this;
        }

        /** A frame where nothing new is pressed. */
        public ArcadeInputFrame next() {
            return this.frame(0);
        }

        /** A frame pressing {@code actions}, which stay held afterwards. */
        public ArcadeInputFrame press(int actions) {
            this.held |= actions;
            return this.frame(actions);
        }

        /** A frame where {@code actions} were pressed and released between two ticks. */
        public ArcadeInputFrame tap(int actions) {
            return this.frame(actions);
        }

        private ArcadeInputFrame frame(int pressed) {
            return new ArcadeInputFrame(this.tick++, this.stickX, this.stickZ, this.yaw, this.coupled, (byte) this.held, (byte) pressed, (short) 0, (short) 0);
        }
    }
}

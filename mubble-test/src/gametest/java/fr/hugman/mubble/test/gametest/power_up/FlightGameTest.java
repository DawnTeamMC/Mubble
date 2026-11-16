package fr.hugman.mubble.test.gametest.power_up;

import fr.hugman.mubble.super_mario.references.SuperMarioFlightRefillIds;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.AirPlayers;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.FlightPolicy;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.ability.FlightAbility;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

import static fr.hugman.mubble.test.gametest.support.AirPlayers.JUMP_HELD;
import static fr.hugman.mubble.test.gametest.support.AirPlayers.STAND;

/**
 * Flying on a held jump key and the gauge it spends, on the numbers of the {@code flier} fixture: the
 * defaults of {@link FlightAbility}, with every refill source priced.
 */
public class FlightGameTest {
    private static final Input NOTHING_HELD = Input.EMPTY;
    private static final int GAUGE = FlightAbility.DEFAULT_GAUGE;
    private static final int RESTART = FlightAbility.DEFAULT_RESTART;
    private static final int COIN = 15;
    private static final int STOMP = 60;
    private static final int LANDING_TICKS = 120;
    /** Past the 80 ticks after which a vanilla server calls a player that never falls a flier. */
    private static final int KICK_TICKS = 90;
    private static final double CLIMB = 0.25D;

    @GameTest
    public void aHeldJumpKeyFlies(GameTestHelper helper) {
        var player = flier(helper);

        AirPlayers.tick(player, JUMP_HELD, 3);

        helper.assertTrue(player.isFlying(), "a jump key held in mid-air should fly");
        helper.succeed();
    }

    @GameTest
    public void lettingGoOfTheKeyStopsTheFlight(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 3);

        AirPlayers.tick(player, NOTHING_HELD, 1);

        helper.assertFalse(player.isFlying(), "a flight should not outlast the jump key");
        helper.succeed();
    }

    @GameTest
    public void nothingFliesOnTheGround(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLIER));
        player.setOnGround(true);

        AirPlayers.tick(player, JUMP_HELD, 5);

        helper.assertTrue(player.onGround(), "the player is not on the ground, the test proves nothing");
        helper.assertFalse(player.isFlying(), "a player on the ground should not be flying");
        helper.assertValueEqual(player.getFlightGauge(), GAUGE, "the gauge of a player who never took off");
        helper.succeed();
    }

    @GameTest
    public void aPowerUpWithoutFlightNeverFlies(GameTestHelper helper) {
        var player = AirPlayers.jumper(helper, PowerUpFixtures.EMPTY);

        AirPlayers.tick(player, JUMP_HELD, 5);

        helper.assertFalse(player.isFlying(), "a power-up granting no flight should not fly");
        helper.succeed();
    }

    @GameTest
    public void aFlyingPlayerClimbsAgainstTheFall(GameTestHelper helper) {
        var flying = flier(helper);
        var plain = AirPlayers.jumper(helper, PowerUpFixtures.EMPTY);

        AirPlayers.tick(flying, JUMP_HELD, 12);
        AirPlayers.tick(plain, JUMP_HELD, 12);

        helper.assertTrue(flying.getY() > plain.getY() + 1.0D,
                "a flying player should be well above one falling plainly, was " + flying.getY() + " against " + plain.getY());
        helper.succeed();
    }

    @GameTest
    public void theGaugeDrainsOnlyInFlight(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 2);

        int start = player.getFlightGauge();
        AirPlayers.tick(player, JUMP_HELD, 10);
        helper.assertValueEqual(player.getFlightGauge(), start - 10, "the gauge after ten ticks of flight");

        int spent = player.getFlightGauge();
        AirPlayers.tick(player, NOTHING_HELD, 3);
        helper.assertTrue(!player.onGround(), "the player landed, the test proves nothing");
        helper.assertValueEqual(player.getFlightGauge(), spent, "the gauge after ticks spent falling");
        helper.succeed();
    }

    @GameTest
    public void aFlightLetGoOfResumesAtOnce(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 5);
        AirPlayers.tick(player, NOTHING_HELD, 1);

        AirPlayers.tick(player, JUMP_HELD, 1);

        helper.assertTrue(player.isFlying(), "pressing the key again should fly again while there is gauge");
        helper.succeed();
    }

    @GameTest
    public void flyingLeavesNoFallDamage(GameTestHelper helper) {
        var player = flier(helper);

        AirPlayers.tick(player, JUMP_HELD, 20);

        helper.assertTrue(player.isFlying(), "the player is not flying, the test proves nothing");
        helper.assertValueEqual((float) player.fallDistance, 0.0F, "the fall distance of a flight");
        helper.succeed();
    }

    @GameTest
    public void theLastTicksOfTheGaugeSputter(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilGauge(player, FlightAbility.DEFAULT_SPUTTER);

        helper.assertTrue(player.isFlying(), "the player is not flying, the test proves nothing");
        helper.assertFalse(player.isFlightSputtering(), "the flight sputtered before the end of the gauge");

        double lowest = Double.MAX_VALUE;
        double highest = -Double.MAX_VALUE;
        while (player.getFlightGauge() > 1) {
            double before = player.getY();
            AirPlayers.tick(player, JUMP_HELD, 1);
            helper.assertTrue(player.isFlightSputtering(), "the end of the gauge should sputter");
            lowest = Math.min(lowest, player.getY() - before);
            highest = Math.max(highest, player.getY() - before);
        }

        helper.assertTrue(lowest <= 0.0D, "a sputtering flight should stop lifting for a tick, lowest was " + lowest);
        helper.assertTrue(highest > 0.0D, "a sputtering flight should still lift in between, highest was " + highest);
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void anEmptyGaugeDropsThePlayer(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilEmpty(player);

        double top = player.getY();
        AirPlayers.tick(player, JUMP_HELD, 5);

        helper.assertFalse(player.isFlying(), "an empty gauge should end the flight even with the key held");
        helper.assertTrue(player.getY() < top, "the player should be falling, was " + player.getY() + " from " + top);
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void anEmptyGaugeBlocksTheRestartUntilItHasRecharged(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilEmpty(player);

        player.refillFlight(SuperMarioFlightRefillIds.COIN);
        AirPlayers.tick(player, JUMP_HELD, 1);
        helper.assertTrue(COIN < RESTART, "one coin is enough to restart, the test proves nothing");
        helper.assertFalse(player.isFlying(), "a gauge short of the restart threshold should not fly");

        player.refillFlight(SuperMarioFlightRefillIds.COIN);
        AirPlayers.tick(player, JUMP_HELD, 1);
        helper.assertTrue(player.isFlying(), "a gauge past the restart threshold should fly again");
        helper.succeed();
    }

    @GameTest(maxTicks = 300)
    public void landingRefillsTheGauge(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 30);
        helper.assertTrue(player.getFlightGauge() < GAUGE, "the gauge never drained, the test proves nothing");

        for (int tick = 0; tick < LANDING_TICKS && !player.onGround(); tick++) {
            AirPlayers.tick(player, NOTHING_HELD, 1);
        }
        helper.assertTrue(player.onGround(), "the player never landed, the test proves nothing");
        AirPlayers.tick(player, NOTHING_HELD, 1);

        helper.assertValueEqual(player.getFlightGauge(), GAUGE, "the gauge once landed");
        helper.succeed();
    }

    @GameTest
    public void aPricedSourceRefillsItsAmount(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 90);
        int before = player.getFlightGauge();

        player.refillFlight(SuperMarioFlightRefillIds.COIN);
        helper.assertValueEqual(player.getFlightGauge(), before + COIN, "the gauge after a coin");

        player.refillFlight(SuperMarioFlightRefillIds.STOMP);
        helper.assertValueEqual(player.getFlightGauge(), before + COIN + STOMP, "the gauge after a stomp");
        helper.succeed();
    }

    @GameTest
    public void aRefillNeverOverfills(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 5);

        player.refillFlight(SuperMarioFlightRefillIds.STOMP);
        player.refillFlight(SuperMarioFlightRefillIds.STOMP);

        helper.assertValueEqual(player.getFlightGauge(), GAUGE, "the gauge after more than it could take");
        helper.succeed();
    }

    @GameTest
    public void anUnpricedSourceRefillsNothing(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 20);
        int before = player.getFlightGauge();

        player.refillFlight(Identifier.parse("mubble-gametest:unpriced"));

        helper.assertValueEqual(player.getFlightGauge(), before, "the gauge after a source nobody priced");
        helper.succeed();
    }

    @GameTest
    public void losingTheFormEndsTheFlightAndResetsTheGauge(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilEmpty(player);

        player.clearPowerUp();
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLIER));

        helper.assertValueEqual(player.getFlightGauge(), GAUGE, "the gauge of a form just obtained");
        AirPlayers.tick(player, JUMP_HELD, 1);
        helper.assertTrue(player.isFlying(), "a new form should not inherit the exhaustion of the old one");
        helper.succeed();
    }

    @GameTest
    public void losingTheFormMidFlightEndsIt(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 5);

        player.clearPowerUp();
        AirPlayers.tick(player, JUMP_HELD, 1);

        helper.assertFalse(player.isFlying(), "a player without the form should not keep flying");
        helper.succeed();
    }

    @GameTest
    public void theGaugeSurvivesSaveAndLoad(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 30);
        int gauge = player.getFlightGauge();

        var loaded = reload(helper, player);

        helper.assertValueEqual(loaded.getFlightGauge(), gauge, "the gauge after a reload");
        helper.succeed();
    }

    @GameTest(maxTicks = 200)
    public void anExhaustedGaugeSurvivesSaveAndLoad(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilEmpty(player);

        var loaded = reload(helper, player);
        loaded.teleportTo(player.getX(), player.getY(), player.getZ());
        AirPlayers.tick(loaded, JUMP_HELD, 2);

        helper.assertValueEqual(loaded.getFlightGauge(), 0, "the gauge after a reload");
        helper.assertFalse(loaded.isFlying(), "a gauge saved empty should still block the flight");
        helper.succeed();
    }

    @GameTest
    public void otherPlayersAreToldAboutTheFlight(GameTestHelper helper) {
        var player = flier(helper);
        AirPlayers.tick(player, JUMP_HELD, 5);
        var viewer = TestPlayers.mock(helper);

        viewer.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());

        helper.assertTrue(viewer.isFlying(), "a player seeing someone fly should know about it");
        helper.assertFalse(viewer.isFlightSputtering(), "the flight is not sputtering yet");
        helper.succeed();
    }

    @GameTest
    public void otherPlayersAreToldAboutTheSputter(GameTestHelper helper) {
        var player = flier(helper);
        Arena.openTheSky(helper, STAND);
        flyUntilGauge(player, 1);
        var viewer = TestPlayers.mock(helper);

        viewer.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());

        helper.assertTrue(viewer.isFlightSputtering(), "a player seeing someone sputter should know about it");
        helper.succeed();
    }

    @GameTest(maxTicks = 300)
    public void aFullFlightIsNotKickedForFloating(GameTestHelper helper) {
        var player = connected(helper, PowerUpFixtures.FLIER);

        FlightPolicy.denyDuring(() -> climb(player, GAUGE));

        helper.assertTrue(player.connection.isAcceptingMessages(), "a legitimate flight got the player kicked for floating");
        helper.succeed();
    }

    /** The same climb without the form, which is what the kick is there for. */
    @GameTest(maxTicks = 300)
    public void aClimbWithoutTheFormIsKickedForFloating(GameTestHelper helper) {
        var player = connected(helper, PowerUpFixtures.EMPTY);

        FlightPolicy.denyDuring(() -> climb(player, GAUGE));

        helper.assertFalse(player.connection.isAcceptingMessages(), "a player climbing the air should have been kicked");
        helper.succeed();
    }

    /** Once the gauge has run out, the server has no flight left to believe in. */
    @GameTest(maxTicks = 400)
    public void aClimbPastTheGaugeIsKickedForFloating(GameTestHelper helper) {
        var player = connected(helper, PowerUpFixtures.FLIER);

        FlightPolicy.denyDuring(() -> climb(player, 2 * GAUGE + KICK_TICKS));

        helper.assertFalse(player.connection.isAcceptingMessages(), "a player still climbing with an empty gauge should have been kicked");
        helper.succeed();
    }

    /** A player in open air whose connection has been told their position, as it is once they have joined. */
    private static ServerPlayer connected(GameTestHelper helper, ResourceKey<PowerUp> powerUp) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, STAND);
        var player = TestPlayers.at(helper, STAND.above(3));
        player.setGameMode(GameType.SURVIVAL);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        player.setOnGround(false);
        acceptPendingTeleport(player.connection);
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        return player;
    }

    private static void acceptPendingTeleport(ServerGamePacketListenerImpl connection) {
        try {
            var pending = ServerGamePacketListenerImpl.class.getDeclaredField("awaitingPositionFromClient");
            var id = ServerGamePacketListenerImpl.class.getDeclaredField("awaitingTeleport");
            pending.setAccessible(true);
            id.setAccessible(true);
            if (pending.get(connection) != null) {
                connection.handleAcceptTeleportPacket(new ServerboundAcceptTeleportationPacket(id.getInt(connection)));
            }
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("the connection no longer keeps its teleport where this test looks for it", e);
        }
    }

    /** The server ticks the connection and the client answers with a step up, holding the jump key all along. */
    private static void climb(ServerPlayer player, int ticks) {
        for (int tick = 0; tick < ticks && player.connection.isAcceptingMessages(); tick++) {
            player.setLastClientInput(JUMP_HELD);
            player.connection.tick();
            player.connection.handleMovePlayer(new ServerboundMovePlayerPacket.Pos(player.getX(), player.getY() + CLIMB, player.getZ(), false, false));
        }
    }

    private static void flyUntilEmpty(ServerPlayer player) {
        flyUntilGauge(player, 0);
    }

    private static void flyUntilGauge(ServerPlayer player, int gauge) {
        for (int tick = 0; tick < 2 * GAUGE && player.getFlightGauge() > gauge; tick++) {
            AirPlayers.tick(player, JUMP_HELD, 1);
        }
    }

    private static ServerPlayer flier(GameTestHelper helper) {
        return AirPlayers.jumper(helper, PowerUpFixtures.FLIER);
    }

    private static ServerPlayer reload(GameTestHelper helper, Player player) {
        var registries = helper.getLevel().registryAccess();
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        player.saveWithoutId(output);

        var reloaded = TestPlayers.inLevel(helper);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));
        return reloaded;
    }
}

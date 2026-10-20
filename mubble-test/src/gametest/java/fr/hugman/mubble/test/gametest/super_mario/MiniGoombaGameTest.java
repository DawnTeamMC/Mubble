package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.Stompable;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoomba;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.MiniGoombaCarrier;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * The mini goomba, which fights by holding on rather than by biting.
 * <p>
 * Catching a player takes it out of the world and leaves a number behind on them, so most of what is
 * checked here is that the two sides of that trade stay in step: the entity goes, the count goes up,
 * the player is weighed down, and shaking hard enough puts a live mini goomba back on the ground.
 */
public class MiniGoombaGameTest {
    private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    /** How fast a stomper has to be falling, as in {@link StompGameTest}. */
    private static final Vec3 FALLING = new Vec3(0.0D, -0.2D, 0.0D);

    @GameTest
    public void aminiGoombaHasOneHitPoint(GameTestHelper helper) {
        var mini = miniGoomba(helper);

        helper.assertValueEqual(mini.getMaxHealth(), 1.0F, "a mini goomba's health");
        helper.succeed();
    }

    /**
     * There is far less underfoot to push off, so a stomp does not carry the stomper as high.
     * <p>
     * The bounce is read off the stomped entity by the entity mixin, so this goes through a real stomp
     * rather than only asking the mini goomba what it would give: a bounce the mixin never consults
     * would pass the second check and fail the player.
     */
    @GameTest
    public void stompingAMiniGoombaThrowsTheStomperLessHigh(GameTestHelper helper) {
        var mini = miniGoomba(helper);
        var player = stomper(helper);

        mini.tick();

        helper.assertTrue(mini.getStompBounce() < Stompable.DEFAULT_STOMP_BOUNCE,
                "a mini goomba should give a smaller bounce than a full-grown goomba");
        helper.assertValueEqual(player.getDeltaMovement().y, MiniGoomba.STOMP_BOUNCE, "the bounce a mini goomba gives");
        helper.succeed();
    }

    /** The same stomp on a full-grown goomba, for the bounce above to be smaller than something. */
    @GameTest
    public void stompingAGoombaThrowsTheStomperTheUsualHeight(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
        var player = stomper(helper);

        goomba.tick();

        helper.assertValueEqual(player.getDeltaMovement().y, Stompable.DEFAULT_STOMP_BOUNCE, "the bounce a goomba gives");
        helper.succeed();
    }

    /** Reaching a player is the whole attack: the mini goomba stops being an entity and holds on. */
    @GameTest
    public void reachingAPlayerTakesTheMiniGoombaOutOfTheWorld(GameTestHelper helper) {
        var mini = miniGoomba(helper);
        var player = TestPlayers.at(helper, GROUND);

        helper.assertTrue(mini.doHurtTarget(helper.getLevel(), player), "the mini goomba failed to hold on");

        helper.assertTrue(mini.isRemoved(), "a clinging mini goomba should no longer be an entity");
        helper.assertValueEqual(carrier(player).getClingingMiniGoombas(), 1, "the mini goombas clinging to the player");
        helper.succeed();
    }

    /** Being weighed down is the price of being caught, not the damage: they never take any health. */
    @GameTest
    public void holdingOnCostsThePlayerNoHealth(GameTestHelper helper) {
        var mini = miniGoomba(helper);
        var player = TestPlayers.at(helper, GROUND);
        float health = player.getHealth();

        mini.doHurtTarget(helper.getLevel(), player);
        TestPlayers.tick(player);

        helper.assertValueEqual(player.getHealth(), health, "the player's health under a clinging mini goomba");
        helper.succeed();
    }

    @GameTest
    public void holdingOnSlowsThePlayerDownAndCutsTheirJumpShort(GameTestHelper helper) {
        var player = TestPlayers.at(helper, GROUND);
        double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
        double jump = player.getAttributeValue(Attributes.JUMP_STRENGTH);

        carrier(player).setClingingMiniGoombas(1);
        TestPlayers.tick(player);

        helper.assertTrue(player.getAttributeValue(Attributes.MOVEMENT_SPEED) < speed,
                "a clinging mini goomba should slow the player down");
        helper.assertTrue(player.getAttributeValue(Attributes.JUMP_STRENGTH) < jump,
                "a clinging mini goomba should cut the player's jump short");
        helper.succeed();
    }

    /** More of them weigh more, which is the whole reason they come in swarms. */
    @GameTest
    public void twoOfThemWeighMoreThanOne(GameTestHelper helper) {
        var player = TestPlayers.at(helper, GROUND);

        carrier(player).setClingingMiniGoombas(1);
        TestPlayers.tick(player);
        double one = player.getAttributeValue(Attributes.MOVEMENT_SPEED);

        carrier(player).setClingingMiniGoombas(2);
        TestPlayers.tick(player);

        helper.assertTrue(player.getAttributeValue(Attributes.MOVEMENT_SPEED) < one,
                "a second mini goomba should slow the player down further");
        helper.succeed();
    }

    /** Letting go gives the player their speed back, rather than leaving the penalty behind. */
    @GameTest
    public void lettingGoGivesThePlayerTheirSpeedBack(GameTestHelper helper) {
        var player = TestPlayers.at(helper, GROUND);
        double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);

        carrier(player).setClingingMiniGoombas(3);
        TestPlayers.tick(player);
        carrier(player).setClingingMiniGoombas(0);
        TestPlayers.tick(player);

        helper.assertValueEqual(player.getAttributeValue(Attributes.MOVEMENT_SPEED), speed,
                "the player's speed once nothing is holding on any more");
        helper.succeed();
    }

    @GameTest
    public void onlySoManyCanHoldOnAtOnce(GameTestHelper helper) {
        var player = TestPlayers.at(helper, GROUND);
        var carrier = carrier(player);
        carrier.setClingingMiniGoombas(MiniGoomba.MAX_CLINGING);

        var mini = miniGoomba(helper);
        helper.assertFalse(mini.doHurtTarget(helper.getLevel(), player), "a full player should have no hold left to offer");

        helper.assertFalse(mini.isRemoved(), "a mini goomba that found no hold should stay an entity");
        helper.assertValueEqual(carrier.getClingingMiniGoombas(), MiniGoomba.MAX_CLINGING, "the mini goombas clinging to a full player");
        helper.succeed();
    }

    /**
     * Sneak is the shake. Every press and every release counts, so the player is made to hammer it and
     * one mini goomba should come loose and land back in the world alive.
     */
    @GameTest
    public void spammingSneakShakesOneOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, GROUND);
        carrier(player).setClingingMiniGoombas(2);

        struggle(player, 4);

        helper.assertValueEqual(carrier(player).getClingingMiniGoombas(), 1, "the mini goombas left after a struggle");
        helper.assertTrue(miniGoombasAround(helper, player) == 1, "the mini goomba that came off should be back in the world");
        helper.succeed();
    }

    /** Holding sneak down is not shaking: it is one press, and one press is not enough. */
    @GameTest
    public void holdingSneakDownShakesNothingOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, GROUND);
        carrier(player).setClingingMiniGoombas(2);

        player.setShiftKeyDown(true);
        for (int i = 0; i < 20; i++) {
            TestPlayers.tick(player);
        }

        helper.assertValueEqual(carrier(player).getClingingMiniGoombas(), 2, "the mini goombas left after merely crouching");
        helper.succeed();
    }

    /** Without a grace period the struggle would be pointless: it would latch on again the next tick. */
    @GameTest
    public void ashakenOffMiniGoombaCannotCatchOnAgainRightAway(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, GROUND);

        var mini = MiniGoomba.shakeOff(helper.getLevel(), player);
        helper.assertTrue(mini != null, "shaking one off should put a mini goomba back in the world");
        helper.assertTrue(mini.getClingCooldown() > 0, "a shaken-off mini goomba should be kept off for a while");

        helper.assertFalse(mini.doHurtTarget(helper.getLevel(), player), "it caught on again straight away");
        helper.assertValueEqual(carrier(player).getClingingMiniGoombas(), 0, "the mini goombas clinging right after a struggle");
        helper.succeed();
    }

    @GameTest
    public void theGracePeriodRunsOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, GROUND);

        var mini = MiniGoomba.shakeOff(helper.getLevel(), player);
        for (int i = 0; i <= MiniGoomba.SHAKE_OFF_COOLDOWN; i++) {
            mini.tick();
        }

        helper.assertValueEqual(mini.getClingCooldown(), 0, "the grace period left on a mini goomba");
        helper.assertTrue(mini.doHurtTarget(helper.getLevel(), player), "it should be able to hold on again by now");
        helper.succeed();
    }

    /** A player hanging in the air just above the arena floor, on their way down. */
    private static ServerPlayer stomper(GameTestHelper helper) {
        var player = TestPlayers.at(helper, GROUND.above());
        player.setGameMode(GameType.SURVIVAL);
        player.setOnGround(false);
        player.setDeltaMovement(FALLING);
        return player;
    }

    /** Hammers sneak for {@code toggles} presses and releases, one per tick. */
    private static void struggle(ServerPlayer player, int toggles) {
        for (int i = 0; i < toggles; i++) {
            player.setShiftKeyDown(i % 2 == 0);
            TestPlayers.tick(player);
        }
    }

    private static int miniGoombasAround(GameTestHelper helper, ServerPlayer player) {
        return helper.getLevel().getEntitiesOfClass(MiniGoomba.class, AABB.ofSize(player.position(), 8.0D, 8.0D, 8.0D)).size();
    }

    private static MiniGoombaCarrier carrier(ServerPlayer player) {
        return (MiniGoombaCarrier) player;
    }

    private static MiniGoomba miniGoomba(GameTestHelper helper) {
        Arena.buildFloor(helper);
        return helper.spawnWithNoFreeWill(SuperMarioEntityTypes.MINI_GOOMBA, GROUND);
    }
}

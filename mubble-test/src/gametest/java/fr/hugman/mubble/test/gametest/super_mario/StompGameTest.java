package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.Goomba;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

/**
 * Jumping on an enemy. The whole thing hangs off one predicate in the entity mixin, so each test here
 * takes away exactly one of the things that predicate asks for.
 * <p>
 * The check runs at the head of the stomped entity's tick, which is why the goomba is ticked by hand:
 * it keeps the moment of the stomp exact rather than depending on where the level is in its loop.
 */
public class StompGameTest {
    private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    /** How fast a stomper has to be falling: the predicate wants less than 0.3 upwards. */
    private static final Vec3 FALLING = new Vec3(0.0D, -0.2D, 0.0D);

    @GameTest
    public void aFallingPlayerHurtsWhatTheyLandOn(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();
        stomper(helper, GameType.SURVIVAL);

        goomba.tick();

        helper.assertTrue(goomba.getHealth() < health, "the goomba walked away from a stomp unharmed");
        helper.succeed();
    }

    @GameTest
    public void aStompBouncesTheStomper(GameTestHelper helper) {
        var goomba = goomba(helper);
        var player = stomper(helper, GameType.SURVIVAL);

        goomba.tick();

        helper.assertTrue(player.getDeltaMovement().y > 0.0D, "the stomper did not bounce back up");
        helper.assertValueEqual((float) player.fallDistance, 0.0F, "the fall distance after a bounce");
        helper.succeed();
    }

    /**
     * A power-up tagged {@code disables_stomping} takes the damage out of a stomp, but the bounce is
     * what makes the enemy feel like a platform, so it has to stay.
     */
    @GameTest
    public void astompProofPowerUpTakesTheDamageOutButKeepsTheBounce(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();
        var player = stomper(helper, GameType.SURVIVAL);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.STOMP_PROOF));

        goomba.tick();

        helper.assertValueEqual(goomba.getHealth(), health, "the goomba's health under a stomp-proof power-up");
        helper.assertTrue(player.getDeltaMovement().y > 0.0D, "the stomper should still bounce");
        helper.succeed();
    }

    /**
     * The stomped flag is what swaps the death sound and the death animation for the flattened ones,
     * so a stomp that actually lands has to raise it.
     */
    @GameTest
    public void alandedStompMarksTheEnemyAsStomped(GameTestHelper helper) {
        var goomba = goomba(helper);
        stomper(helper, GameType.SURVIVAL);

        goomba.tick();

        helper.assertTrue(goomba.isStomped(), "a goomba that took a stomp should be marked as stomped");
        helper.succeed();
    }

    /** A jump that deals no damage is not a stomp, whatever it looks like from above. */
    @GameTest
    public void astompProofPowerUpDoesNotMarkTheEnemyAsStomped(GameTestHelper helper) {
        var goomba = goomba(helper);
        var player = stomper(helper, GameType.SURVIVAL);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.STOMP_PROOF));

        goomba.tick();

        helper.assertFalse(goomba.isStomped(), "a harmless jump should leave the goomba unmarked");
        helper.succeed();
    }

    /**
     * A goomba jumped on by a player who cannot stomp, then killed by hand, used to die flattened: the
     * jump raised the flag for good, and every later death read it.
     */
    @GameTest
    public void aharmlessJumpDoesNotFlattenAlaterDeath(GameTestHelper helper) {
        var goomba = goomba(helper);
        var player = stomper(helper, GameType.SURVIVAL);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.STOMP_PROOF));

        goomba.tick();
        kill(helper, goomba, player);

        helper.assertFalse(goomba.isStomped(), "a goomba killed by hand should not die flattened");
        helper.succeed();
    }

    /**
     * Same story for a mark an enemy carries over from any earlier stomp: the hit it dies from is the one
     * that decides how it dies. A goomba cannot show that on its own, since {@code instant_kills_goombas}
     * makes every stomp that reaches it lethal, so the mark is set by hand here.
     */
    @GameTest
    public void astaleStompMarkDoesNotFlattenAlaterDeath(GameTestHelper helper) {
        var goomba = goomba(helper);
        var player = TestPlayers.at(helper, GROUND.above());
        goomba.setStomped(true);

        kill(helper, goomba, player);

        helper.assertFalse(goomba.isStomped(), "a goomba killed by hand should not die flattened");
        helper.succeed();
    }

    @GameTest
    public void anUntaggedPowerUpStillStomps(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();
        var player = stomper(helper, GameType.SURVIVAL);
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.EMPTY));

        goomba.tick();

        helper.assertTrue(goomba.getHealth() < health, "only power-ups tagged disables_stomping should soften a stomp");
        helper.succeed();
    }

    @GameTest
    public void aPlayerMovingUpwardsDoesNotStomp(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();
        var player = stomper(helper, GameType.SURVIVAL);
        player.setDeltaMovement(0.0D, 0.5D, 0.0D);

        goomba.tick();

        helper.assertValueEqual(goomba.getHealth(), health, "jumping up through an enemy should not stomp it");
        helper.succeed();
    }

    @GameTest
    public void aPlayerStandingOnTheGroundDoesNotStomp(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();
        var player = stomper(helper, GameType.SURVIVAL);
        player.setOnGround(true);

        goomba.tick();

        helper.assertValueEqual(goomba.getHealth(), health, "walking into an enemy should not stomp it");
        helper.succeed();
    }

    /**
     * Only what the {@code can_stomp} tag names may stomp, which today is the player alone. Spectators
     * are filtered out by the same predicate, but a mock player cannot be put into spectator mode, so
     * that half stays uncovered here.
     */
    @GameTest
    public void anEntityOutsideTheTagDoesNotStomp(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();

        var pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, GROUND.above());
        pig.setOnGround(false);
        pig.setDeltaMovement(FALLING);

        goomba.tick();

        helper.assertValueEqual(goomba.getHealth(), health, "a pig falling on a goomba should not stomp it");
        helper.succeed();
    }

    @GameTest
    public void anEmptyStompBoxHurtsNobody(GameTestHelper helper) {
        var goomba = goomba(helper);
        float health = goomba.getHealth();

        goomba.tick();

        helper.assertValueEqual(goomba.getHealth(), health, "the goomba hurt itself with nobody around");
        helper.succeed();
    }

    /**
     * Kills {@code goomba} with a plain melee hit. The invulnerability left by an earlier hit is wound
     * back first, so that this one is the hit the goomba actually dies from.
     */
    private static void kill(GameTestHelper helper, Goomba goomba, ServerPlayer player) {
        goomba.invulnerableTime = 0;
        goomba.hurtServer(helper.getLevel(), goomba.damageSources().playerAttack(player), Float.MAX_VALUE);
    }

    private static Goomba goomba(GameTestHelper helper) {
        Arena.buildFloor(helper);
        return helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);
    }

    /** A player hanging in the air just above the goomba, on their way down. */
    private static ServerPlayer stomper(GameTestHelper helper, GameType mode) {
        var player = TestPlayers.at(helper, GROUND.above());
        player.setGameMode(mode);
        player.setOnGround(false);
        player.setDeltaMovement(FALLING);
        return player;
    }
}

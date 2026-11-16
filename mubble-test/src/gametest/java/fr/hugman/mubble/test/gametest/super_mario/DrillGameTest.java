package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;

/**
 * The drill of the {@code driller.json} fixture, at 0.8 blocks per tick: started by a fresh press of sneak
 * in the air, chewing through the bricks under it and stopping on anything else.
 */
public class DrillGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double DRILL_SPEED = 0.8D;
    private static final double EPSILON = 1.0E-4D;

    private static final Input SNEAK = TestPlayers.holdingSneak();
    private static final Input NOTHING = Input.EMPTY;

    @GameTest
    public void aFreshSneakInTheAirStartsTheDrill(GameTestHelper helper) {
        var player = airborne(helper, 5);

        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(player.isDrilling(), "sneaking in the air should start the drill");

        for (int tick = 0; tick < 3; tick++) {
            double before = player.getY();
            TestPlayers.tick(player, SNEAK);
            helper.assertTrue(Math.abs(before - player.getY() - DRILL_SPEED) < EPSILON,
                    "a drill should come down at its speed, came down " + (before - player.getY()));
        }
        helper.succeed();
    }

    @GameTest
    public void theDrillIsFasterThanAPlainFall(GameTestHelper helper) {
        var drilling = airborne(helper, 5);
        var plain = airborne(helper, 5);
        plain.teleportTo(plain.getX() - 2.0D, plain.getY(), plain.getZ());

        tick(drilling, SNEAK, 4);
        tick(plain, NOTHING, 4);

        helper.assertTrue(drilling.getY() < plain.getY(), "a drilling player should be lower than a falling one");
        helper.succeed();
    }

    @GameTest
    public void aSneakHeldWhenLeavingTheGroundDoesNotDrill(GameTestHelper helper) {
        var player = player(helper, STAND);
        tick(player, SNEAK, 2);
        player.teleportTo(player.getX(), player.getY() + 4.0D, player.getZ());

        tick(player, SNEAK, 3);

        helper.assertFalse(player.onGround(), "the player is not in the air, the test proves nothing");
        helper.assertFalse(player.isDrilling(), "a sneak held since the ground should not start the drill");
        helper.succeed();
    }

    @GameTest
    public void nothingDrillsOnTheGround(GameTestHelper helper) {
        var player = player(helper, STAND);

        TestPlayers.tick(player, NOTHING);
        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isDrilling(), "sneaking on the ground should not drill");
        helper.succeed();
    }

    @GameTest
    public void landingEndsTheDrill(GameTestHelper helper) {
        var player = drilling(helper, 5);

        tick(player, SNEAK, 10);

        helper.assertTrue(player.onGround(), "the player never landed, the test proves nothing");
        helper.assertFalse(player.isDrilling(), "landing should end the drill");
        helper.succeed();
    }

    @GameTest
    public void sneakingDuringTheSlowFallStartsTheDrill(GameTestHelper helper) {
        var player = player(helper, STAND);
        Arena.openTheSky(helper, STAND);
        TestPlayers.tick(player, TestPlayers.holdingJump());
        TestPlayers.tick(player, NOTHING);
        TestPlayers.tick(player, TestPlayers.holdingJump());
        for (int tick = 0; tick < 40 && player.isLaunching(); tick++) {
            TestPlayers.tick(player, NOTHING);
        }
        helper.assertTrue(player.isSlowFalling(), "the slow fall never started, the test proves nothing");

        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isSlowFalling(), "the drill should cancel the slow fall");
        helper.assertTrue(player.isDrilling(), "sneaking during the slow fall should start the drill");
        helper.succeed();
    }

    /**
     * Ticked the way a client moves: the bricks are never solid to the drill, so it never lands on one for a
     * tick and loses its speed, and each of them is gone once the drill has gone through.
     */
    @GameTest
    public void theDrillChewsThroughAStackOfBricksWithoutStopping(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int y = 0; y < 3; y++) {
            helper.setBlock(STAND.above(y), SuperMarioBlocks.BRICK_BLOCK);
        }
        var player = drillingAbove(helper, STAND.above(5));

        int ticks = 0;
        while (player.getY() - DRILL_SPEED > helper.absolutePos(STAND).getY() && ticks++ < 20) {
            double before = player.getY();
            TestPlayers.tick(player, SNEAK);
            helper.assertTrue(Math.abs(before - player.getY() - DRILL_SPEED) < EPSILON,
                    "the drill should keep its speed through the bricks, came down " + (before - player.getY()) + " at " + before);
        }
        tick(player, SNEAK, 2);

        for (int y = 0; y < 3; y++) {
            helper.assertBlockPresent(Blocks.AIR, STAND.above(y));
        }
        helper.assertTrue(player.onGround(), "the drill should have gone all the way down to the floor");
        helper.assertFalse(player.isDrilling(), "landing on the floor should end the drill");
        helper.succeed();
    }

    /** The predicate the client moves with, before the server has broken anything. */
    @GameTest
    public void aBrickIsNotSolidToADrillAboutToSmashIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var drilling = drillingAbove(helper, STAND.above(5));
        var standing = player(helper, STAND.above(5).east(2));
        helper.setBlock(STAND.above(3), SuperMarioBlocks.BRICK_BLOCK);
        helper.setBlock(STAND.above(3).east(2), SuperMarioBlocks.BRICK_BLOCK);
        helper.setBlock(STAND.above(1), SuperMarioBlocks.QUESTION_BLOCK);

        var level = helper.getLevel();
        double insideTheBrick = helper.absolutePos(STAND.above(3)).getY() + 0.5D;
        double insideTheQuestionBlock = helper.absolutePos(STAND.above(1)).getY() + 0.5D;
        helper.assertTrue(level.noCollision(drilling, feetAt(drilling, insideTheBrick)),
                "a brick under a drilling player should not be solid to them");
        helper.assertFalse(level.noCollision(standing, feetAt(standing, insideTheBrick)),
                "a brick should stay solid to a player who is not drilling");
        helper.assertFalse(level.noCollision(drilling, feetAt(standing, insideTheBrick)),
                "a brick beside the column of a drilling player should stay solid to them");
        helper.assertFalse(level.noCollision(drilling, feetAt(drilling, insideTheQuestionBlock)),
                "a question block, which does not break, should stay solid to the drill");
        helper.succeed();
    }

    @GameTest
    public void aQuestionBlockIsBumpedAndStopsTheDrill(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(STAND, SuperMarioBlocks.QUESTION_BLOCK);
        var player = drillingAbove(helper, STAND.above(4));

        tick(player, SNEAK, 8);

        var blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(STAND), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK);
        helper.assertTrue(blockEntity.isPresent(), "the question block should still be there");
        helper.assertTrue(blockEntity.get().isBumping(), "the drill should bump the question block");
        helper.assertTrue(player.onGround(), "the drill should land on the question block");
        helper.assertFalse(player.isDrilling(), "landing on the question block should end the drill");
        helper.assertTrue(player.getY() > helper.absolutePos(STAND).getY(), "the drill should stop on top of the question block");
        helper.succeed();
    }

    @GameTest
    public void losingTheFormMidAirEndsTheDrill(GameTestHelper helper) {
        var player = drilling(helper, 5);

        player.clearPowerUp();
        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isDrilling(), "losing the form should end the drill");
        helper.succeed();
    }

    @GameTest
    public void waterEndsTheDrill(GameTestHelper helper) {
        var player = drilling(helper, 5);
        for (int y = Arena.FLOOR_Y + 1; y < Arena.SIZE; y++) {
            helper.setBlock(new BlockPos(STAND.getX(), y, STAND.getZ()), Blocks.WATER);
        }

        TestPlayers.tick(player, SNEAK);

        helper.assertTrue(player.isInWater(), "the player is not in the water, the test proves nothing");
        helper.assertFalse(player.isDrilling(), "water should end the drill");
        helper.succeed();
    }

    @GameTest
    public void theDrillStateIsSaved(GameTestHelper helper) {
        var player = drilling(helper, 5);
        var registries = helper.getLevel().registryAccess();
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        player.saveWithoutId(output);

        Player reloaded = TestPlayers.mock(helper);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));

        helper.assertTrue(reloaded.isDrilling(), "a drill should carry on through a save");
        helper.succeed();
    }

    @GameTest
    public void theDrillFlagReachesTheOtherPlayers(GameTestHelper helper) {
        var player = drilling(helper, 5);

        var watched = TestPlayers.mock(helper);
        watched.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());

        helper.assertTrue(watched.isDrilling(), "the drill flag should reach the other players");
        helper.succeed();
    }

    private static ServerPlayer player(GameTestHelper helper, BlockPos pos) {
        Arena.buildFloor(helper);
        var player = TestPlayers.survival(TestPlayers.at(helper, pos));
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.DRILLER));
        TestPlayers.tick(player, NOTHING);
        return player;
    }

    private static ServerPlayer airborne(GameTestHelper helper, int height) {
        var player = player(helper, STAND.above(height));
        helper.assertFalse(player.onGround(), "the player is not in the air, the test proves nothing");
        return player;
    }

    private static ServerPlayer drilling(GameTestHelper helper, int height) {
        var player = airborne(helper, height);
        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(player.isDrilling(), "the drill never started, the test proves nothing");
        return player;
    }

    /** Placed at {@code pos} without a tick, then drilling from there on its first one. */
    private static ServerPlayer drillingAbove(GameTestHelper helper, BlockPos pos) {
        var player = TestPlayers.survival(TestPlayers.at(helper, pos));
        player.setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.DRILLER));
        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(player.isDrilling(), "the drill never started, the test proves nothing");
        return player;
    }

    private static AABB feetAt(ServerPlayer player, double y) {
        return player.getBoundingBox().move(0.0D, y - player.getY(), 0.0D);
    }

    private static void tick(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}

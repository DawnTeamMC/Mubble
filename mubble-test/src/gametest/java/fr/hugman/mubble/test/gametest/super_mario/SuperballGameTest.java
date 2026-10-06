package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.Superball;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * In-game behaviour of the superball thrown by the Superball Flower: it ricochets off whatever it meets
 * instead of bursting on it, sweeps up the coins it crosses, and goes out on its own timer.
 */
public class SuperballGameTest {
    /** The layer the ball flies through: the one just above the floor, where everything else stands. */
    private static final int BALL_Y = Arena.FLOOR_Y + 1;
    /** Speed every test throws at, in blocks per tick. Fast enough to cross the arena, slow enough to watch. */
    private static final double SPEED = 0.4D;

    private static final BlockPos FAR_WALL = new BlockPos(4, BALL_Y, 6);
    private static final BlockPos NEAR_WALL = new BlockPos(4, BALL_Y, 1);
    /** Where something standing in the ball's path goes. */
    private static final BlockPos TARGET_POS = new BlockPos(4, BALL_Y, 5);

    @GameTest(maxTicks = 100)
    public void reboundsOnTheFaceItHits(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(FAR_WALL, Blocks.STONE);

        var superball = throwBall(helper, SPEED);

        helper.failIfEver(() -> helper.assertFalse(superball.isRemoved(), "the superball burst on the wall instead of bouncing off it"));
        helper.succeedWhen(() -> {
            Vec3 movement = superball.getDeltaMovement();
            helper.assertTrue(movement.z < 0.0D, "the superball has not bounced off the wall yet");
            // A reflection on the face of the wall only flips the axis it came in on, and hands the ball
            // back all of the speed it arrived with.
            helper.assertTrue(Math.abs(movement.z + SPEED) < 1.0E-6D, "the superball did not keep its speed through the bounce");
            helper.assertTrue(Math.abs(movement.x) < 1.0E-6D && Math.abs(movement.y) < 1.0E-6D,
                    "the bounce moved the superball off the axis it came in on");
        });
    }

    /**
     * Where the other balls of the mod are counted out in rebounds, the superball only answers to its
     * lifetime: it keeps bouncing between two walls for the whole four seconds and then goes out.
     */
    @GameTest(maxTicks = 200)
    public void bouncesUntilItsLifetimeRunsOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(FAR_WALL, Blocks.STONE);
        helper.setBlock(NEAR_WALL, Blocks.STONE);

        var superball = throwBall(helper, SPEED);

        helper.startSequence()
                // By now the ball has crossed the four blocks between the walls the better part of ten times,
                // which is far more bounces than a fire or ice ball is worth.
                .thenIdle(Superball.LIFETIME - 20)
                .thenExecute(() -> helper.assertFalse(superball.isRemoved(), "the superball ran out of bounces before its lifetime was up"))
                .thenIdle(25)
                .thenExecute(() -> helper.assertTrue(superball.isRemoved(), "the superball outlived its four seconds"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void collectsTheCoinsItFliesThrough(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, new BlockPos(1, BALL_Y, 1));

        var coin = helper.spawn(MubbleEntityTypes.COLLECTIBLE, TARGET_POS);
        coin.setItem(new ItemStack(SuperMarioItems.COIN));
        // Pinned in place, so the test is about where the ball goes rather than about where the coin fell to.
        coin.setFixed(true);

        var superball = throwBall(helper, SPEED);
        superball.setOwner(player);

        helper.succeedWhen(() -> {
            helper.assertTrue(coin.isRemoved(), "the superball flew straight through the coin without taking it");
            helper.assertTrue(player.getInventory().contains(new ItemStack(SuperMarioItems.COIN)),
                    "the coin never made it to the player who threw the superball");
            // Coins are swept up rather than hit: taking one costs the ball nothing.
            helper.assertFalse(superball.isRemoved(), "picking a coin up put the superball out");
        });
    }

    /** Bringing anything else back is the Boomerang Flower's job, so everything but coins is left alone. */
    @GameTest(maxTicks = 60)
    public void leavesEverythingThatIsNotACoinWhereItIs(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, new BlockPos(1, BALL_Y, 1));

        var ingot = helper.spawn(MubbleEntityTypes.COLLECTIBLE, TARGET_POS);
        ingot.setItem(new ItemStack(Items.GOLD_INGOT));
        ingot.setFixed(true);

        var superball = throwBall(helper, SPEED);
        superball.setOwner(player);

        helper.startSequence()
                .thenIdle(40)
                .thenExecute(() -> {
                    helper.assertFalse(ingot.isRemoved(), "the superball made off with something that is not a coin");
                    helper.assertFalse(player.getInventory().contains(new ItemStack(Items.GOLD_INGOT)),
                            "the player was handed something that is not a coin");
                })
                .thenSucceed();
    }

    /** As in Super Mario Land, an invulnerable target is not a target at all: the ball goes straight through. */
    @GameTest(maxTicks = 60)
    public void passesThroughInvulnerableEntities(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET_POS);
        pig.setInvulnerable(true);
        float health = pig.getHealth();

        var superball = throwBall(helper, SPEED);

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> {
                    helper.assertTrue(pig.getHealth() == health, "the superball hurt an invulnerable pig");
                    helper.assertFalse(superball.isRemoved(), "the superball went out on an invulnerable pig");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void hurtsWhatItHits(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET_POS);

        var superball = throwBall(helper, SPEED);

        helper.succeedWhen(() -> {
            helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the superball did not hurt the pig");
            // A hit is the end of the superball, unlike a bounce off a wall.
            helper.assertTrue(superball.isRemoved(), "the superball carried on after striking the pig");
        });
    }

    /** A ball that comes back at you the whole time it is out must never be the one to hurt you. */
    @GameTest(maxTicks = 60)
    public void goesThroughItsOwnerWithoutHurtingThem(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, TARGET_POS);
        float health = player.getHealth();

        var superball = throwBall(helper, SPEED);
        superball.setOwner(player);

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> {
                    helper.assertTrue(player.getHealth() == health, "the superball hurt the player who threw it");
                    helper.assertFalse(superball.isRemoved(), "the superball went out on the player who threw it");
                })
                .thenSucceed();
    }

    /**
     * Throws a superball along the {@code +Z} axis from the near end of the arena, at the height the walls
     * and targets of these tests sit at.
     */
    private static Superball throwBall(GameTestHelper helper, double speed) {
        var superball = helper.spawn(SuperMarioEntityTypes.SUPERBALL, new Vec3(4.5D, BALL_Y + 0.5D, 3.5D));
        superball.setDeltaMovement(0.0D, 0.0D, speed);
        return superball;
    }
}

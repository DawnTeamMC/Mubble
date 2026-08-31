package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.Flower;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * The huge flower the Super Flower Pot grows: it rises on its own, defeats whatever it grows through,
 * and runs out of both time and height so that it never climbs forever.
 */
public class FlowerGameTest {
    /** Where a flower is grown from, in structure-relative coordinates. */
    private static final BlockPos GROUND = new BlockPos(3, Arena.FLOOR_Y + 1, 3);
    /** Enough ticks for a flower on its own numbers to be well on its way, and none of them wasted. */
    private static final int RISING_TICKS = 6;

    @GameTest
    public void aFlowerRisesStraightUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        Vec3 start = flower.position();

        helper.startSequence()
                .thenIdle(RISING_TICKS)
                .thenExecute(() -> {
                    helper.assertTrue(flower.getY() > start.y(), "the flower should be going up");
                    helper.assertValueEqual(flower.getX(), start.x(), "the x a flower drifted to");
                    helper.assertValueEqual(flower.getZ(), start.z(), "the z a flower drifted to");
                })
                .thenSucceed();
    }

    /**
     * Two flowers grown from the same spot have to follow the exact same path, which they only do as long
     * as nothing touches their speed. Both halves of that are checked from what the flower has climbed so
     * far, rather than from a tick count the test would have to guess at.
     */
    @GameTest
    public void aFlowerRisesAtAConstantSpeed(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        double start = flower.getY();

        helper.startSequence()
                .thenIdle(RISING_TICKS)
                .thenExecute(() -> {
                    helper.assertValueEqual(flower.getDeltaMovement(), new Vec3(0.0D, flower.getSpeed(), 0.0D),
                            "the movement of a flower that gravity and drag should never touch");
                    helper.assertValueEqual(flower.getClimbed(), flower.getSpeed() * flower.tickCount,
                            "the height a flower climbed over its whole life");
                    helper.assertValueEqual(flower.getY() - start, flower.getClimbed(),
                            "the height a flower climbed, against where it actually ended up");
                })
                .thenSucceed();
    }

    /** Rising through ceilings is the whole point: a flower stopping at the first block is useless indoors. */
    @GameTest
    public void aFlowerGrowsThroughBlocks(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        ceilingAt(helper, 5);
        double ceiling = helper.absolutePos(new BlockPos(GROUND.getX(), 5, GROUND.getZ())).getY();

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(flower.getY() > ceiling, "the flower never made it past the ceiling"))
                .thenExecute(() -> helper.assertFalse(flower.isRemoved(), "a flower should grow through a ceiling rather than pop against it"))
                .thenSucceed();
    }

    /** The other behaviour a data pack can ask for: pop against the first solid block instead. */
    @GameTest
    public void aFlowerCanBeStoppedByBlocksInstead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setStoppedByBlocks(true);
        ceilingAt(helper, 5);

        helper.succeedWhen(() -> helper.assertTrue(flower.isRemoved(), "a flower stopped by blocks should pop against the ceiling"));
    }

    @GameTest
    public void aFlowerWiltsOnceItsTimeIsUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(6);
        // Well out of reach, so that the height limit cannot be what ends this one.
        flower.setMaxClimb(Double.MAX_VALUE);

        helper.startSequence()
                .thenIdle(3)
                .thenExecute(() -> helper.assertFalse(flower.isRemoved(), "the flower wilted before its time was up"))
                .thenWaitUntil(() -> helper.assertTrue(flower.isRemoved(), "the flower should wilt once its lifetime has run out"))
                .thenSucceed();
    }

    @GameTest
    public void aFlowerWiltsOnceItHasClimbedFarEnough(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(Integer.MAX_VALUE);
        flower.setMaxClimb(2.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(flower.isRemoved(), "the flower should wilt once it has climbed its whole height");
            helper.assertTrue(flower.getClimbed() >= 2.0D, "the flower wilted before climbing its whole height");
        });
    }

    @GameTest
    public void aFlowerDefeatsWhatItGrowsThrough(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, GROUND.above(2));
        grow(helper);

        helper.succeedWhen(() -> helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the flower should hurt what it grows through"));
    }

    /**
     * One flower is worth one hit per entity, however long it lingers inside their hitbox. It is grown
     * slowly here so that it stays in the pig well past the invulnerability a second hit would land in.
     */
    @GameTest(maxTicks = 200)
    public void aFlowerOnlyEverHitsTheSameEntityOnce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, GROUND);
        var flower = grow(helper);
        flower.setSpeed(0.01D);
        flower.setLifetime(Integer.MAX_VALUE);

        float[] afterFirstHit = new float[1];
        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the pig was never hit at all"))
                .thenExecute(() -> afterFirstHit[0] = pig.getHealth())
                // Twice the invulnerability window a second hit would have to wait out.
                .thenIdle(40)
                .thenExecute(() -> helper.assertValueEqual(pig.getHealth(), afterFirstHit[0],
                        "the health of a pig a single flower grew through"))
                .thenSucceed();
    }

    @GameTest
    public void aFlowerSparesWhoeverGrewIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, GROUND);
        float health = player.getHealth();

        var flower = grow(helper);
        flower.setOwner(player);

        helper.startSequence()
                .thenIdle(RISING_TICKS)
                .thenExecute(() -> helper.assertValueEqual(player.getHealth(), health, "the health of the player who grew the flower"))
                .thenSucceed();
    }

    /** Standing on a flower would turn the form into a lift, which is not what it is for. */
    @GameTest
    public void aFlowerIsNotSomethingToStandOn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        var player = TestPlayers.at(helper, GROUND.above(4));

        helper.assertFalse(flower.canBeCollidedWith(player), "a flower should not be something to stand on");
        helper.succeed();
    }

    /** {@code spawn} takes structure-relative coordinates and works the absolute ones out itself. */
    private static Flower grow(GameTestHelper helper) {
        return helper.spawn(SuperMarioEntityTypes.FLOWER, new Vec3(GROUND.getX() + 0.5D, GROUND.getY(), GROUND.getZ() + 0.5D));
    }

    /** Fills a whole layer of the arena, so that nothing can slip past the ceiling sideways. */
    private static void ceilingAt(GameTestHelper helper, int y) {
        for (int x = 0; x < Arena.SIZE; x++) {
            for (int z = 0; z < Arena.SIZE; z++) {
                helper.setBlock(new BlockPos(x, y, z), Blocks.STONE);
            }
        }
    }
}

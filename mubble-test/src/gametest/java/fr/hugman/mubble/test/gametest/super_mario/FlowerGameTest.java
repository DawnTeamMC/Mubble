package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.Goomba;
import fr.hugman.mubble.super_mario.world.entity.projectile.Flower;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

/**
 * The huge flower the Super Flower Pot grows: it rises on its own, defeats whatever it grows through, ducks
 * out from under the ceilings it meets rather than dying against them, and runs out of both time and
 * distance so that it never travels forever.
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
     * as nothing touches their speed. Both halves of that are checked from what the flower has travelled so
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
                    helper.assertValueEqual(flower.getTravelled(), flower.getSpeed() * flower.tickCount,
                            "the distance a flower travelled over its whole life");
                    helper.assertValueEqual(flower.getY() - start, flower.getTravelled(),
                            "the distance a flower travelled, against where it actually ended up");
                })
                .thenSucceed();
    }

    /** A flower cannot go through blocks: a ceiling makes it duck down and forward rather than stopping it. */
    @GameTest
    public void aCeilingMakesAFlowerDuckDownAndForward(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        // Facing north, so the duck should carry it towards a smaller z.
        flower.setForwardYaw(Direction.NORTH.toYRot());
        ceilingAt(helper, 5);
        double planted = flower.getZ();

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(flower.isEscaping(), "the flower never ducked out of the ceiling"))
                .thenExecute(() -> {
                    helper.assertFalse(flower.isRemoved(), "a ceiling should send a flower on rather than end it");
                    helper.assertTrue(flower.getDeltaMovement().y() < 0.0D,
                            "a flower ducking out should be heading back down, was " + flower.getDeltaMovement());
                    helper.assertTrue(flower.getDeltaMovement().z() < 0.0D,
                            "a flower thrown north should be carried north by its duck, was " + flower.getDeltaMovement());
                })
                .thenIdle(3)
                .thenExecute(() -> helper.assertTrue(flower.getZ() < planted, "the flower never actually moved forward while ducking"))
                .thenSucceed();
    }

    /** The duck is an attempt to get out, not the end of the climb: what follows it is more climbing. */
    @GameTest(maxTicks = 200)
    public void aFlowerClimbsAgainAfterDuckingOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(Integer.MAX_VALUE);
        flower.setRange(Double.MAX_VALUE);
        ceilingAt(helper, 5);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(flower.isEscaping(), "the flower never ducked out of the ceiling"))
                .thenWaitUntil(() -> helper.assertFalse(flower.isEscaping(), "the flower never stopped ducking"))
                .thenExecute(() -> {
                    helper.assertFalse(flower.isRemoved(), "a flower should still be around once it has ducked out");
                    helper.assertValueEqual(flower.getDeltaMovement(), new Vec3(0.0D, flower.getSpeed(), 0.0D),
                            "the movement of a flower that has gone back to climbing");
                })
                .thenSucceed();
    }

    /** Nothing it runs into ends a flower: it only ever runs out of time or of distance. */
    @GameTest(maxTicks = 200)
    public void aFlowerBoxedInKeepsTryingUntilItRunsOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(Integer.MAX_VALUE);
        flower.setRange(8.0D);
        ceilingAt(helper, 5);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(flower.isRemoved(), "the flower never ran out at all"))
                .thenExecute(() -> helper.assertTrue(flower.getTravelled() >= 8.0D,
                        "a boxed-in flower should have run out of distance rather than been stopped, travelled " + flower.getTravelled()))
                .thenSucceed();
    }

    /** A flower hits what it runs into the way any projectile does, which is what target blocks answer to. */
    @GameTest
    public void aFlowerTriggersTargetBlocks(GameTestHelper helper) {
        Arena.buildFloor(helper);
        BlockPos target = new BlockPos(GROUND.getX(), 5, GROUND.getZ());
        helper.setBlock(target, Blocks.TARGET);
        grow(helper);

        helper.succeedWhen(() -> helper.assertTrue(helper.getBlockState(target).getValue(BlockStateProperties.POWER) > 0,
                "the flower should trigger the target block it grows into"));
    }

    /**
     * A flower is a projectile like any other as far as the redstone it moves through is concerned: a whole
     * tripwire, hooks and all, so that the test covers what a player would actually build.
     */
    @GameTest
    public void aFlowerTriggersTripwireHooks(GameTestHelper helper) {
        Arena.buildFloor(helper);
        int y = GROUND.getY() + 3;
        int z = GROUND.getZ();
        // A wire strung between two hooks, running through the column the flower grows in.
        helper.setBlock(new BlockPos(GROUND.getX() - 2, y, z), Blocks.STONE);
        helper.setBlock(new BlockPos(GROUND.getX() + 2, y, z), Blocks.STONE);
        BlockPos hook = new BlockPos(GROUND.getX() - 1, y, z);
        BlockPos farHook = new BlockPos(GROUND.getX() + 1, y, z);
        helper.setBlock(hook, Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.EAST));
        helper.setBlock(new BlockPos(GROUND.getX(), y, z), Blocks.TRIPWIRE);
        helper.setBlock(farHook, Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue(TripWireHookBlock.FACING, Direction.WEST));
        // Placing the blocks outright leaves the hooks unaware of each other; this is the pass vanilla runs
        // when a player puts one down, and what actually strings the wire between them.
        attach(helper, hook);
        attach(helper, farHook);

        helper.assertBlockProperty(hook, TripWireHookBlock.ATTACHED, true);
        grow(helper);

        helper.succeedWhen(() -> helper.assertBlockProperty(hook, TripWireHookBlock.POWERED, true));
    }

    @GameTest
    public void aFlowerWiltsOnceItsTimeIsUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(6);
        // Well out of reach, so that the height limit cannot be what ends this one.
        flower.setRange(Double.MAX_VALUE);

        helper.startSequence()
                .thenIdle(3)
                .thenExecute(() -> helper.assertFalse(flower.isRemoved(), "the flower wilted before its time was up"))
                .thenWaitUntil(() -> helper.assertTrue(flower.isRemoved(), "the flower should wilt once its lifetime has run out"))
                .thenSucceed();
    }

    @GameTest
    public void aFlowerWiltsOnceItHasTravelledFarEnough(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var flower = grow(helper);
        flower.setLifetime(Integer.MAX_VALUE);
        flower.setRange(2.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(flower.isRemoved(), "the flower should wilt once it has travelled its whole range");
            helper.assertTrue(flower.getTravelled() >= 2.0D, "the flower wilted before travelling its whole range");
        });
    }

    /** The enemies of the module go down in one, the way they do in the games they come from. */
    @GameTest
    public void aFlowerDefeatsMarioEnemiesOutright(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Goomba goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND.above(2));
        helper.assertTrue(goomba.getMaxHealth() > Flower.DAMAGE,
                "a goomba that a plain hit would kill anyway proves nothing, it has " + goomba.getMaxHealth() + " health");
        grow(helper);

        helper.succeedWhen(() -> helper.assertTrue(goomba.isDeadOrDying(), "the flower should defeat a Mario enemy outright"));
    }

    /** Everything else takes what a ball would have dealt, and lives to tell the tale. */
    @GameTest
    public void aFlowerOnlyChipsAtEverythingElse(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, GROUND.above(2));
        grow(helper);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the pig was never hit at all"))
                .thenExecute(() -> {
                    helper.assertFalse(pig.isDeadOrDying(), "only the enemies of the module should go down in one");
                    helper.assertValueEqual(pig.getHealth(), pig.getMaxHealth() - Flower.DAMAGE, "the health left on a hit pig");
                })
                .thenSucceed();
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
    private static void attach(GameTestHelper helper, BlockPos hook) {
        var absolute = helper.absolutePos(hook);
        TripWireHookBlock.calculateState(helper.getLevel(), absolute, helper.getLevel().getBlockState(absolute), false, false, -1, null);
    }

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

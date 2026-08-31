package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * In-game behaviour of {@link Ball}, the fireball/iceball projectiles thrown by the fire and ice
 * power-ups: they bounce off the floor a few times before bursting, and they mark whoever they hit.
 */
public class BallGameTest {
    /** Bottom layer of the arena, in structure-relative coordinates. */
    private static final int FLOOR_Y = 0;
    private static final int ARENA_SIZE = 8;

    /** Where the mob taking the hit stands, one block above the floor. */
    private static final BlockPos TARGET_POS = new BlockPos(4, FLOOR_Y + 1, 3);

    /** After that many ticks a ball dropped from {@code FLOOR_Y + 5} has landed once but not four times. */
    private static final int BOUNCES_LEFT_AFTER_TICKS = 25;

    @GameTest(maxTicks = 100)
    public void bouncesBackUpWhenItLandsOnTheFloor(GameTestHelper helper) {
        buildFloor(helper);

        var fireball = helper.spawn(SuperMarioEntityTypes.FIREBALL, new Vec3(4.5D, FLOOR_Y + 5.0D, 3.5D));

        // `failIfEver` re-runs its assertion on every tick and fails the test as soon as it trips.
        helper.failIfEver(() -> helper.assertFalse(fireball.isRemoved(), "the fireball burst instead of bouncing off the floor"));
        // The ball is dropped with no motion at all, so any upwards motion can only come from a bounce.
        helper.succeedWhen(() -> helper.assertTrue(fireball.getDeltaMovement().y > 0.0D, "the fireball has not bounced off the floor yet"));
    }

    @GameTest(maxTicks = 400)
    public void burstsOnceItRanOutOfBounces(GameTestHelper helper) {
        buildFloor(helper);

        var fireball = helper.spawn(SuperMarioEntityTypes.FIREBALL, new Vec3(4.5D, FLOOR_Y + 5.0D, 3.5D));

        // A ball is worth several bounces: it takes about ten ticks to reach the floor and a dozen more
        // per bounce, so it must still be around well after its first landing.
        helper.startSequence()
                .thenIdle(BOUNCES_LEFT_AFTER_TICKS)
                .thenExecute(() -> helper.assertFalse(fireball.isRemoved(), "the fireball burst on its first landing instead of bouncing"))
                .thenWaitUntil(() -> helper.assertTrue(fireball.isRemoved(), "the fireball never burst, it kept bouncing forever"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void fireballSetsWhatItHitsOnFire(GameTestHelper helper) {
        buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET_POS);

        shootAt(helper, SuperMarioEntityTypes.FIREBALL, TARGET_POS);

        helper.succeedWhen(() -> {
            helper.assertTrue(pig.getRemainingFireTicks() > 0, "the fireball did not set the pig on fire");
            helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the fireball did not hurt the pig");
        });
    }

    @GameTest(maxTicks = 100)
    public void iceballFreezesWhatItHits(GameTestHelper helper) {
        buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET_POS);

        shootAt(helper, SuperMarioEntityTypes.ICEBALL, TARGET_POS);

        helper.succeedWhen(() -> {
            helper.assertTrue(Freezing.isFrozen(pig), "the iceball did not freeze the pig");
            helper.assertTrue(pig.getHealth() < pig.getMaxHealth(), "the iceball did not hurt the pig");
            helper.assertTrue(pig.getRemainingFireTicks() <= 0, "the iceball set the pig on fire");
        });
    }

    /** Fills the bottom layer of the arena with stone, so that nothing falls out of the test. */
    private static void buildFloor(GameTestHelper helper) {
        for (int x = 0; x < ARENA_SIZE; x++) {
            for (int z = 0; z < ARENA_SIZE; z++) {
                helper.setBlock(new BlockPos(x, FLOOR_Y, z), Blocks.STONE);
            }
        }
    }

    /**
     * Shoots a ball horizontally at {@code target} from two blocks away. Gravity is turned off so
     * that the impact tests only depend on what happens on contact, not on the ballistic curve.
     */
    private static <T extends Ball> void shootAt(GameTestHelper helper, EntityType<T> type, BlockPos target) {
        var spawnPos = new Vec3(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() - 2.0D);

        T ball = helper.spawn(type, spawnPos);
        ball.setNoGravity(true);
        ball.setDeltaMovement(0.0D, 0.0D, 0.5D);
    }
}

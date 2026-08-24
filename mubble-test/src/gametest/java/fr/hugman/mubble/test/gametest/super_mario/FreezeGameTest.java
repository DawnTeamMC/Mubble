package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeResistance;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.pig.Pig;

/**
 * Entities caught in a block of ice: how long they stay in there, what it takes out of them, and
 * what the block of ice itself behaves like while it lasts.
 *
 * @see Freezing
 */
public class FreezeGameTest {
    private static final BlockPos TARGET = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    /** Where a mob about to be shoved stands, with room to slide east from there. */
    private static final BlockPos SHOVE_START = new BlockPos(1, Arena.FLOOR_Y + 1, 3);

    @GameTest(maxTicks = 120)
    public void aRegularMobStaysFrozenForTheWholeDuration(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);

        helper.assertTrue(freeze(helper, pig) == FreezeResistance.NONE, "a pig is small enough to be frozen outright");

        helper.startSequence()
                // well past the point a big mob would have broken out of the ice
                .thenIdle(Freezing.TOUGH_DURATION + 20)
                .thenExecute(() -> {
                    helper.assertTrue(Freezing.isFrozen(pig), "the pig thawed long before its freeze was up");
                    helper.assertTrue(pig.getHealth() == pig.getMaxHealth(), "being frozen hurt the pig by itself");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 120)
    public void aBigMobBreaksOutOfTheIceAndHurtsItselfDoingSo(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var golem = helper.spawnWithNoFreeWill(EntityTypes.IRON_GOLEM, TARGET);

        helper.assertTrue(freeze(helper, golem) == FreezeResistance.TOUGH, "an iron golem is big enough to break out of the ice");
        helper.assertTrue(Freezing.isFrozen(golem), "a big mob is still frozen, only not for long");

        helper.startSequence()
                .thenIdle(Freezing.TOUGH_DURATION + 5)
                .thenExecute(() -> {
                    helper.assertFalse(Freezing.isFrozen(golem), "the iron golem never broke out of the ice");
                    helper.assertTrue(golem.getHealth() < golem.getMaxHealth(), "breaking out of the ice cost the iron golem nothing");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void aBossIsHurtRatherThanFrozen(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var wither = helper.spawnWithNoFreeWill(EntityTypes.WITHER, TARGET);

        helper.assertTrue(freeze(helper, wither) == FreezeResistance.IMMUNE, "a boss cannot be frozen at all");
        helper.assertFalse(Freezing.isFrozen(wither), "the wither ended up in a block of ice anyway");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void frozenEntitiesCanBeStoodOn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);

        helper.assertFalse(pig.canBeCollidedWith(null), "a pig is walked through, not into");
        freeze(helper, pig);
        helper.assertTrue(pig.canBeCollidedWith(null), "the block of ice is not solid enough to stand on");
        helper.succeed();
    }

    @GameTest(maxTicks = 60)
    public void shovedIceSlidesStraightAhead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        freeze(helper, pig);

        double startX = pig.getX();
        double startZ = pig.getZ();
        Freezing.shove(pig, Direction.EAST);

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> {
                    helper.assertTrue(pig.getX() > startX + 1.0D, "the shoved block of ice barely moved");
                    helper.assertTrue(Math.abs(pig.getZ() - startZ) < 0.1D, "the shoved block of ice veered off its axis");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void smashingTheKeysMeltsTheIceFaster(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);

        int before = Freezing.getRemainingTicks(pig);
        helper.assertTrue(Freezing.struggle(pig), "struggling did nothing to a frozen entity");
        helper.assertTrue(Freezing.getRemainingTicks(pig) == before - Freezing.STRUGGLE_RELIEF,
                "struggling did not melt its share of the ice");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void strugglingDoesNothingWhenNotFrozen(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);

        helper.assertFalse(Freezing.struggle(pig), "an entity that is not frozen has nothing to struggle out of");
        helper.succeed();
    }

    private static FreezeResistance freeze(GameTestHelper helper, LivingEntity entity) {
        return Freezing.freeze(helper.getLevel(), entity, iceball(helper));
    }

    private static DamageSource iceball(GameTestHelper helper) {
        return helper.getLevel().damageSources().source(SuperMarioDamageTypeIds.ICEBALL);
    }
}

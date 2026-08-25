package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeResistance;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;

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
    /** What a sliding mob is aimed at, for the tests about running into something. */
    private static final BlockPos WALL = new BlockPos(5, Arena.FLOOR_Y + 1, 3);

    @GameTest(maxTicks = 140)
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

    @GameTest(maxTicks = 140)
    public void aBigMobBreaksOutOfTheIceUnharmed(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var golem = helper.spawnWithNoFreeWill(EntityTypes.IRON_GOLEM, TARGET);

        helper.assertTrue(freeze(helper, golem) == FreezeResistance.TOUGH, "an iron golem is big enough to break out of the ice");
        helper.assertTrue(Freezing.isFrozen(golem), "a big mob is still frozen, only not for long");

        helper.startSequence()
                .thenIdle(Freezing.TOUGH_DURATION + 5)
                .thenExecute(() -> {
                    helper.assertFalse(Freezing.isFrozen(golem), "the iron golem never broke out of the ice");
                    helper.assertTrue(golem.getHealth() == golem.getMaxHealth(), "breaking out of the ice should cost the iron golem nothing");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void aBossIsLeftAloneRatherThanFrozen(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var wither = helper.spawnWithNoFreeWill(EntityTypes.WITHER, TARGET);
        float before = wither.getHealth();

        helper.assertTrue(freeze(helper, wither) == FreezeResistance.IMMUNE, "a boss cannot be frozen at all");
        helper.assertFalse(Freezing.isFrozen(wither), "the wither ended up in a block of ice anyway");
        helper.assertTrue(wither.getHealth() == before, "the freeze hurt the wither on its own, on top of whatever threw it");
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

    @GameTest(maxTicks = 60)
    public void aSlideRunsItselfOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        freeze(helper, pig);
        // gently, so that the arena wall is never reached and only the friction can stop it
        pig.setDeltaMovement(Freezing.SHATTER_SPEED * 0.8D, 0.0D, 0.0D);

        helper.startSequence()
                .thenIdle(30)
                .thenExecute(() -> {
                    helper.assertTrue(pig.getDeltaMovement().horizontalDistance() < 0.05D,
                            "the block of ice was still going as fast as ever");
                    helper.assertTrue(Freezing.isFrozen(pig), "and it fell apart rather than coming to a stop");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void aFastSlideIntoAWallShattersTheIce(GameTestHelper helper) {
        Pig pig = walledIn(helper);
        Freezing.shove(pig, Direction.EAST);

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> helper.assertFalse(Freezing.isFrozen(pig), "the ice held up against a wall at full tilt"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void aSpentSlideIntoAWallLeavesTheIceStanding(GameTestHelper helper) {
        Pig pig = walledIn(helper);
        pig.setDeltaMovement(Freezing.SHATTER_SPEED * 0.6D, 0.0D, 0.0D);

        helper.startSequence()
                .thenIdle(25)
                .thenExecute(() -> helper.assertTrue(Freezing.isFrozen(pig), "a slide that had run its course still broke the ice"))
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

    @GameTest(maxTicks = 20)
    public void theIceTakesTheHitInsteadOfTheEntity(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);

        float health = pig.getHealth();
        int before = Freezing.getRemainingTicks(pig);

        helper.assertFalse(pig.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 4.0F),
                "the hit got through to the pig");
        helper.assertTrue(pig.getHealth() == health, "and took health off it");
        helper.assertTrue(Freezing.getRemainingTicks(pig) == before - 4 * Freezing.MELT_PER_DAMAGE,
                "the hit went nowhere: it should have melted its share of the ice");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void theIceIsNoShieldAgainstTheThingsNothingIsSafeFrom(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);

        helper.assertTrue(pig.hurtServer(helper.getLevel(), helper.getLevel().damageSources().genericKill(), Float.MAX_VALUE),
                "a block of ice turned `/kill` away");
        helper.assertFalse(pig.isAlive(), "and left the pig standing");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void aBurnBreaksTheIceOpenAtOnce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);

        pig.hurtServer(helper.getLevel(), helper.getLevel().damageSources().inFire(), 1.0F);

        helper.assertFalse(Freezing.isFrozen(pig), "fire left the block of ice standing");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void aFireballBreaksTheIceOpenToo(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);

        // the mod's own fireballs are fire in everything but the vanilla tag, hence `super_mario:melts_freeze`
        pig.hurtServer(helper.getLevel(), helper.getLevel().damageSources().source(SuperMarioDamageTypeIds.FIREBALL), 1.0F);

        helper.assertFalse(Freezing.isFrozen(pig), "a fireball left the block of ice standing");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void frozenMobsAreNotSetAlight(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        pig.igniteForSeconds(8.0F);

        freeze(helper, pig);
        helper.assertTrue(pig.getRemainingFireTicks() <= 0, "freezing a burning mob left it burning");

        pig.igniteForSeconds(8.0F);
        helper.assertTrue(pig.getRemainingFireTicks() <= 0, "a mob caught fire while sitting in a block of ice");
        helper.succeed();
    }

    @GameTest(maxTicks = 60)
    public void aHitTheIceTurnsAwayStillSendsItSkidding(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        freeze(helper, pig);
        var attacker = TestPlayers.at(helper, SHOVE_START.west(1));

        double startX = pig.getX();
        double liftBefore = pig.getDeltaMovement().y();
        pig.hurtServer(helper.getLevel(), helper.getLevel().damageSources().playerAttack(attacker), 1.0F);

        helper.assertTrue(pig.getDeltaMovement().y() == liftBefore, "the hit lifted the block of ice off the floor");

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> helper.assertTrue(pig.getX() > startX + 1.0D,
                        "a hit the ice turned away left it standing there rather than skidding out of reach"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void knockbackNeverLiftsABlockOfIce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        freeze(helper, pig);
        pig.setOnGround(true);

        double liftBefore = pig.getDeltaMovement().y();
        pig.knockback(0.5D, 1.0D, 0.0D, helper.getLevel().damageSources().generic(), 0.5F);

        helper.assertTrue(pig.getDeltaMovement().y() == liftBefore, "the punch sent the block of ice into the air");
        helper.assertTrue(pig.getDeltaMovement().horizontalDistanceSqr() > 0.0D, "and it did not send it skidding either");
        helper.succeed();
    }

    /** A frozen pig two blocks short of a wall, with room to build up speed on the way there. */
    private static Pig walledIn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(WALL, Blocks.STONE);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, WALL.west(2));
        freeze(helper, pig);
        return pig;
    }

    private static FreezeResistance freeze(GameTestHelper helper, LivingEntity entity) {
        return Freezing.freeze(helper.getLevel(), entity);
    }
}

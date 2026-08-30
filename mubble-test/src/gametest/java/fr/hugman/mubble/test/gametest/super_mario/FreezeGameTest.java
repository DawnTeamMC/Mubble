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
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

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
    /** The waterline of the pool the floating tests fill, in structure-relative coordinates. */
    private static final int POOL_SURFACE_Y = Arena.FLOOR_Y + 5;
    /** Well clear of the floor, for the tests about ice on its way down. */
    private static final BlockPos MIDAIR = new BlockPos(4, Arena.FLOOR_Y + 5, 3);

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
    public void aShoveOffTheAxesSendsTheIceOffAtThatAngle(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        freeze(helper, pig);

        double startX = pig.getX();
        double startZ = pig.getZ();
        // straight between east and south, which snapping to the nearest way round would have flattened
        Freezing.shove(pig, new Vec3(1.0D, 0.0D, 1.0D));

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> {
                    double east = pig.getX() - startX;
                    double south = pig.getZ() - startZ;
                    helper.assertTrue(east > 0.5D && south > 0.5D, "the block of ice went off along an axis rather than the corner");
                    helper.assertTrue(Math.abs(east - south) < 0.2D, "and it favoured one of the two over the other");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void theTopOfABlockOfIceIsSlippery(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig ice = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        Pig rider = rest(helper, ice, helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET.above(2)));

        helper.assertTrue(rider.onGround(), "the rider never came to rest on the block of ice");
        helper.assertTrue(rider.mainSupportingBlockPos.isEmpty(), "and it found a block under it rather than the ice");
        helper.assertTrue(Freezing.isStandingOnFrozen(rider), "standing on a frozen mob should count as standing on ice");
        helper.assertFalse(Freezing.isStandingOnFrozen(ice), "the block of ice is not standing on itself");
        helper.succeed();
    }

    @GameTest(maxTicks = 60)
    public void aSlidingBlockOfIceCarriesItsRider(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig ice = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        Pig rider = rest(helper, ice, helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START.above(2)));

        double startX = rider.getX();
        Freezing.shove(ice, Direction.EAST);

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> {
                    helper.assertTrue(rider.getX() > startX + 1.0D, "the ice slid out from under its rider");
                    helper.assertTrue(Math.abs(rider.getX() - ice.getX()) < 0.5D, "and the rider drifted off the back of it");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void aBlockOfIceClimbingAStepTakesItsRiderWithIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // a half step to the east, low enough for a block of ice to ride up rather than stop dead
        for (int x = 3; x < Arena.SIZE; x++) {
            for (int z = 2; z <= 4; z++) {
                helper.setBlock(new BlockPos(x, Arena.FLOOR_Y + 1, z), Blocks.SMOOTH_STONE_SLAB);
            }
        }
        Pig ice = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        Pig rider = rest(helper, ice, helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START.above(2)));
        // gently enough that the step is climbed rather than shattered against
        ice.setDeltaMovement(Freezing.SHATTER_SPEED * 0.8D, 0.0D, 0.0D);
        double startY = ice.getY();

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> {
                    helper.assertTrue(ice.getY() > startY + 0.4D, "the block of ice never climbed the step");
                    helper.assertTrue(Math.abs(rider.getX() - ice.getX()) < 0.5D,
                            "the ice climbed the step and left its rider at the foot of it");
                    helper.assertTrue(rider.getY() > startY + 0.4D, "and the rider never came up with it");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 40)
    public void aFallingBlockOfIceTakesItsRiderDownWithIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig ice = helper.spawnWithNoFreeWill(EntityTypes.PIG, MIDAIR);
        Pig rider = rest(helper, ice, helper.spawnWithNoFreeWill(EntityTypes.PIG, MIDAIR.above(2)));
        // ice falls faster than anything riding it would on its own, which is what pulls them apart
        ice.setDeltaMovement(0.0D, -0.5D, 0.0D);

        helper.startSequence()
                .thenIdle(3)
                .thenExecute(() -> helper.assertTrue(rider.getY() - ice.getBoundingBox().maxY < 0.2D,
                        "the ice dropped out from under its rider and left it hanging in the air"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void jumpingOffASlidingBlockOfIceCarriesItsSpeed(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig ice = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START);
        Pig rider = rest(helper, ice, helper.spawnWithNoFreeWill(EntityTypes.PIG, SHOVE_START.above(2)));
        Freezing.shove(ice, Direction.EAST);

        helper.assertTrue(rider.getDeltaMovement().x() == 0.0D, "the rider was already going somewhere");
        rider.jumpFromGround();

        helper.assertTrue(rider.getDeltaMovement().x() > Freezing.SLIDE_SPEED / 2.0D,
                "the jump left the speed of the ice behind rather than taking it along");
        helper.assertTrue(rider.getDeltaMovement().y() > 0.0D, "and it was not much of a jump either");
        helper.succeed();
    }

    @GameTest(maxTicks = 20)
    public void jumpingOffSolidGroundPicksUpNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        pig.move(MoverType.SELF, new Vec3(0.0D, -0.2D, 0.0D));

        pig.jumpFromGround();

        helper.assertTrue(pig.getDeltaMovement().horizontalDistanceSqr() == 0.0D,
                "a jump off plain ground picked up speed from somewhere");
        helper.succeed();
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

        // the mod's own fireballs are fire in everything but the vanilla tag, hence `super_mario:melts_frozen_entities`
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

    /**
     * Freezes {@code ice} and settles {@code rider} on top of it. The last inch is covered by a move
     * of its own rather than by a fall: that move is what works out what is holding the rider up.
     */
    private static <T extends LivingEntity> T rest(GameTestHelper helper, LivingEntity ice, T rider) {
        freeze(helper, ice);
        rider.setPos(ice.getX(), ice.getBoundingBox().maxY + 0.05D, ice.getZ());
        rider.move(MoverType.SELF, new Vec3(0.0D, -0.2D, 0.0D));
        return rider;
    }

    /** A frozen pig two blocks short of a wall, with room to build up speed on the way there. */
    @GameTest(maxTicks = 160)
    public void frozenIceFloatsUpToTheWaterSurface(GameTestHelper helper) {
        Pig pig = inThePool(helper);
        freeze(helper, pig);

        double startY = pig.getY();
        // the pig's position is absolute, the pool was built in structure-relative coordinates
        double waterline = helper.absolutePos(new BlockPos(0, POOL_SURFACE_Y, 0)).getY();

        helper.startSequence()
                .thenIdle(120)
                .thenExecute(() -> {
                    helper.assertTrue(pig.getY() > startY + 1.0D,
                            "the block of ice sank instead of floating up");
                    // it rides the surface rather than popping out of the water and landing back in
                    helper.assertTrue(pig.getY() < waterline + 0.5D,
                            "the block of ice was pushed clear of the water");
                    helper.assertTrue(pig.getY() > waterline - pig.getBbHeight(),
                            "the block of ice settled below the surface instead of on it");
                    helper.assertTrue(Math.abs(pig.getDeltaMovement().y()) < 0.05D,
                            "the block of ice never settled, it is still bobbing");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 160)
    public void aFloatingBlockOfIceKeepsItsRiderOutOfTheWater(GameTestHelper helper) {
        Pig pig = inThePool(helper);
        freeze(helper, pig);

        helper.startSequence()
                .thenIdle(120)
                .thenExecute(() -> helper.assertFalse(pig.isEyeInFluid(FluidTags.WATER),
                        "a floating block of ice left the head of whoever is inside it under water"))
                .thenSucceed();
    }

    /**
     * A pig sitting on the bottom of a pool deep enough that it has somewhere to float up to.
     */
    private static Pig inThePool(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int x = 0; x < Arena.SIZE; x++) {
            for (int z = 0; z < Arena.SIZE; z++) {
                for (int y = Arena.FLOOR_Y + 1; y < POOL_SURFACE_Y; y++) {
                    helper.setBlock(new BlockPos(x, y, z), Blocks.WATER);
                }
            }
        }
        return helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, Arena.FLOOR_Y + 1, 3));
    }

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

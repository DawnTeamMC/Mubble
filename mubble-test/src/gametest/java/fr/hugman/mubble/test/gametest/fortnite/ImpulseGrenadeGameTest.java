package fr.hugman.mubble.test.gametest.fortnite;

import fr.hugman.mubble.fortnite.world.entity.FortniteEntityTypes;
import fr.hugman.mubble.fortnite.world.entity.projectile.ImpulseGrenade;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * The impulse grenade from Fortnite: it sticks to the first block it lands on, then goes off after a short fuse,
 * sending whatever is around it flying without hurting anyone or breaking anything.
 */
public class ImpulseGrenadeGameTest {
    private static final int GROUND_Y = Arena.FLOOR_Y + 1;
    /** Where the grenade is dropped, on the floor in the middle of the arena. */
    private static final Vec3 GRENADE_POS = new Vec3(4.5D, GROUND_Y, 4.5D);
    /** Close enough to the grenade to get caught in its blast. */
    private static final BlockPos NEAR_POS = new BlockPos(5, GROUND_Y, 4);

    @GameTest(maxTicks = 20)
    public void launchesWhatIsAroundItWithoutHurtingIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, NEAR_POS);

        var grenade = dropGrenade(helper);

        helper.startSequence()
                .thenExecute(grenade::detonate)
                .thenExecute(() -> {
                    Vec3 movement = pig.getDeltaMovement();
                    helper.assertTrue(movement.y > 0.3D, "the blast did not throw the pig into the air");
                    // The pig stands on the +X side of the grenade, so that is the way it should be pushed.
                    helper.assertTrue(movement.x > 0.1D, "the blast did not push the pig away from the grenade");
                    helper.assertTrue(pig.getHealth() == pig.getMaxHealth(), "the blast hurt the pig");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void pushesHarderTheCloserItGoesOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig near = helper.spawnWithNoFreeWill(EntityTypes.PIG, NEAR_POS);
        Pig far = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(7, GROUND_Y, 4));

        var grenade = dropGrenade(helper);

        helper.startSequence()
                .thenExecute(grenade::detonate)
                .thenExecute(() -> helper.assertTrue(near.getDeltaMovement().length() > far.getDeltaMovement().length(),
                        "the pig further from the blast was pushed at least as hard as the one next to it"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void breaksNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        BlockPos glass = new BlockPos(5, GROUND_Y, 4);
        helper.setBlock(glass, Blocks.GLASS);

        var grenade = dropGrenade(helper);

        helper.startSequence()
                .thenExecute(grenade::detonate)
                .thenExecute(() -> {
                    helper.assertBlockPresent(Blocks.GLASS, glass);
                    helper.assertBlockPresent(Blocks.STONE, new BlockPos(4, Arena.FLOOR_Y, 4));
                })
                .thenSucceed();
    }

    /** The fuse only starts burning once the grenade has stuck to something: one still in the air never goes off. */
    @GameTest(maxTicks = 40)
    public void waitsToStickBeforeArming(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 5.0D, 0.0D));
        grenade.setDeltaMovement(0.0D, 0.2D, 0.0D);

        helper.startSequence()
                .thenIdle(3)
                .thenExecute(() -> {
                    helper.assertFalse(grenade.isStuck(), "the grenade stuck to thin air");
                    helper.assertFalse(grenade.isArmed(), "the fuse started burning before the grenade stuck to anything");
                    helper.assertTrue(grenade.getFuse() == ImpulseGrenade.FUSE, "the fuse burnt down while the grenade was in the air");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 20)
    public void sticksToTheGroundInsteadOfBouncing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 2.0D, 0.0D));
        grenade.setDeltaMovement(0.3D, -0.2D, 0.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(grenade.isStuck(), "the grenade never stuck to the floor");
            helper.assertTrue(grenade.getDeltaMovement().equals(Vec3.ZERO), "the grenade kept moving once stuck");
            double height = grenade.getY() - helper.absoluteVec(GRENADE_POS).y;
            helper.assertTrue(height >= 0.0D && height < 0.2D, "the grenade is not lying on the floor");
        });
    }

    @GameTest(maxTicks = 20)
    public void sticksToWalls(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int y = GROUND_Y; y < Arena.SIZE; y++) {
            helper.setBlock(new BlockPos(6, y, 4), Blocks.STONE);
        }

        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 3.0D, 0.0D));
        grenade.setDeltaMovement(0.6D, 0.0D, 0.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(grenade.isStuck(), "the grenade never stuck to the wall");
            // It stays up on the wall rather than sliding down to the floor.
            helper.assertTrue(grenade.getY() > helper.absoluteVec(GRENADE_POS).y + 1.0D, "the grenade fell off the wall");
        });
    }

    @GameTest(maxTicks = ImpulseGrenade.FUSE + 30)
    public void goesOffOnceStuckForItsWholeFuse(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, NEAR_POS);

        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 0.5D, 0.0D));
        grenade.setDeltaMovement(0.0D, -0.3D, 0.0D);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(grenade.isStuck(), "the grenade never stuck to the floor"))
                .thenIdle(ImpulseGrenade.FUSE - 5)
                .thenExecute(() -> {
                    helper.assertFalse(grenade.isRemoved(), "the grenade went off before its fuse ran out");
                    helper.assertTrue(pig.getDeltaMovement().y <= 0.0D, "the pig was thrown before the grenade went off");
                })
                .thenIdle(10)
                .thenExecute(() -> {
                    helper.assertTrue(grenade.isRemoved(), "the grenade outlived its fuse");
                    helper.assertTrue(pig.getHealth() == pig.getMaxHealth(), "the blast hurt the pig");
                })
                .thenSucceed();
    }

    /** It is meant to end up on the ground, so it flies straight through whatever stands in the way. */
    @GameTest(maxTicks = 20)
    public void fliesThroughEntities(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, GROUND_Y, 4));

        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, new Vec3(2.0D, GROUND_Y + 0.5D, 4.5D));
        grenade.setDeltaMovement(0.6D, 0.0D, 0.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(grenade.isStuck(), "the grenade never stuck to the floor");
            helper.assertTrue(grenade.getX() > pig.getBoundingBox().minX, "the grenade stopped at the pig");
            helper.assertFalse(grenade.isRemoved(), "the grenade went off on the pig");
        });
    }

    private static ImpulseGrenade dropGrenade(GameTestHelper helper) {
        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS);
        grenade.setDeltaMovement(Vec3.ZERO);
        return grenade;
    }
}

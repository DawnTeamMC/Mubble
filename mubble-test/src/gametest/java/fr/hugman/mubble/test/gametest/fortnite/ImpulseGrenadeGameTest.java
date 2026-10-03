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
 * The impulse grenade from Fortnite: it bounces about until its fuse runs out, then sends whatever is around it
 * flying without hurting anyone or breaking anything.
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

    @GameTest(maxTicks = ImpulseGrenade.FUSE + 20)
    public void goesOffWhenItsFuseRunsOut(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, NEAR_POS);

        var grenade = dropGrenade(helper);

        helper.startSequence()
                .thenIdle(ImpulseGrenade.FUSE - 5)
                .thenExecute(() -> {
                    helper.assertFalse(grenade.isRemoved(), "the grenade went off before its fuse ran out");
                    helper.assertTrue(pig.getDeltaMovement().y <= 0.0D, "the pig was thrown before the grenade went off");
                })
                .thenIdle(10)
                .thenExecute(() -> helper.assertTrue(grenade.isRemoved(), "the grenade outlived its fuse"))
                .thenSucceed();
    }

    /** A grenade left on the floor rolls to a stop and waits there, instead of bouncing on the spot. */
    @GameTest(maxTicks = ImpulseGrenade.FUSE)
    public void settlesOnTheGround(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 2.0D, 0.0D));
        grenade.setDeltaMovement(0.1D, 0.0D, 0.0D);

        helper.startSequence()
                .thenIdle(ImpulseGrenade.FUSE - 5)
                .thenExecute(() -> {
                    helper.assertFalse(grenade.isRemoved(), "the grenade went off before its fuse ran out");
                    helper.assertTrue(grenade.getY() - helper.absoluteVec(GRENADE_POS).y < 0.2D, "the grenade is not lying on the floor");
                    helper.assertTrue(Math.abs(grenade.getDeltaMovement().y) < 0.1D, "the grenade keeps hopping on the floor");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = ImpulseGrenade.FUSE)
    public void bouncesOffWalls(GameTestHelper helper) {
        Arena.buildFloor(helper);
        for (int y = GROUND_Y; y < Arena.SIZE; y++) {
            helper.setBlock(new BlockPos(6, y, 4), Blocks.STONE);
        }

        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS.add(0.0D, 1.0D, 0.0D));
        grenade.setDeltaMovement(0.5D, 0.0D, 0.0D);

        helper.succeedWhen(() -> helper.assertTrue(grenade.getDeltaMovement().x < 0.0D, "the grenade never bounced back off the wall"));
    }

    @GameTest(maxTicks = 40)
    public void goesOffOnTheEntityItHits(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, GROUND_Y, 4));

        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, new Vec3(2.0D, GROUND_Y + 0.5D, 4.5D));
        grenade.setDeltaMovement(0.6D, 0.0D, 0.0D);

        helper.succeedWhen(() -> {
            helper.assertTrue(grenade.isRemoved(), "the grenade did not go off on the pig");
            helper.assertTrue(grenade.getFuse() > 0, "the grenade only went off once its fuse ran out");
            helper.assertTrue(pig.getHealth() == pig.getMaxHealth(), "the grenade hurt the pig it hit");
        });
    }

    private static ImpulseGrenade dropGrenade(GameTestHelper helper) {
        var grenade = helper.spawn(FortniteEntityTypes.IMPULSE_GRENADE, GRENADE_POS);
        grenade.setDeltaMovement(Vec3.ZERO);
        return grenade;
    }
}

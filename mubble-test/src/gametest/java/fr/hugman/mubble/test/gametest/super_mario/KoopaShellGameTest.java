package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.KoopaShell;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * The sliding shells. {@code AABBUtilTest} already pins down the geometry of the bounce; this is the
 * same thing played out in a level, plus what happens to whatever gets in the way.
 */
public class KoopaShellGameTest {
    private static final int FLOOR = Arena.FLOOR_Y;
    private static final Vec3 START = new Vec3(1.5D, FLOOR + 1.0D, 3.5D);
    private static final BlockPos TARGET = new BlockPos(4, FLOOR + 1, 3);

    @GameTest(maxTicks = 120)
    public void ashellKeepsSlidingAlongTheFloor(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var shell = slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.4D);

        helper.startSequence()
                .thenIdle(10)
                .thenExecute(() -> {
                    helper.assertFalse(shell.isRemoved(), "the shell vanished instead of sliding");
                    helper.assertFalse(shell.isStopped(), "the shell stopped dead on a flat floor");
                    helper.assertTrue(shell.getX() > helper.absoluteVec(START).x, "the shell never moved along its axis");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 200)
    public void ashellBouncesOffAWall(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // A wall across the shell's path, two blocks along.
        for (int y = FLOOR + 1; y <= FLOOR + 2; y++) {
            helper.setBlock(new BlockPos(5, y, 3), Blocks.STONE);
        }

        var shell = slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.4D);

        helper.succeedWhen(() -> helper.assertTrue(shell.getDeltaMovement().x < 0.0D,
                "the shell never turned back after meeting the wall"));
    }

    @GameTest(maxTicks = 200)
    public void ashellHurtsWhatItRunsInto(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET);
        float health = pig.getHealth();

        slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.4D);

        helper.succeedWhen(() -> helper.assertTrue(pig.getHealth() < health, "the shell slid straight through the pig"));
    }

    @GameTest(maxTicks = 400)
    public void ashellRunsOutOfBounces(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // Boxed in on both sides, so the shell has to spend its bounces.
        for (int y = FLOOR + 1; y <= FLOOR + 2; y++) {
            helper.setBlock(new BlockPos(0, y, 3), Blocks.STONE);
            helper.setBlock(new BlockPos(6, y, 3), Blocks.STONE);
        }

        var shell = slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.6D);

        helper.succeedWhen(() -> helper.assertTrue(shell.isRemoved(), "the shell bounced between the walls forever"));
    }

    @GameTest
    public void thereboundsCounterIsWrittenDown(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var shell = slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.4D);

        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        shell.saveWithoutId(output);

        helper.assertTrue(output.buildResult().contains(KoopaShell.REBOUNDS_KEY),
                "a shell that forgets its rebounds comes back from a save able to bounce forever");
        helper.succeed();
    }

    /**
     * The red shell chases, the green one does not: that is the whole difference between them. It only
     * looks for a target on behalf of whoever threw it, so a shell without an owner never homes at all.
     */
    @GameTest(maxTicks = 200)
    public void theredShellTurnsTowardsATarget(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // A target off to one side, so a shell going straight would never reach it.
        helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, FLOOR + 1, 6));

        var thrower = TestPlayers.at(helper, new BlockPos(1, FLOOR + 1, 3));
        var shell = slide(helper, SuperMarioEntityTypes.RED_KOOPA_SHELL, 0.4D);
        shell.setOwner(thrower);

        helper.succeedWhen(() -> helper.assertTrue(shell.getDeltaMovement().z > 0.05D,
                "the red shell never turned towards the pig standing off its path"));
    }

    @GameTest(maxTicks = 60)
    public void anownerlessRedShellGoesStraight(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, FLOOR + 1, 6));

        var shell = slide(helper, SuperMarioEntityTypes.RED_KOOPA_SHELL, 0.4D);

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> helper.assertTrue(Math.abs(shell.getDeltaMovement().z) < 0.05D,
                        "a shell nobody threw should have nothing to chase"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void thegreenShellIgnoresATarget(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, FLOOR + 1, 6));

        var shell = slide(helper, SuperMarioEntityTypes.GREEN_KOOPA_SHELL, 0.4D);

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> helper.assertTrue(Math.abs(shell.getDeltaMovement().z) < 0.05D,
                        "the green shell should not steer towards anything"))
                .thenSucceed();
    }

    private static <T extends KoopaShell> T slide(GameTestHelper helper, EntityType<T> type, double speed) {
        T shell = helper.spawn(type, START);
        shell.setDeltaMovement(speed, 0.0D, 0.0D);
        return shell;
    }
}

package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;

/**
 * The note block, which is a bumpable block that also throws back whatever lands on it. The launch
 * strength depends on whether the entity is sneaking, which is how a player controls their jump
 * height.
 */
public class NoteBlockGameTest {
    private static final BlockPos BLOCK = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void landingOnItThrowsYouBackUp(GameTestHelper helper) {
        var pig = onTopOf(helper);

        SuperMarioBlocks.NOTE_BLOCK.launchEntity(pig);

        helper.assertTrue(pig.getDeltaMovement().y > 0.5D, "the note block did not throw the pig back up");
        helper.succeed();
    }

    /** Sneaking is how you keep the bounce low, so the two heights have to stay apart. */
    @GameTest
    public void sneakingKeepsTheBounceLow(GameTestHelper helper) {
        var standing = onTopOf(helper);
        var sneaking = helper.spawnWithNoFreeWill(EntityTypes.PIG, BLOCK.above());
        sneaking.setShiftKeyDown(true);

        SuperMarioBlocks.NOTE_BLOCK.launchEntity(standing);
        SuperMarioBlocks.NOTE_BLOCK.launchEntity(sneaking);

        helper.assertTrue(sneaking.getDeltaMovement().y < standing.getDeltaMovement().y,
                "sneaking should give a lower bounce than standing");
        helper.assertTrue(sneaking.getDeltaMovement().y > 0.0D, "sneaking should still bounce");

        helper.succeed();
    }

    @GameTest
    public void alaunchKeepsTheHorizontalMomentum(GameTestHelper helper) {
        var pig = onTopOf(helper);
        pig.setDeltaMovement(0.3D, -0.5D, -0.2D);

        SuperMarioBlocks.NOTE_BLOCK.launchEntity(pig);

        helper.assertValueEqual(pig.getDeltaMovement().x, 0.3D, "the sideways speed through a launch");
        helper.assertValueEqual(pig.getDeltaMovement().z, -0.2D, "the forward speed through a launch");
        helper.succeed();
    }

    @GameTest(maxTicks = 100)
    public void fallingOnItBumpsTheBlock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, SuperMarioBlocks.NOTE_BLOCK);
        var absolute = helper.absolutePos(BLOCK);

        var pig = helper.spawn(EntityTypes.PIG, BLOCK.above(3));

        // Landing on the block is what starts the bump, through fallOn.
        SuperMarioBlocks.NOTE_BLOCK.fallOn(helper.getLevel(), helper.getLevel().getBlockState(absolute), absolute, pig, 3.0D);

        // The hit posts a block event, so the bump only shows up on the level's next pass.
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> helper.assertTrue(
                        helper.getLevel().getBlockEntity(absolute, fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes.BUMPABLE_BLOCK)
                                .orElseThrow(() -> new AssertionError("the note block has no block entity"))
                                .isBumping(),
                        "landing on the note block did not bump it"))
                .thenSucceed();
    }

    @GameTest
    public void anoteBlockNeverTurnsIntoSomethingElse(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, SuperMarioBlocks.NOTE_BLOCK);

        var entity = helper.getLevel()
                .getBlockEntity(helper.absolutePos(BLOCK), fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes.BUMPABLE_BLOCK)
                .orElseThrow(() -> new AssertionError("the note block has no block entity"));

        helper.assertTrue(entity.getBumpedState() == null, "a note block should stay a note block for good");
        helper.assertFalse(entity.shouldBreak(), "and it should never break");

        helper.succeed();
    }

    private static Pig onTopOf(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, SuperMarioBlocks.NOTE_BLOCK);
        return helper.spawnWithNoFreeWill(EntityTypes.PIG, BLOCK.above());
    }
}

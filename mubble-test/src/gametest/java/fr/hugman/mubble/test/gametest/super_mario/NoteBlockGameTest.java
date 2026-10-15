package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    /**
     * Landing on the block is what starts the bump, through fallOn. The bump has to be known as a downward
     * one right away: that is what keeps the generic bump sound from playing over the bounce, and it is
     * read before the block event posted by the hit has gone out.
     */
    @GameTest
    public void fallingOnItBumpsTheBlockDownwards(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, SuperMarioBlocks.NOTE_BLOCK);
        var absolute = helper.absolutePos(BLOCK);
        var entity = helper.getLevel().getBlockEntity(absolute, SuperMarioBlockEntityTypes.BUMPABLE_BLOCK)
                .orElseThrow(() -> new AssertionError("the note block has no block entity"));

        var pig = helper.spawn(EntityTypes.PIG, BLOCK.above(3));

        SuperMarioBlocks.NOTE_BLOCK.fallOn(helper.getLevel(), helper.getLevel().getBlockState(absolute), absolute, pig, 3.0D);

        helper.assertTrue(entity.isBumping(), "landing on the note block did not bump it");
        helper.assertValueEqual(entity.getBumpDirection(), Direction.DOWN, "the direction of a bump from landing on top");
        helper.succeed();
    }

    @GameTest
    public void anoteBlockNeverTurnsIntoSomethingElse(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, SuperMarioBlocks.NOTE_BLOCK);

        var entity = helper.getLevel()
                .getBlockEntity(helper.absolutePos(BLOCK), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK)
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

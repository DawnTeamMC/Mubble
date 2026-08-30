package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.BumpableDropMode;
import fr.hugman.mubble.super_mario.world.level.block.HittableBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The question block and its family: an inventory, a three-stage bump animation, and a state it turns
 * into once emptied.
 * <p>
 * The bump is started through {@code onHit}, the same entry point the entity mixin uses when something
 * jumps into the block from below, rather than by moving an entity into it.
 */
public class BumpableBlockGameTest {
    /** Two above the floor, leaving a block of air underneath for whoever hits it. */
    private static final BlockPos BLOCK = new BlockPos(4, Arena.FLOOR_Y + 2, 3);

    @GameTest
    public void aquestionBlockComesWithABlockEntity(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());

        helper.assertFalse(entity.isBumping(), "a fresh block is already mid-bump");
        helper.assertValueEqual(entity.getDropMode(), BumpableDropMode.ALL, "the drop mode it starts on");

        helper.succeed();
    }

    /**
     * A hit does not raise the bump itself: it posts a block event, which the level dispatches on its
     * next pass. So everything about a bump in progress is only readable a tick later.
     */
    @GameTest(maxTicks = 100)
    public void hittingFromBelowStartsABump(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());

        bumpFromBelow(helper);

        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> helper.assertTrue(entity.isBumping(), "hitting the block from below did not start a bump"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void ablockAlreadyBumpingIgnoresASecondHit(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());

        bumpFromBelow(helper);

        helper.startSequence()
                .thenIdle(3)
                .thenExecute(() -> {
                    helper.assertTrue(entity.isBumping(), "the first hit did not start a bump");
                    int ticks = entity.getBumpTicks();
                    bumpFromBelow(helper);
                    helper.assertValueEqual(entity.getBumpTicks(), ticks, "a second hit restarted the animation");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void bumpingAFullBlockDropsItsContents(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());
        entity.setItem(0, new ItemStack(Items.GOLD_INGOT, 3));

        bumpFromBelow(helper);

        helper.succeedWhen(() -> helper.assertTrue(droppedIngots(helper) > 0, "the block kept its contents to itself"));
    }

    @GameTest(maxTicks = 100)
    public void thedropModeDecidesHowMuchComesOut(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());
        entity.setItem(0, new ItemStack(Items.GOLD_INGOT, 3));
        entity.setDropMode(BumpableDropMode.ONE);

        bumpFromBelow(helper);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(droppedIngots(helper) > 0, "nothing came out"))
                .thenExecute(() -> {
                    helper.assertValueEqual(droppedIngots(helper), 1, "the amount dropped in one-at-a-time mode");
                    helper.assertFalse(entity.isEmpty(), "the block should keep the rest of the stack");
                })
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void anEmptiedQuestionBlockTurnsIntoAnEmptyBlock(GameTestHelper helper) {
        place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());

        bumpFromBelow(helper);

        helper.succeedWhen(() -> helper.assertBlockPresent(SuperMarioBlocks.EMPTY_BLOCK, BLOCK));
    }

    @GameTest(maxTicks = 100)
    public void ablockWithSomethingLeftKeepsItsShape(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());
        entity.setItem(0, new ItemStack(Items.GOLD_INGOT, 3));
        entity.setDropMode(BumpableDropMode.ONE);

        bumpFromBelow(helper);

        helper.startSequence()
                .thenIdle(BumpableBlockEntity.BUMP_LENGTH + 5)
                .thenExecute(() -> helper.assertBlockPresent(SuperMarioBlocks.QUESTION_BLOCK, BLOCK))
                .thenSucceed();
    }

    /** A brick block turns into air, so it has to drop everything at once rather than one at a time. */
    @GameTest
    public void ablockThatBreaksAlwaysDropsEverything(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.BRICK_BLOCK.defaultBlockState());

        helper.assertTrue(entity.shouldBreak(), "the brick block should be one that breaks");
        helper.assertValueEqual(entity.getDropMode(), BumpableDropMode.ALL,
                "a block that breaks cannot drop one item at a time, it has nowhere to keep the rest");

        helper.succeed();
    }

    /** The author is kept straight away, the direction only once the block event comes back round. */
    @GameTest(maxTicks = 100)
    public void thebumpAuthorAndDirectionAreRemembered(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());
        var pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, BLOCK.below());

        entity.bump(helper.absolutePos(BLOCK), pig, Direction.UP);
        helper.assertTrue(entity.getBumpAuthor() == pig, "the block forgot who hit it");

        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> helper.assertValueEqual(entity.getBumpDirection(), Direction.UP, "the direction the bump came from"))
                .thenSucceed();
    }

    @GameTest
    public void thecomparatorReadsTheContents(GameTestHelper helper) {
        var entity = place(helper, SuperMarioBlocks.QUESTION_BLOCK.defaultBlockState());
        var absolute = helper.absolutePos(BLOCK);
        var level = helper.getLevel();
        var state = level.getBlockState(absolute);

        helper.assertTrue(state.hasAnalogOutputSignal(), "a bumpable block should drive a comparator");
        int empty = state.getAnalogOutputSignal(level, absolute, Direction.UP);

        entity.setItem(0, new ItemStack(Items.GOLD_INGOT, 64));
        entity.setChanged();
        int full = level.getBlockState(absolute).getAnalogOutputSignal(level, absolute, Direction.UP);

        helper.assertTrue(full > empty, "filling the block did not move the comparator");
        helper.succeed();
    }

    private static BumpableBlockEntity place(GameTestHelper helper, BlockState state) {
        Arena.buildFloor(helper);
        helper.setBlock(BLOCK, state);

        var found = helper.getLevel().getBlockEntity(helper.absolutePos(BLOCK), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK);
        helper.assertTrue(found.isPresent(),
                "the block was placed without its block entity, what sits there is " + helper.getBlockState(BLOCK));
        return found.get();
    }

    /** Hits the block on its underside, which is what jumping into it amounts to. */
    private static void bumpFromBelow(GameTestHelper helper) {
        var absolute = helper.absolutePos(BLOCK);
        var level = helper.getLevel();
        var state = level.getBlockState(absolute);
        var pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, BLOCK.below());

        var hit = new BlockHitResult(Vec3.atCenterOf(absolute), Direction.DOWN, absolute, false);
        ((HittableBlock) state.getBlock()).onHit(level, state, pig, hit);
    }

    private static int droppedIngots(GameTestHelper helper) {
        var area = AABB.ofSize(Vec3.atCenterOf(helper.absolutePos(BLOCK)), 8.0D, 8.0D, 8.0D);
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, area).stream()
                .filter(item -> item.getItem().is(Items.GOLD_INGOT))
                .mapToInt(item -> item.getItem().getCount())
                .sum();
    }
}

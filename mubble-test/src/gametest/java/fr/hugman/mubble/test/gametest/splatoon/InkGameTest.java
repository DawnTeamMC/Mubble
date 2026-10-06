package fr.hugman.mubble.test.gametest.splatoon;

import fr.hugman.mubble.splatoon.world.attribute.SplatoonEnvironmentAttributes;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBullet;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import fr.hugman.mubble.splatoon.world.level.ink.InkFace;
import fr.hugman.mubble.splatoon.world.level.ink.InkGrid;
import fr.hugman.mubble.splatoon.world.level.ink.InkLevel;
import fr.hugman.mubble.splatoon.world.level.ink.InkPainter;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import fr.hugman.mubble.splatoon.world.level.ink.InkWeathering;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Ink in a real level: where it lands, what cleans it, and how it goes away over time.
 */
public class InkGameTest {
    private static final int FLOOR = Arena.FLOOR_Y;
    private static final BlockPos TARGET = new BlockPos(3, FLOOR, 3);
    private static final InkStyle PINK = new InkStyle(0xF02D7D);

    /** The four cells in the middle of a face, which is where a torch stands. */
    private static final long MIDDLE = cells(3, 3) | cells(4, 3) | cells(3, 4) | cells(4, 4);

    private static long cells(int u, int v) {
        return 1L << InkGrid.cell(u, v);
    }

    @GameTest
    public void aSplatPaintsTheFloorAroundIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var center = Vec3.atCenterOf(helper.absolutePos(TARGET)).add(0, 0.5, 0);

        helper.assertTrue(InkPainter.splat(helper.getLevel(), center, 1.0, PINK), "the splat painted nothing");

        var hit = face(helper, TARGET, Direction.UP);
        helper.assertTrue(hit != null && hit.cells() == InkGrid.ALL_CELLS, "the face under the splat should be fully painted");
        helper.assertValueEqual(hit.get(0), PINK, "the style of the ink");
        var neighbor = face(helper, TARGET.east(), Direction.UP);
        helper.assertTrue(neighbor != null && neighbor.cells() != InkGrid.ALL_CELLS,
                "the splat should spill over the next block, but not cover it");
        helper.assertTrue(face(helper, TARGET, Direction.EAST) == null, "a side buried in the floor got ink");
        helper.succeed();
    }

    @GameTest
    public void aTorchOnlyCleansTheMiddleOfTheFace(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        helper.setBlock(TARGET.above(), Blocks.TORCH);

        var face = face(helper, TARGET, Direction.UP);
        helper.assertTrue(face != null, "the torch cleaned the whole face");
        helper.assertValueEqual(face.cells(), InkGrid.ALL_CELLS & ~MIDDLE, "the cells left after placing a torch");
        helper.succeed();
    }

    @GameTest
    public void aFullBlockCleansTheWholeFace(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        helper.setBlock(TARGET.above(), Blocks.STONE);

        helper.assertTrue(face(helper, TARGET, Direction.UP) == null, "ink stayed under a full block");
        helper.succeed();
    }

    @GameTest
    public void breakingABlockTakesItsInkAway(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        helper.destroyBlock(TARGET);
        helper.setBlock(TARGET, Blocks.STONE);

        helper.assertTrue(face(helper, TARGET, Direction.UP) == null, "a new block came with the ink of the old one");
        helper.succeed();
    }

    @GameTest
    public void waterWashesInkAway(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        helper.setBlock(TARGET.above(), Blocks.WATER);

        helper.assertTrue(face(helper, TARGET, Direction.UP) == null, "ink stayed under water");
        helper.assertFalse(InkLevel.paint(helper.getLevel(), helper.absolutePos(TARGET), Direction.UP, InkGrid.ALL_CELLS, PINK),
                "ink got painted under water");
        helper.succeed();
    }

    @GameTest
    public void aSlabHoldsInkHalfwayUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var slab = TARGET.above();
        helper.setBlock(slab, Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
        paintTop(helper, slab);

        // the block above does not touch the top of the slab
        helper.setBlock(slab.above(), Blocks.STONE);

        var face = face(helper, slab, Direction.UP);
        helper.assertTrue(face != null && face.cells() == InkGrid.ALL_CELLS, "the top of the slab lost its ink");
        helper.succeed();
    }

    @GameTest
    public void plantsAndTorchesDoNotHoldInk(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TARGET.above(), Blocks.TORCH);

        helper.assertFalse(InkLevel.paint(helper.getLevel(), helper.absolutePos(TARGET.above()), Direction.UP, InkGrid.ALL_CELLS, PINK),
                "a torch got ink");
        helper.assertTrue(InkLevel.paint(helper.getLevel(), helper.absolutePos(TARGET), Direction.UP, InkGrid.ALL_CELLS, PINK),
                "the floor around the torch should still take ink");
        helper.assertValueEqual(face(helper, TARGET, Direction.UP).cells(), InkGrid.ALL_CELLS & ~MIDDLE, "the cells painted around a torch");
        helper.succeed();
    }

    @GameTest(maxTicks = InkWeathering.DRY_INTERVAL + 20)
    public void inkDriesOffAfterItsLifetime(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        var level = helper.getLevel();
        int lifetime = level.environmentAttributes().getValue(SplatoonEnvironmentAttributes.INK_LIFETIME, helper.absolutePos(TARGET));
        helper.assertTrue(lifetime > 0, "ink is set to never dry in the test level, the test cannot tell anything");
        // painting nothing still refreshes the face, which is how it gets aged here
        face(helper, TARGET, Direction.UP).paint(0L, PINK, level.getGameTime() - lifetime);

        helper.succeedWhen(() -> helper.assertTrue(face(helper, TARGET, Direction.UP) == null, "old ink is still there"));
    }

    @GameTest(maxTicks = 60)
    public void freshInkDoesNotDry(GameTestHelper helper) {
        Arena.buildFloor(helper);
        paintTop(helper, TARGET);

        helper.startSequence()
                .thenIdle(InkWeathering.RAIN_INTERVAL * 2)
                .thenExecute(() -> helper.assertTrue(face(helper, TARGET, Direction.UP) != null, "fresh ink dried off"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void rainWashesInkAway(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Arena.openTheSky(helper, TARGET.above());
        helper.getLevel().setRainLevel(1.0F);
        paintTop(helper, TARGET);

        helper.assertTrue(helper.getLevel().isRainingAt(helper.absolutePos(TARGET.above())),
                "it is not raining on the ink, the test cannot tell anything");

        helper.succeedWhen(() -> {
            var face = face(helper, TARGET, Direction.UP);
            helper.assertTrue(face == null || face.cells() != InkGrid.ALL_CELLS, "the rain did not wash any ink");
        });
    }

    @GameTest(maxTicks = 60)
    public void aShotPaintsWithTheInkOfItsWeapon(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var shooter = helper.spawn(EntityTypes.ARMOR_STAND, TARGET.above());
        shooter.setXRot(90.0F);

        var bullet = new ShooterInkBullet(helper.getLevel(), shooter, ShooterInkBulletConfig.DEFAULT, PINK, 0.0F);
        helper.getLevel().addFreshEntity(bullet);

        helper.succeedWhen(() -> {
            var face = face(helper, TARGET, Direction.UP);
            helper.assertTrue(face != null, "the shot left no ink under it");
            helper.assertValueEqual(face.get(InkGrid.cell(4, 4)), PINK, "the ink of the shot");
        });
    }

    private static void paintTop(GameTestHelper helper, BlockPos pos) {
        helper.assertTrue(InkLevel.paint(helper.getLevel(), helper.absolutePos(pos), Direction.UP, InkGrid.ALL_CELLS, PINK),
                "could not paint " + pos);
    }

    @Nullable
    private static InkFace face(GameTestHelper helper, BlockPos pos, Direction side) {
        return InkLevel.get(helper.getLevel(), helper.absolutePos(pos), side);
    }
}

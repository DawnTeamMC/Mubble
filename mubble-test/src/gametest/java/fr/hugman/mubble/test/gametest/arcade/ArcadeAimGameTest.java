package fr.hugman.mubble.test.gametest.arcade;

import fr.hugman.mubble.world.arcade.ArcadeAim;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The orbit camera against real blocks: it never sits inside a wall, and its crosshair only picks
 * what the player could reach from their own eyes.
 */
public class ArcadeAimGameTest {
    /** Looking north, along -z, the way the player stands in these tests. */
    private static final Vec3 NORTH = new Vec3(0.0D, 0.0D, -1.0D);

    @GameTest(structure = ArcadeTestKit.LANE)
    public void theCameraStopsShortOfAWallBehindThePlayer(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var focus = helper.absoluteVec(new Vec3(5.5D, 2.6D, 10.5D));
        // in the open, the camera goes all the way back
        helper.assertTrue(Math.abs(ArcadeAim.cameraDistance(helper.getLevel(), player, focus, NORTH, 5.0D) - 5.0D) < 1.0E-9D, "nothing behind the player");

        // a wall two blocks behind the player: the camera stays in front of it, its box included
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 13), new BlockPos(9, 5, 13), Blocks.STONE.defaultBlockState());
        double distance = ArcadeAim.cameraDistance(helper.getLevel(), player, focus, NORTH, 5.0D);
        double wall = helper.absoluteVec(new Vec3(0.0D, 0.0D, 13.0D)).z;
        double camera = focus.z + distance;
        helper.assertTrue(camera <= wall - 0.1D + 1.0E-6D, "the camera at z=" + camera + " should keep its box out of the wall at z=" + wall);
        helper.assertTrue(distance > 2.0D, "the camera should still back off as far as the wall lets it, got " + distance);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void theCrosshairOnlyPicksWhatTheEyesCanReach(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(5.5D, 1.0D, 20.5D)));
        var eye = player.getEyePosition();
        // the camera four blocks behind the player and a bit above, looking north and slightly down
        var origin = eye.add(0.0D, 1.0D, 4.0D);
        var target = helper.absolutePos(new BlockPos(5, 1, 17));
        helper.setBlock(new BlockPos(5, 1, 17), Blocks.STONE.defaultBlockState());
        var forward = Vec3.atCenterOf(target).subtract(origin).normalize();

        var hit = ArcadeAim.pick(player, origin, forward, eye);
        helper.assertTrue(hit.getType() == HitResult.Type.BLOCK && ((BlockHitResult) hit).getBlockPos().equals(target), "a block in front of the player, in reach: got " + hit.getType());

        // a block between the camera and the player is not what the crosshair means
        helper.setBlock(new BlockPos(5, 3, 22), Blocks.STONE.defaultBlockState());
        hit = ArcadeAim.pick(player, origin, forward, eye);
        helper.assertTrue(hit.getType() == HitResult.Type.BLOCK && ((BlockHitResult) hit).getBlockPos().equals(target), "a block behind the player, in the way of the camera, should be looked past");

        // the camera sees over a wall the eyes cannot see over
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.GLASS.defaultBlockState());
        var high = helper.absolutePos(new BlockPos(5, 1, 16));
        helper.setBlock(new BlockPos(5, 1, 17), Blocks.AIR.defaultBlockState());
        helper.setBlock(new BlockPos(5, 1, 16), Blocks.STONE.defaultBlockState());
        var raised = eye.add(0.0D, 3.0D, 4.0D);
        var overTheWall = Vec3.atCenterOf(high).add(0.0D, 0.5D, 0.0D).subtract(raised).normalize();
        helper.assertTrue(helper.getLevel().clip(new ClipContext(raised, Vec3.atCenterOf(high).add(0.0D, 0.49D, 0.0D),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos().equals(high),
                "the camera should see the block over the wall, the test proves nothing otherwise");
        hit = ArcadeAim.pick(player, raised, overTheWall, eye);
        helper.assertFalse(hit.getType() == HitResult.Type.BLOCK && ((BlockHitResult) hit).getBlockPos().equals(high), "a block the eyes cannot see should not be picked");

        // and nothing out of reach is picked, however clear the view
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.AIR.defaultBlockState());
        helper.setBlock(new BlockPos(5, 1, 16), Blocks.AIR.defaultBlockState());
        var far = helper.absolutePos(new BlockPos(5, 1, 10));
        helper.setBlock(new BlockPos(5, 1, 10), Blocks.STONE.defaultBlockState());
        hit = ArcadeAim.pick(player, origin, Vec3.atCenterOf(far).subtract(origin).normalize(), eye);
        helper.assertTrue(hit.getType() == HitResult.Type.MISS, "a block ten blocks away is out of reach");
        helper.succeed();
    }
}

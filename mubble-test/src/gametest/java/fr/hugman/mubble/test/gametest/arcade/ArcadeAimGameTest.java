package fr.hugman.mubble.test.gametest.arcade;

import fr.hugman.mubble.arcade.ArcadeAim;
import fr.hugman.mubble.arcade.ArcadeShoulder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The orbit camera against real blocks: it never sits inside a wall, and meanwhile the hands of the
 * player reach out to what stands in front of them, never to a block. Holding a weapon, it aims
 * over the shoulder, and the eyes of the player look at what its crosshair points at.
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
    public void goingDownAStepPullsTheTrailingFocusInOnlyAsFarAsTheStepRequires(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        // a step a block high: the player went down it, north, and the focus still trails on top of it
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 12), new BlockPos(9, 1, 20), Blocks.STONE.defaultBlockState());
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var target = helper.absoluteVec(new Vec3(5.5D, 1.0D, 10.8D));
        var focus = helper.absoluteVec(new Vec3(5.5D, 1.45D, 12.2D));
        // seen from the height of a rolling player's eyes, the edge of the step stands in between
        double height = 0.4D;
        var trailed = ArcadeAim.trail(helper.getLevel(), player, target, focus, height);
        helper.assertTrue(!trailed.equals(target), "the focus jumped onto the player");
        helper.assertTrue(!trailed.equals(focus), "the focus stayed behind the edge of the step");
        double along = trailed.subtract(target).length() / focus.subtract(target).length();
        helper.assertTrue(along > 0.5D && along < 1.0D, "the focus pulled in only as far as the step requires, kept " + along + " of the gap");
        var from = target.add(0.0D, height, 0.0D);
        var to = trailed.add(0.0D, height, 0.0D);
        helper.assertTrue(helper.getLevel().clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS, "nothing between the player and the focus any more");

        // in the open, the focus trails where it is
        var open = helper.absoluteVec(new Vec3(5.5D, 1.0D, 8.5D));
        helper.assertTrue(ArcadeAim.trail(helper.getLevel(), player, target, open, height).equals(open), "nothing in between, nothing to pull in");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void somethingBetweenThePlayerAndTheCameraDoesNotMoveIt(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var focus = helper.absoluteVec(new Vec3(5.5D, 2.6D, 10.5D));
        // a pillar between the player and where the camera sits, which stays in the open
        ArcadeTestKit.fill(helper, new BlockPos(5, 1, 12), new BlockPos(5, 4, 12), Blocks.STONE.defaultBlockState());
        double distance = ArcadeAim.clearDistance(helper.getLevel(), player, focus, NORTH, 5.0D, 0.3D);
        helper.assertTrue(Math.abs(distance - 5.0D) < 1.0E-9D, "the camera stays where it is, got " + distance);
        helper.assertTrue(ArcadeAim.cameraDistance(helper.getLevel(), player, focus, NORTH, 5.0D) < 2.0D, "the pillar does stand in between");
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aCameraBackingIntoAWallComesInJustEnough(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var focus = helper.absoluteVec(new Vec3(5.5D, 2.6D, 10.5D));
        // a thick wall from three blocks behind the player, where the camera would sit
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 13), new BlockPos(9, 5, 17), Blocks.STONE.defaultBlockState());
        double margin = 0.3D;
        double distance = ArcadeAim.clearDistance(helper.getLevel(), player, focus, NORTH, 5.0D, margin);
        double wall = helper.absoluteVec(new Vec3(0.0D, 0.0D, 13.0D)).z;
        double camera = focus.z + distance;
        helper.assertTrue(camera <= wall - 0.1D - margin + 1.0E-6D, "the camera keeps its room out of the wall, at z=" + camera + " for a wall at z=" + wall);
        helper.assertTrue(camera >= wall - 0.1D - margin - 0.05D, "the camera comes no closer than it has to, at z=" + camera + " for a wall at z=" + wall);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void theHandsReachWhatStandsInFrontAndNeverABlock(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(5.5D, 1.0D, 20.5D)));
        var eye = player.getEyePosition();
        // facing north: a yaw of 180 degrees
        float north = 180.0F;

        // a block right in front of the player is not something the hands reach
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.STONE.defaultBlockState());
        var hit = ArcadeAim.pickAhead(player, eye, north);
        helper.assertTrue(hit.getType() == HitResult.Type.MISS, "a block in front of the player, got " + hit.getType());
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.AIR.defaultBlockState());

        // a small animal two blocks ahead, well below the eyes, is
        var chicken = helper.spawnWithNoFreeWill(EntityTypes.CHICKEN, new BlockPos(5, 1, 18));
        hit = ArcadeAim.pickAhead(player, eye, north);
        helper.assertTrue(hit instanceof EntityHitResult entityHit && entityHit.getEntity() == chicken, "a chicken in front of the player, got " + hit.getType());

        // a bit off to the side still counts
        hit = ArcadeAim.pickAhead(player, eye, north + 10.0F);
        helper.assertTrue(hit instanceof EntityHitResult entityHit && entityHit.getEntity() == chicken, "a chicken a little to the side, got " + hit.getType());

        // but not behind the player
        hit = ArcadeAim.pickAhead(player, eye, 0.0F);
        helper.assertTrue(hit.getType() == HitResult.Type.MISS, "a chicken behind the player, got " + hit.getType());

        // nor through a wall
        helper.setBlock(new BlockPos(5, 1, 19), Blocks.STONE.defaultBlockState());
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.STONE.defaultBlockState());
        hit = ArcadeAim.pickAhead(player, eye, north);
        helper.assertTrue(hit.getType() == HitResult.Type.MISS, "a chicken behind a wall, got " + hit.getType());
        helper.setBlock(new BlockPos(5, 1, 19), Blocks.AIR.defaultBlockState());
        helper.setBlock(new BlockPos(5, 2, 19), Blocks.AIR.defaultBlockState());

        // nor out of reach
        chicken.setPos(helper.absoluteVec(new Vec3(5.5D, 1.0D, 12.5D)));
        hit = ArcadeAim.pickAhead(player, eye, north);
        helper.assertTrue(hit.getType() == HitResult.Type.MISS, "a chicken eight blocks away, got " + hit.getType());
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aimingOverTheShoulderTheEyesLookAtWhatTheCrosshairPointsAt(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(5.5D, 1.0D, 20.5D)));
        var eye = player.getEyePosition();
        // a wall eight blocks north, and the camera behind the right shoulder of a player facing it
        ArcadeTestKit.fill(helper, new BlockPos(0, 1, 12), new BlockPos(9, 5, 12), Blocks.STONE.defaultBlockState());
        var camera = eye.add(0.6D, 0.15D, 1.6D);
        var target = ArcadeAim.shoulderTarget(helper.getLevel(), player, camera, NORTH, eye, 96.0D);
        double wall = helper.absoluteVec(new Vec3(0.0D, 0.0D, 13.0D)).z;
        helper.assertTrue(Math.abs(target.z - wall) < 1.0E-6D, "the crosshair points at the wall at z=" + wall + ", got z=" + target.z);
        helper.assertTrue(Math.abs(target.x - camera.x) < 1.0E-6D && Math.abs(target.y - camera.y) < 1.0E-6D, "straight ahead of the camera, got " + target);

        // the eyes, beside the camera, look at the same spot of the wall
        var rotation = ArcadeShoulder.lookAt(eye, target);
        var reach = eye.add(Vec3.directionFromRotation(rotation.x, rotation.y).scale(16.0D));
        var hit = helper.getLevel().clip(new ClipContext(eye, reach, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        helper.assertTrue(hit.getLocation().distanceTo(target) < 1.0E-3D, "the eyes should hit " + target + ", hit " + hit.getLocation());

        // a block between the camera and the player is never aimed at
        helper.setBlock(new BlockPos(6, 2, 21), Blocks.STONE.defaultBlockState());
        var behind = ArcadeAim.shoulderTarget(helper.getLevel(), player, camera, NORTH, eye, 96.0D);
        helper.assertTrue(behind.distanceTo(target) < 1.0E-6D, "the block behind the eyes is ignored, got " + behind);

        // nor is the player, and an entity in front is
        var chicken = helper.spawnWithNoFreeWill(EntityTypes.CHICKEN, new BlockPos(6, 1, 16));
        chicken.setPos(new Vec3(camera.x, camera.y - 0.3D, chicken.getZ()));
        var atChicken = ArcadeAim.shoulderTarget(helper.getLevel(), player, camera, NORTH, eye, 96.0D);
        helper.assertTrue(atChicken.z > target.z + 2.0D, "the crosshair points at the chicken in front of the wall, got " + atChicken);
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void holdingABowACrossbowOrATridentAimsOverTheShoulder(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        for (var item : new Item[]{Items.BOW, Items.CROSSBOW, Items.TRIDENT}) {
            // held, before charging, while charging, and once shot: the camera stays at the shoulder
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
            helper.assertTrue(ArcadeShoulder.aims(player), "holding " + item);
            player.startUsingItem(InteractionHand.MAIN_HAND);
            helper.assertTrue(ArcadeShoulder.aims(player), "charging " + item);
            player.stopUsingItem();
            helper.assertTrue(ArcadeShoulder.aims(player), "after shooting " + item);
        }
        // what charges nothing does not
        for (var item : new Item[]{Items.BREAD, Items.SHIELD, Items.SPYGLASS, Items.DIAMOND_SWORD}) {
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
            helper.assertFalse(ArcadeShoulder.aims(player), "holding " + item);
        }

        // a loaded crossbow aims too, in either hand
        var loaded = new ItemStack(Items.CROSSBOW);
        loaded.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(List.of(new ItemStack(Items.ARROW))));
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        player.setItemInHand(InteractionHand.OFF_HAND, loaded);
        helper.assertTrue(ArcadeShoulder.aims(player), "holding a loaded crossbow in the off hand");
        helper.succeed();
    }
}

package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.platform.CloudPlatform;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;

/**
 * Standing on entities that are solid: boats, which always are, and cloud platforms, which only are
 * for a player holding the cloud power-up and coming from above. The simulation and the server have
 * to see them exactly as vanilla collisions do.
 */
public class PlatformerSolidEntityGameTest {
    /** Falls onto the entity, stands, jumps on it twice, then ground pounds back onto it. */
    private static PlatformerInputFrame onAndOff(PlatformerTestKit.Frames frames, int t) {
        return switch (t) {
            case 30, 60 -> frames.press(JUMP);
            case 36, 66 -> {
                frames.letGo(JUMP);
                yield frames.next();
            }
            case 90 -> frames.press(JUMP);
            case 94 -> {
                frames.letGo(JUMP);
                yield frames.press(CROUCH);
            }
            case 96 -> {
                frames.letGo(CROUCH);
                yield frames.next();
            }
            default -> frames.next();
        };
    }

    private static void standJumpAndPound(GameTestHelper helper, PlatformerTestClient client, Entity support) {
        var frames = new PlatformerTestKit.Frames();
        Set<PlatformerMove> played = new HashSet<>();
        client.play(130, t -> {
            client.tick(onAndOff(frames, t));
            played.add(client.controller().state().move);
        }, () -> {
            client.assertUndisturbed("standing, jumping and ground pounding on " + support.getType().getDescriptionId());
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            helper.assertTrue(played.contains(PlatformerMoves.GROUND_POUND_LAND), "the ground pound should have landed, the moves were " + played);
            double top = support.getBoundingBox().maxY;
            helper.assertTrue(Math.abs(client.self().getY() - top) < 1.0E-3D, "the player should end standing on top, at " + top + ", not at " + client.self().getY());
            helper.assertTrue(client.self().onGround(), "standing on top");
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void aBoatHoldsThePlayerUp(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var boat = helper.spawn(EntityTypes.OAK_BOAT, new BlockPos(5, 1, 10));
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 5, 10), PlatformerProfileIds.TRIAL);
        standJumpAndPound(helper, client, boat);
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void aCloudPlatformHoldsUpAPlayerWithTheCloudPowerUp(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var platform = helper.spawn(SuperMarioEntityTypes.CLOUD_PLATFORM, new BlockPos(5, 3, 10));
        platform.setDuration(CloudPlatform.INFINITE_DURATION);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 7, 10), PlatformerProfileIds.TRIAL);
        var cloud = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.CLOUD);
        client.server().setPowerUp(cloud);
        client.self().setPowerUp(cloud);
        standJumpAndPound(helper, client, platform);
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void aBackflipGoesUpThroughACloudPlatformAndLandsOnIt(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var platform = helper.spawn(SuperMarioEntityTypes.CLOUD_PLATFORM, new BlockPos(5, 3, 10));
        platform.setDuration(CloudPlatform.INFINITE_DURATION);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 10), PlatformerProfileIds.TRIAL);
        var cloud = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.CLOUD);
        client.server().setPowerUp(cloud);
        client.self().setPowerUp(cloud);
        var frames = new PlatformerTestKit.Frames();
        client.play(60, t -> client.tick(switch (t) {
            case 5 -> frames.press(CROUCH);
            case 8 -> frames.press(JUMP);
            case 9 -> {
                frames.letGo(CROUCH | JUMP);
                yield frames.next();
            }
            default -> frames.next();
        }), () -> {
            client.assertUndisturbed("a backflip up through a cloud platform");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
            double top = platform.getBoundingBox().maxY;
            helper.assertTrue(client.self().onGround() && Math.abs(client.self().getY() - top) < 1.0E-3D,
                    "a cloud platform is only solid from above: the player should go up through it and land on it, at " + top + ", not at " + client.self().getY());
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 300)
    public void runningAcrossBoatsStaysInLockstep(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        for (int z = 6; z <= 30; z += 2) {
            helper.spawn(EntityTypes.OAK_BOAT, new BlockPos(5, 1, z));
        }
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        client.play(80, t -> client.tick(t % 20 == 10 ? frames.tap(JUMP) : frames.next()), () -> {
            client.assertUndisturbed("running and jumping across a row of boats");
            helper.assertValueEqual(client.corrections(), 0, "corrections (" + client.events() + ")");
        });
    }
}

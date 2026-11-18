package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;

import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.block.Blocks;

/**
 * The statue when the platformer module drives the player: its own ground pound has the motion, core only
 * hears of the two ends of it, and the statue and the impact are the ones of vanilla movement.
 */
public class PlatformerStatueGameTest {
    private static final int RELEASE_TICK = 20;
    private static final Input SNEAK = new Input(false, false, false, false, false, true, false);

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void aPlatformerPoundTurnsTheHolderToStoneAndLandsTheImpact(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, new BlockPos(5, 1, 3));
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, new BlockPos(6, 1, 3));
        helper.setBlock(new BlockPos(5, 0, 3), SuperMarioBlocks.BRICK_BLOCK);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 5, 3), PlatformerProfileIds.TRIAL);
        client.server().setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.STATUE));
        var frames = new PlatformerTestKit.Frames();
        Set<PlatformerMove> played = new HashSet<>();
        boolean[] statue = {false};
        boolean[] invulnerableWhileStone = {true};
        client.play(60, t -> {
            boolean held = t < RELEASE_TICK;
            client.server().setLastClientInput(held && t >= 2 ? SNEAK : Input.EMPTY);
            client.server().setShiftKeyDown(held && t >= 2);
            client.tick(t == 2 ? frames.press(CROUCH) : frames.next());
            played.add(client.controller().state().move);
            statue[0] |= client.server().isStatue();
            if (client.server().isStatue()) {
                var level = helper.getLevel();
                invulnerableWhileStone[0] &= client.server().isInvulnerableTo(level, level.damageSources().generic());
            }
        }, () -> {
            helper.assertTrue(played.contains(PlatformerMoves.GROUND_POUND_LAND), "the ground pound never landed, went through " + played);
            helper.assertTrue(statue[0], "the platformer pound should have turned the holder to stone");
            helper.assertTrue(invulnerableWhileStone[0], "the statue should be invulnerable");
            helper.assertTrue(goomba.isDeadOrDying(), "the impact should defeat the goomba");
            helper.assertTrue(cow.getHealth() < cow.getMaxHealth(), "the impact should hurt the cow");
            helper.assertBlockPresent(Blocks.AIR, new BlockPos(5, 0, 3));
            helper.assertFalse(client.server().isStatue(), "letting go of sneak should end the statue");
            helper.assertFalse(client.server().isGroundPounding(), "the landing should end the pound");
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 200)
    public void aPlatformerPoundOfAFormWithoutAStatueTurnsNothingToStone(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 5, 3), PlatformerProfileIds.TRIAL);
        client.server().setPowerUp(PowerUpFixtures.get(helper, PowerUpFixtures.FLOATER));
        var frames = new PlatformerTestKit.Frames();
        boolean[] statue = {false};
        client.play(30, t -> {
            client.tick(t == 2 ? frames.press(CROUCH) : frames.next());
            statue[0] |= client.server().isStatue() || client.server().isGroundPounding();
        }, () -> helper.assertFalse(statue[0], "a form without the ability should neither pound nor turn to stone"));
    }
}

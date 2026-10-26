package fr.hugman.mubble.test.gametest.platformer;

import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.ACTION;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * Vanilla effects, attributes and equipment bending the platformer physics as they bend vanilla's,
 * under {@code mubble:trial}: what any other mod adds through them is honored the same way.
 */
public class PlatformerEffectsGameTest {
    private static final Identifier MODIFIER = Identifier.fromNamespaceAndPath("mubble-gametest", "effects");
    /** The height of a held jump under {@code mubble:trial}, see {@link PlatformerMovementGameTest#jumpReachesItsAuthoredHeight}. */
    private static final double JUMP_HEIGHT = 2.2D;
    /** The run speed of {@code mubble:trial}. */
    private static final double RUN_SPEED = 0.4D;

    private static Player standing(GameTestHelper helper, BlockPos pos) {
        var player = PlatformerTestKit.client(helper, pos, PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 4; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertTrue(player.onGround(), "the stand-in never landed, the test proves nothing");
        return player;
    }

    private static PlatformerController controller(Player player) {
        return PlatformerController.of(player);
    }

    private static PlatformerMove move(Player player) {
        return controller(player).state().move;
    }

    private static void effect(Player player, Holder<MobEffect> effect, int amplifier) {
        player.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, amplifier));
    }

    private static void modifier(Player player, Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation) {
        player.getAttribute(attribute).addPermanentModifier(new AttributeModifier(MODIFIER, amount, operation));
    }

    private static void assertClose(GameTestHelper helper, double expected, double actual, double tolerance, String what) {
        helper.assertTrue(Math.abs(actual - expected) < tolerance, what + ": expected " + expected + ", got " + actual);
    }

    /** Runs forward with sprint held for {@code ticks} ticks, and returns the horizontal speed reached. */
    private static double runSpeed(Player player, int ticks) {
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < ticks; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        return controller(player).state().horizontalSpeed();
    }

    /** Jumps with jump held all along, and returns the highest the feet went. */
    private static double heldJump(GameTestHelper helper, Player player) {
        var frames = new PlatformerTestKit.Frames();
        double startY = player.getY();
        PlatformerTestKit.step(player, frames.press(JUMP));
        helper.assertValueEqual(move(player), PlatformerMoves.JUMP, "move after pressing jump");
        double peak = 0.0D;
        for (int i = 0; i < 80 && !(player.onGround() && i > 0); i++) {
            PlatformerTestKit.step(player, frames.next());
            peak = Math.max(peak, player.getY() - startY);
        }
        helper.assertTrue(player.onGround(), "the player never came back down");
        return peak;
    }

    // -- the ground ---------------------------------------------------------------------------

    @GameTest(structure = PlatformerTestKit.LANE)
    public void speedAndSlownessScaleTheRun(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var plain = standing(helper, new BlockPos(2, 1, 3));
        var fast = standing(helper, new BlockPos(5, 1, 3));
        var slow = standing(helper, new BlockPos(8, 1, 3));
        // as in vanilla: 20% faster per level of speed, 15% slower per level of slowness
        effect(fast, MobEffects.SPEED, 1);
        effect(slow, MobEffects.SLOWNESS, 0);

        assertClose(helper, RUN_SPEED, runSpeed(plain, 20), 1.0E-3D, "the plain run");
        assertClose(helper, RUN_SPEED * 1.4D, runSpeed(fast, 20), 1.0E-3D, "the run under speed II");
        assertClose(helper, RUN_SPEED * 0.85D, runSpeed(slow, 20), 1.0E-3D, "the run under slowness I");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void blindnessKeepsFromTheSpeedMoves(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 3));
        var controller = controller(player);
        helper.assertTrue(controller.allows(PlatformerMoves.ROLL), "a roll is allowed with sight, the test proves nothing");
        effect(player, MobEffects.BLINDNESS, 0);

        // as sprinting in vanilla, whatever the profile says of food
        helper.assertFalse(controller.allows(PlatformerMoves.ROLL), "a blind player should not roll");
        helper.assertFalse(controller.allows(PlatformerMoves.LONG_JUMP), "a blind player should not long jump");
        helper.assertTrue(controller.allows(PlatformerMoves.JUMP), "a blind player still jumps");
        double pace = runSpeed(player, 20);
        helper.assertValueEqual(move(player), PlatformerMoves.WALK, "a blind player walks rather than runs");
        assertClose(helper, controller.tuning().walkSpeed(), pace, 1.0E-3D, "the pace of a blind player");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void movementEfficiencyUndoesTheSlowdownOfSoulSand(GameTestHelper helper) {
        PlatformerTestKit.floor(helper, Blocks.SOUL_SAND.defaultBlockState());
        var plain = standing(helper, new BlockPos(3, 1, 3));
        var efficient = standing(helper, new BlockPos(7, 1, 3));
        // what soul speed does, at its fullest
        modifier(efficient, Attributes.MOVEMENT_EFFICIENCY, 1.0D, AttributeModifier.Operation.ADD_VALUE);

        double factor = Blocks.SOUL_SAND.getSpeedFactor();
        assertClose(helper, RUN_SPEED * factor, runSpeed(plain, 20), 1.0E-3D, "the run on soul sand");
        assertClose(helper, RUN_SPEED, runSpeed(efficient, 20), 1.0E-3D, "the run on soul sand with full movement efficiency");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void theEdgeOfIceOverAirStillSlides(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 1, 0), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 1, 5), Blocks.ICE.defaultBlockState());
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 2, 3), PlatformerProfileIds.TRIAL);
        // the center over the air beyond the ice, the feet still on its edge
        var edge = helper.absoluteVec(new Vec3(5.5D, 2.0D, 6.2D));
        player.setPos(edge);
        var world = controller(player).world();

        helper.assertValueEqual(world.supportingPos(edge), helper.absolutePos(new BlockPos(5, 1, 5)), "the block the feet rest on");
        assertClose(helper, Blocks.ICE.getFriction(), world.friction(edge), 1.0E-6D, "the friction on the edge of the ice");
        helper.succeed();
    }

    // -- jumps and falls ----------------------------------------------------------------------

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpBoostRaisesJumpsAtTheVanillaRatio(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        effect(player, MobEffects.JUMP_BOOST, 0);

        // vanilla takes off 0.1 faster per level, out of 0.42: a height goes with the square of that
        double ratio = (0.42D + 0.1D) / 0.42D;
        assertClose(helper, JUMP_HEIGHT * ratio * ratio, heldJump(helper, player), 0.03D, "a held jump under jump boost I");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void jumpStrengthScalesJumps(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        modifier(player, Attributes.JUMP_STRENGTH, -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        // half the take-off speed, a quarter of the height
        assertClose(helper, JUMP_HEIGHT * 0.25D, heldJump(helper, player), 0.03D, "a held jump at half the jump strength");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void gravityScalesJumps(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        modifier(player, Attributes.GRAVITY, 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        // the same take-off speed against twice the gravity: half the height, give or take the tick the apex falls on
        assertClose(helper, JUMP_HEIGHT * 0.5D, heldJump(helper, player), 0.05D, "a held jump under twice the gravity");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void slowFallingLowersGravityAsInVanilla(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var plain = PlatformerTestKit.client(helper, new BlockPos(3, 10, 5), PlatformerProfileIds.TRIAL);
        var slow = PlatformerTestKit.client(helper, new BlockPos(7, 10, 5), PlatformerProfileIds.TRIAL);
        effect(slow, MobEffects.SLOW_FALLING, 0);
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 10; i++) {
            var frame = frames.next();
            PlatformerTestKit.step(plain, frame);
            PlatformerTestKit.step(slow, frame);
        }

        // vanilla brings its gravity down from 0.08 to 0.01 on the way down: an eighth
        double plainSpeed = controller(plain).state().vy;
        double slowSpeed = controller(slow).state().vy;
        helper.assertTrue(plainSpeed < -0.5D, "the plain fall never picked up speed, the test proves nothing: " + plainSpeed);
        assertClose(helper, plainSpeed / 8.0D, slowSpeed, 1.0E-6D, "the fall under slow falling");

        // and its fall no faster than 0.49 b/t, which is where its gravity and drag balance out
        controller(slow).state().vy = -2.0D;
        PlatformerTestKit.step(slow, frames.next());
        assertClose(helper, -0.49D, controller(slow).state().vy, 1.0E-6D, "a fast fall under slow falling");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void levitationLiftsAsInVanilla(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var player = standing(helper, new BlockPos(5, 1, 5));
        effect(player, MobEffects.LEVITATION, 1);
        double y = player.getY();
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 40; i++) {
            PlatformerTestKit.step(player, frames.next());
        }

        // vanilla aims for 0.05 b/t per level
        assertClose(helper, 0.1D, controller(player).state().vy, 1.0E-3D, "the rise under levitation II");
        helper.assertTrue(player.getY() > y + 2.0D, "the player should have risen, moved by " + (player.getY() - y));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void airDragModifierBendsTheDragOfTheAir(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var plain = standing(helper, new BlockPos(3, 1, 3));
        var dragged = standing(helper, new BlockPos(7, 1, 3));
        modifier(dragged, Attributes.AIR_DRAG_MODIFIER, 1.0D, AttributeModifier.Operation.ADD_VALUE);

        // as vanilla bends its own: a modifier of 2 takes twice as much speed away
        double drag = controller(plain).tuning().airDrag();
        assertClose(helper, drag, airDrag(helper, plain), 1.0E-6D, "the drag of the air");
        assertClose(helper, 1.0D - (1.0D - drag) * 2.0D, airDrag(helper, dragged), 1.0E-6D, "the drag of the air with twice the modifier");
        helper.succeed();
    }

    /** Runs, jumps, lets go of the stick, and returns the share of horizontal speed one tick in the air keeps. */
    private static double airDrag(GameTestHelper helper, Player player) {
        runSpeed(player, 12);
        var frames = new PlatformerTestKit.Frames().forward();
        PlatformerTestKit.step(player, frames.press(JUMP));
        frames.release();
        PlatformerTestKit.step(player, frames.next());
        double before = controller(player).state().horizontalSpeed();
        PlatformerTestKit.step(player, frames.next());
        helper.assertFalse(player.onGround(), "the player should still be in the air");
        return controller(player).state().horizontalSpeed() / before;
    }

    // -- water and climbing -------------------------------------------------------------------

    /** Swims forward from the bottom of the pool for {@code ticks} ticks, and returns the pace of the last one. */
    private static double swimPace(GameTestHelper helper, Player player, int ticks) {
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 10; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.SWIM, "deep in water");
        helper.assertTrue(controller(player).state().grounded, "at the bottom of the pool");
        frames.forward();
        double pace = 0.0D;
        for (int i = 0; i < ticks; i++) {
            var before = player.position();
            PlatformerTestKit.step(player, frames.next());
            pace = player.position().subtract(before).horizontalDistance();
        }
        return pace;
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void depthStriderSwimsAtTheVanillaPace(GameTestHelper helper) {
        PlatformerMovementGameTest.pool(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        // what depth strider does, at its fullest
        modifier(player, Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0D, AttributeModifier.Operation.ADD_VALUE);

        // as vanilla on the bottom: the strokes of the walking pace, 0.1, against a drag of 0.546
        double pace = swimPace(helper, player, 25);
        assertClose(helper, 0.1D / (1.0D - 0.546D), pace, 0.005D, "the pace with full water movement efficiency");
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void dolphinsGraceSwimsFaster(GameTestHelper helper) {
        PlatformerMovementGameTest.pool(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 3), PlatformerProfileIds.TRIAL);
        effect(player, MobEffects.DOLPHINS_GRACE, 0);

        // as vanilla: strokes of 0.02 against a drag of 0.96, building up the pace tick after tick
        int ticks = 15;
        double pace = swimPace(helper, player, ticks);
        assertClose(helper, 0.02D * (1.0D - Math.pow(0.96D, ticks)) / (1.0D - 0.96D), pace, 0.005D, "the pace under dolphin's grace");
        helper.succeed();
    }

    /** A column of powder snow four blocks high at x = 5, z = 5. */
    private static void powderSnow(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(5, 1, 5), new BlockPos(5, 4, 5), Blocks.POWDER_SNOW.defaultBlockState());
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void leatherBootsClimbOutOfPowderSnow(GameTestHelper helper) {
        powderSnow(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 5), PlatformerProfileIds.TRIAL);
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        double y = player.getY();
        var frames = new PlatformerTestKit.Frames().hold(JUMP);
        for (int i = 0; i < 10; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        helper.assertValueEqual(move(player), PlatformerMoves.CLIMB, "jump held in powder snow, in leather boots");
        helper.assertTrue(player.getY() > y + 0.8D, "the player should climb, moved by " + (player.getY() - y));
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void powderSnowIsNotClimbedWithoutLeatherBoots(GameTestHelper helper) {
        powderSnow(helper);
        var player = PlatformerTestKit.client(helper, new BlockPos(5, 1, 5), PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames().hold(JUMP);
        for (int i = 0; i < 10; i++) {
            PlatformerTestKit.step(player, frames.next());
            helper.assertFalse(move(player) == PlatformerMoves.CLIMB, "powder snow is climbed without leather boots, tick " + i);
        }
        helper.succeed();
    }

    // -- both sides ---------------------------------------------------------------------------

    /** The same effects, modifiers and boots on both sides, as the server syncs them to the client. */
    private static void bend(Player player) {
        effect(player, MobEffects.SPEED, 0);
        effect(player, MobEffects.JUMP_BOOST, 0);
        effect(player, MobEffects.SLOW_FALLING, 0);
        modifier(player, Attributes.JUMP_STRENGTH, 0.05D, AttributeModifier.Operation.ADD_VALUE);
        modifier(player, Attributes.GRAVITY, 0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        modifier(player, Attributes.AIR_DRAG_MODIFIER, 0.5D, AttributeModifier.Operation.ADD_VALUE);
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
    }

    /**
     * A long jump forward, onto soul sand, a turn, a jump and a roll back, a ground pound: short enough
     * to stay on the lane, however far the effects carry the player.
     */
    private static List<PlatformerInputFrame> routine() {
        var f = new PlatformerTestKit.Frames();
        var out = new ArrayList<PlatformerInputFrame>();
        repeat(out, 4, f::next);
        f.forward().hold(SPRINT);
        repeat(out, 8, f::next);
        out.add(f.press(CROUCH));
        out.add(f.press(JUMP));
        f.letGo(CROUCH | JUMP);
        repeat(out, 30, f::next);
        f.stick(0.0F, -1.0F);
        repeat(out, 10, f::next);
        out.add(f.press(JUMP));
        repeat(out, 4, f::next);
        f.letGo(JUMP);
        repeat(out, 25, f::next);
        out.add(f.press(CROUCH));
        out.add(f.press(ACTION));
        f.letGo(CROUCH | ACTION);
        repeat(out, 10, f::next);
        f.release().letGo(SPRINT);
        repeat(out, 10, f::next);
        out.add(f.press(JUMP));
        f.letGo(JUMP);
        repeat(out, 2, f::next);
        out.add(f.press(CROUCH));
        f.letGo(CROUCH);
        repeat(out, 30, f::next);
        return out;
    }

    private static void repeat(List<PlatformerInputFrame> out, int count, Supplier<PlatformerInputFrame> frame) {
        for (int i = 0; i < count; i++) {
            out.add(frame.get());
        }
    }

    @GameTest(structure = PlatformerTestKit.LANE, maxTicks = 400)
    public void bentPhysicsStayInLockstep(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 14), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 0, 18), Blocks.ICE.defaultBlockState());
        PlatformerTestKit.fill(helper, new BlockPos(0, 0, 24), new BlockPos(PlatformerTestKit.LANE_WIDTH - 1, 0, 28), Blocks.SOUL_SAND.defaultBlockState());
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 10), PlatformerProfileIds.TRIAL);
        bend(client.server());
        bend(client.self());
        var routine = routine();
        Set<PlatformerMove> played = new HashSet<>();
        client.play(routine.size(), t -> {
            client.tick(routine.get(t));
            played.add(client.controller().state().move);
        }, () -> {
            client.assertUndisturbed("a routine played under effects and modifiers");
            helper.assertValueEqual(client.corrections(), 0, "corrections, which an honest client never needs (" + client.events() + ")");
            helper.assertValueEqual(client.serverController().validation().accepted, routine.size(), "steps the server accepted");
            helper.assertValueEqual(client.serverController().state().fingerprint(), client.controller().state().fingerprint(), "fingerprint of the state on both sides");
            helper.assertTrue(client.serverController().tuning().slowFalling(), "the effects wore off, the test proves nothing");
            helper.assertTrue(played.containsAll(Set.of(PlatformerMoves.RUN, PlatformerMoves.LONG_JUMP, PlatformerMoves.JUMP, PlatformerMoves.ROLL, PlatformerMoves.GROUND_POUND)),
                    "the routine should go through the moves it plays, it went through " + played);
        });
    }
}

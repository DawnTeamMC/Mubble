package fr.hugman.mubble.test.gametest.platformer;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.ACTION;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.CROUCH;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.JUMP;
import static fr.hugman.mubble.platformer.sim.PlatformerInputFrame.SPRINT;

import fr.hugman.mubble.platformer.references.PlatformerMoveIds;
import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.platformer.tags.PlatformerMoveTags;
import fr.hugman.mubble.test.gametest.mixin.FoodDataAccessor;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerInteraction;
import fr.hugman.mubble.platformer.PlatformerPhysics;
import fr.hugman.mubble.platformer.PlatformerPlayer;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.PlatformerProfiles;
import fr.hugman.mubble.platformer.PlatformerUnlocks;
import fr.hugman.mubble.platformer.access.AccessMode;
import fr.hugman.mubble.platformer.access.PlatformerSource;
import fr.hugman.mubble.platformer.access.PlatformerSources;
import fr.hugman.mubble.platformer.access.MoveSelector;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.PlatformerAttributes;
import fr.hugman.mubble.platformer.item.PlatformerItems;
import fr.hugman.mubble.platformer.reward.PlatformerMoveReward;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

/**
 * How the layer comes and goes: what turns it on, what keeps moves away, what suspends it, and that
 * nothing is left behind when it stops.
 */
public class PlatformerLifecycleGameTest {
    private static final Identifier DENY_AERIAL = Identifier.fromNamespaceAndPath("mubble-gametest", "no_aerial");

    private static PlatformerTestClient player(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        return PlatformerTestClient.join(helper, new BlockPos(5, 1, 5), null);
    }

    private static void finish(GameTestHelper helper, PlatformerTestClient client) {
        client.leave();
        helper.succeed();
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void withoutASourceThePlayerIsVanilla(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        client.tickServer();
        var controller = PlatformerController.of(player);
        helper.assertFalse(controller.isActive(), "a player without a source should not have the layer on");
        helper.assertValueEqual(player.getAttributeBaseValue(PlatformerAttributes.PLATFORMER_RUN_SPEED), PlatformerAttributes.PLATFORMER_RUN_SPEED.value().getDefaultValue(), "base run speed");
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        player.jumpFromGround();
        helper.assertTrue(Math.abs(player.getDeltaMovement().y - 0.42D) < 1.0E-6D, "a vanilla jump should still be one, got " + player.getDeltaMovement().y);
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.BLOCK), "nothing is refused to a vanilla player");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void marioBootsEnableOnlyTheMovesOwned(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = PlatformerController.of(player);
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(PlatformerItems.MARIO_BOOTS));
        client.tickServer();
        helper.assertFalse(controller.isActive(), "boots enabling moves the player owns none of should leave the layer off");

        PlatformerUnlocks.unlock(player, MoveSelector.move(PlatformerMoveIds.JUMP));
        PlatformerUnlocks.unlock(player, MoveSelector.move(PlatformerMoveIds.DOUBLE_JUMP));
        client.tickServer();
        helper.assertTrue(controller.isActive(), "boots with an owned move");
        helper.assertValueEqual(controller.access().profile(), Optional.of(PlatformerProfileIds.OVERWORLD), "the profile of the boots");
        helper.assertTrue(controller.allows(PlatformerMoves.JUMP), "an owned move");
        helper.assertTrue(controller.allows(PlatformerMoves.DOUBLE_JUMP), "an owned move");
        helper.assertFalse(controller.allows(PlatformerMoves.DIVE), "a move not owned");
        helper.assertTrue(controller.allows(PlatformerMoves.WALK), "walking is always there");

        // a deny on a tag wins over the boots, whatever its priority
        PlatformerSources.add(player, new PlatformerSource(DENY_AERIAL, Optional.empty(), Map.of(MoveSelector.tag(PlatformerMoveTags.AERIAL), AccessMode.DENY), -10, Optional.empty()));
        client.tickServer();
        helper.assertFalse(controller.allows(PlatformerMoves.DOUBLE_JUMP), "a denied move, owned and enabled by the boots");
        helper.assertFalse(controller.isActive(), "with every owned move denied, the layer has nothing to do");
        PlatformerSources.remove(player, DENY_AERIAL);

        // plain leather boots do nothing
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        client.tickServer();
        helper.assertFalse(controller.isActive(), "boots without the component");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void bootsHeldInTheHandDoNothing(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        PlatformerUnlocks.unlock(player, MoveSelector.tag(PlatformerMoveTags.ALL));
        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(PlatformerItems.MARIO_BOOTS));
        client.tickServer();
        helper.assertFalse(PlatformerController.of(player).isActive(), "the component only counts in the slots it names");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void aSourceRunsOutAtItsExpiry(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var id = Identifier.fromNamespaceAndPath("mubble-gametest", "timed");
        long now = helper.getLevel().getGameTime();
        PlatformerSources.add(player, new PlatformerSource(id, Optional.of(PlatformerProfileIds.TRIAL), Map.of(MoveSelector.tag(PlatformerMoveTags.ALL), AccessMode.FORCE), 0, Optional.of(now + 5)));
        client.tickServer();
        helper.assertTrue(PlatformerController.of(player).isActive(), "a source before its expiry");
        helper.runAfterDelay(6, () -> {
            client.tickServer();
            helper.assertFalse(PlatformerController.of(player).isActive(), "a source past its expiry");
            helper.assertTrue(PlatformerSources.get(player).find(id).isEmpty(), "an expired source should be removed from the player");
            finish(helper, client);
        });
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void takingTheBootsOffMidRollEndsCleanly(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = PlatformerController.of(player);
        PlatformerUnlocks.unlock(player, MoveSelector.tag(PlatformerMoveTags.ALL));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(PlatformerItems.MARIO_BOOTS));
        var frames = new PlatformerTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.press(CROUCH));
        PlatformerTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(controller.state().move, PlatformerMoves.ROLL, "crouch and action while running");
        helper.assertValueEqual(player.getPose(), Pose.SWIMMING, "the pose of a roll");

        player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        client.tickServer();
        helper.assertFalse(controller.isActive(), "the layer without the boots");
        helper.assertValueEqual(player.getPose(), Pose.STANDING, "the pose once vanilla is back");
        helper.assertTrue(((PlatformerPlayer) player).mubble$platformerVisual().move() == null, "the move other players see should be cleared");
        helper.assertValueEqual(player.getAttributeBaseValue(PlatformerAttributes.PLATFORMER_RUN_SPEED), PlatformerAttributes.PLATFORMER_RUN_SPEED.value().getDefaultValue(), "base run speed once the layer is off");
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.ATTACK), "hands busy from the roll should not outlive it");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void waterIsSwumAndLavaSuspendsTheLayer(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = PlatformerController.of(player);
        PlatformerTestKit.force(player, PlatformerProfileIds.TRIAL);
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "on dry land");
        PlatformerTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.WATER.defaultBlockState());
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "the layer swims in water");

        // drained first: lava poured over water would turn to stone
        PlatformerTestKit.fill(helper, new BlockPos(2, 1, 2), new BlockPos(8, 4, 8), Blocks.AIR.defaultBlockState());
        client.tickServer();
        PlatformerTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.LAVA.defaultBlockState());
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isActive() && controller.isSuspended(), "lava is vanilla's");
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.BLOCK), "a suspended layer refuses nothing");
        PlatformerTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.AIR.defaultBlockState());
        player.clearFire();
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "out of the lava again");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void busyHandsAndTheInteractionPolicy(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        PlatformerTestKit.force(player, PlatformerProfileIds.TRIAL);
        var frames = new PlatformerTestKit.Frames();
        PlatformerTestKit.step(player, frames.next());
        // the trial is combat only
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.ATTACK), "attacking under combat_only");
        helper.assertTrue(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.BLOCK), "breaking and placing under combat_only");

        var target = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(5, 1, 7));
        var result = AttackEntityCallback.EVENT.invoker().interact(player, helper.getLevel(), InteractionHand.MAIN_HAND, target, null);
        helper.assertValueEqual(result, InteractionResult.PASS, "an attack while running");

        frames.forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            PlatformerTestKit.step(player, frames.next());
        }
        PlatformerTestKit.step(player, frames.press(CROUCH));
        PlatformerTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(PlatformerController.of(player).state().move, PlatformerMoves.ROLL, "crouch and action while running");
        result = AttackEntityCallback.EVENT.invoker().interact(player, helper.getLevel(), InteractionHand.MAIN_HAND, target, null);
        helper.assertValueEqual(result, InteractionResult.FAIL, "an attack mid-roll, hands busy");

        // the overworld profile allows everything
        PlatformerTestKit.force(player, PlatformerProfileIds.OVERWORLD);
        var standing = new PlatformerTestKit.Frames();
        for (int i = 0; i < 20; i++) {
            PlatformerTestKit.step(player, standing.next());
        }
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.BLOCK), "breaking and placing under full");
        var controller = PlatformerController.of(player);
        helper.assertFalse(controller.handsGoToMoves(false), "attack and use while standing go to the hands");
        helper.assertTrue(controller.handsGoToMoves(true), "attack and use with crouch held roll");

        // with the orbit camera, the hands reach out where the body faces: never to a block
        var orbiting = new PlatformerTestKit.Frames().orbit();
        PlatformerTestKit.step(player, orbiting.next());
        helper.assertTrue(controller.orbiting(), "a step taken with the orbit camera");
        helper.assertTrue(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.BLOCK), "breaking and placing with the orbit camera, even under full");
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.ENTITY), "using an entity with the orbit camera");
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.USE_ITEM), "using an item with the orbit camera");
        helper.assertFalse(PlatformerInteraction.refused(player, PlatformerInteraction.Kind.ATTACK), "attacking with the orbit camera");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void profilesReloadWithoutRelogging(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var live = PlatformerProfiles.serverValues();
        helper.assertTrue(live.containsKey(PlatformerProfileIds.TRIAL) && live.containsKey(PlatformerProfileIds.OVERWORLD),
                "the reload listener should have read the profile files, it has " + live.keySet());

        // a profile of this test alone, so that tests running alongside keep theirs
        var key = ResourceKey.create(PlatformerRegistries.PLATFORMER_PROFILE, Identifier.fromNamespaceAndPath("mubble-gametest", "live"));
        var trial = live.get(PlatformerProfileIds.TRIAL);
        try {
            putLive(key, withRunSpeed(trial, 0.55D));
            PlatformerSources.add(player, new PlatformerSource(PlatformerTestKit.SOURCE, Optional.of(key), Map.of(MoveSelector.tag(PlatformerMoveTags.ALL), AccessMode.FORCE), 100, Optional.empty()));
            client.tickServer();
            helper.assertValueEqual(player.getAttributeBaseValue(PlatformerAttributes.PLATFORMER_RUN_SPEED), 0.55D, "base run speed from the live profile");

            // what a reload with an edited file does
            putLive(key, withRunSpeed(trial, 0.25D));
            client.tickServer();
            helper.assertValueEqual(player.getAttributeBaseValue(PlatformerAttributes.PLATFORMER_RUN_SPEED), 0.25D, "base run speed after the reload");
        } finally {
            var restored = new HashMap<>(PlatformerProfiles.serverValues());
            restored.remove(key);
            PlatformerProfiles.setServerValues(restored);
        }
        finish(helper, client);
    }

    private static void putLive(ResourceKey<PlatformerProfile> key, PlatformerProfile profile) {
        var values = new HashMap<>(PlatformerProfiles.serverValues());
        values.put(key, profile);
        PlatformerProfiles.setServerValues(values);
    }

    private static PlatformerProfile withRunSpeed(PlatformerProfile profile, double runSpeed) {
        var physics = profile.physics();
        var g = physics.ground();
        var ground = new PlatformerPhysics.Ground(g.walkSpeed(), runSpeed, g.accelTicks(), g.decelTicks(), g.turnSpeed(), g.overCapDrag(), g.analogRunThreshold(), g.stickDeadzone(), g.minGrip());
        var newPhysics = new PlatformerPhysics(ground, physics.air(), physics.gravity(), physics.slope(), physics.bounce(), physics.safetyCeiling());
        return new PlatformerProfile(newPhysics, profile.grace(), profile.interaction(), profile.costs(), profile.fallDamage(), profile.camera(), profile.validation(), profile.moves());
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void unlockingByCommandAndByReward(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        client.serverCommand("mubble platformer unlock @self mubble:dive");
        helper.assertTrue(PlatformerUnlocks.get(player).owns(PlatformerMoves.DIVE), "a move unlocked by command");
        client.serverCommand("mubble platformer unlock @self #mubble:wall");
        helper.assertTrue(PlatformerUnlocks.get(player).owns(PlatformerMoves.WALL_JUMP) && PlatformerUnlocks.get(player).owns(PlatformerMoves.WALL_SLIDE), "a tag unlocked by command");
        client.serverCommand("mubble platformer lock @self mubble:dive");
        helper.assertFalse(PlatformerUnlocks.get(player).owns(PlatformerMoves.DIVE), "a move locked by command");

        new PlatformerMoveReward(List.of(MoveSelector.move(PlatformerMoveIds.GROUND_POUND))).grant(player);
        helper.assertTrue(PlatformerUnlocks.get(player).owns(PlatformerMoves.GROUND_POUND), "a move given by a reward");
        helper.assertTrue(PlatformerUnlocks.get(player).owns(PlatformerMoves.GROUND_POUND_LAND), "the parts of a move come with it");
        finish(helper, client);
    }

    @GameTest(structure = PlatformerTestKit.LANE)
    public void theOverworldChargesFoodAndCountsJumps(GameTestHelper helper) {
        PlatformerTestKit.floor(helper);
        var client = PlatformerTestClient.join(helper, new BlockPos(5, 1, 5), PlatformerProfileIds.OVERWORLD);
        var player = client.server();
        var food = (FoodDataAccessor) player.getFoodData();
        int jumps = player.getStats().getValue(Stats.CUSTOM.get(Stats.JUMP));
        var frames = new PlatformerTestKit.Frames();
        for (int i = 0; i < 4; i++) {
            client.tick(frames.next());
        }
        float before = food.mubbleGametest$getExhaustionLevel();
        client.tick(frames.press(JUMP));
        helper.assertValueEqual(client.controller().state().move, PlatformerMoves.JUMP, "a jump");
        helper.assertTrue(food.mubbleGametest$getExhaustionLevel() > before, "a jump under the overworld profile should cost food");
        helper.assertValueEqual(player.getStats().getValue(Stats.CUSTOM.get(Stats.JUMP)), jumps + 1, "jumps counted");

        // too hungry to sprint, too hungry for the moves of speed
        player.getFoodData().setFoodLevel(6);
        client.tickServer();
        var controller = PlatformerController.of(player);
        helper.assertFalse(controller.allows(PlatformerMoves.ROLL), "a roll on an empty stomach");
        helper.assertTrue(controller.allows(PlatformerMoves.JUMP), "a jump on an empty stomach");
        client.leave();
        helper.succeed();
    }
}

package fr.hugman.mubble.test.gametest.arcade;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.ACTION;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.CROUCH;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.JUMP;
import static fr.hugman.mubble.arcade.sim.ArcadeInputFrame.SPRINT;

import fr.hugman.mubble.arcade.references.ArcadeMoveIds;
import fr.hugman.mubble.arcade.references.ArcadeProfileIds;
import fr.hugman.mubble.arcade.tags.ArcadeMoveTags;
import fr.hugman.mubble.test.gametest.mixin.FoodDataAccessor;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadeInteraction;
import fr.hugman.mubble.arcade.ArcadePhysics;
import fr.hugman.mubble.arcade.ArcadePlayer;
import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.ArcadeProfiles;
import fr.hugman.mubble.arcade.ArcadeUnlocks;
import fr.hugman.mubble.arcade.access.AccessMode;
import fr.hugman.mubble.arcade.access.ArcadeSource;
import fr.hugman.mubble.arcade.access.ArcadeSources;
import fr.hugman.mubble.arcade.access.MoveSelector;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.ArcadeAttributes;
import fr.hugman.mubble.arcade.item.ArcadeItems;
import fr.hugman.mubble.arcade.reward.ArcadeMoveReward;
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
public class ArcadeLifecycleGameTest {
    private static final Identifier DENY_AERIAL = Identifier.fromNamespaceAndPath("mubble-gametest", "no_aerial");

    private static ArcadeTestClient player(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        return ArcadeTestClient.join(helper, new BlockPos(5, 1, 5), null);
    }

    private static void finish(GameTestHelper helper, ArcadeTestClient client) {
        client.leave();
        helper.succeed();
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void withoutASourceThePlayerIsVanilla(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        client.tickServer();
        var controller = ArcadeController.of(player);
        helper.assertFalse(controller.isActive(), "a player without a source should not have the layer on");
        helper.assertValueEqual(player.getAttributeBaseValue(ArcadeAttributes.ARCADE_RUN_SPEED), ArcadeAttributes.ARCADE_RUN_SPEED.value().getDefaultValue(), "base run speed");
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        player.jumpFromGround();
        helper.assertTrue(Math.abs(player.getDeltaMovement().y - 0.42D) < 1.0E-6D, "a vanilla jump should still be one, got " + player.getDeltaMovement().y);
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.BLOCK), "nothing is refused to a vanilla player");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void marioBootsEnableOnlyTheMovesOwned(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = ArcadeController.of(player);
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ArcadeItems.MARIO_BOOTS));
        client.tickServer();
        helper.assertFalse(controller.isActive(), "boots enabling moves the player owns none of should leave the layer off");

        ArcadeUnlocks.unlock(player, MoveSelector.move(ArcadeMoveIds.JUMP));
        ArcadeUnlocks.unlock(player, MoveSelector.move(ArcadeMoveIds.DOUBLE_JUMP));
        client.tickServer();
        helper.assertTrue(controller.isActive(), "boots with an owned move");
        helper.assertValueEqual(controller.access().profile(), Optional.of(ArcadeProfileIds.OVERWORLD), "the profile of the boots");
        helper.assertTrue(controller.allows(ArcadeMoves.JUMP), "an owned move");
        helper.assertTrue(controller.allows(ArcadeMoves.DOUBLE_JUMP), "an owned move");
        helper.assertFalse(controller.allows(ArcadeMoves.DIVE), "a move not owned");
        helper.assertTrue(controller.allows(ArcadeMoves.WALK), "walking is always there");

        // a deny on a tag wins over the boots, whatever its priority
        ArcadeSources.add(player, new ArcadeSource(DENY_AERIAL, Optional.empty(), Map.of(MoveSelector.tag(ArcadeMoveTags.AERIAL), AccessMode.DENY), -10, Optional.empty()));
        client.tickServer();
        helper.assertFalse(controller.allows(ArcadeMoves.DOUBLE_JUMP), "a denied move, owned and enabled by the boots");
        helper.assertFalse(controller.isActive(), "with every owned move denied, the layer has nothing to do");
        ArcadeSources.remove(player, DENY_AERIAL);

        // plain leather boots do nothing
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        client.tickServer();
        helper.assertFalse(controller.isActive(), "boots without the component");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void bootsHeldInTheHandDoNothing(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        ArcadeUnlocks.unlock(player, MoveSelector.tag(ArcadeMoveTags.ALL));
        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ArcadeItems.MARIO_BOOTS));
        client.tickServer();
        helper.assertFalse(ArcadeController.of(player).isActive(), "the component only counts in the slots it names");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void aSourceRunsOutAtItsExpiry(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var id = Identifier.fromNamespaceAndPath("mubble-gametest", "timed");
        long now = helper.getLevel().getGameTime();
        ArcadeSources.add(player, new ArcadeSource(id, Optional.of(ArcadeProfileIds.TRIAL), Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), AccessMode.FORCE), 0, Optional.of(now + 5)));
        client.tickServer();
        helper.assertTrue(ArcadeController.of(player).isActive(), "a source before its expiry");
        helper.runAfterDelay(6, () -> {
            client.tickServer();
            helper.assertFalse(ArcadeController.of(player).isActive(), "a source past its expiry");
            helper.assertTrue(ArcadeSources.get(player).find(id).isEmpty(), "an expired source should be removed from the player");
            finish(helper, client);
        });
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void takingTheBootsOffMidRollEndsCleanly(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = ArcadeController.of(player);
        ArcadeUnlocks.unlock(player, MoveSelector.tag(ArcadeMoveTags.ALL));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ArcadeItems.MARIO_BOOTS));
        var frames = new ArcadeTestKit.Frames().forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.press(CROUCH));
        ArcadeTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(controller.state().move, ArcadeMoves.ROLL, "crouch and action while running");
        helper.assertValueEqual(player.getPose(), Pose.SWIMMING, "the pose of a roll");

        player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        client.tickServer();
        helper.assertFalse(controller.isActive(), "the layer without the boots");
        helper.assertValueEqual(player.getPose(), Pose.STANDING, "the pose once vanilla is back");
        helper.assertTrue(((ArcadePlayer) player).mubble$arcadeVisual().move() == null, "the move other players see should be cleared");
        helper.assertValueEqual(player.getAttributeBaseValue(ArcadeAttributes.ARCADE_RUN_SPEED), ArcadeAttributes.ARCADE_RUN_SPEED.value().getDefaultValue(), "base run speed once the layer is off");
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.ATTACK), "hands busy from the roll should not outlive it");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void waterIsSwumAndLavaSuspendsTheLayer(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var controller = ArcadeController.of(player);
        ArcadeTestKit.force(player, ArcadeProfileIds.TRIAL);
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "on dry land");
        ArcadeTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.WATER.defaultBlockState());
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "the layer swims in water");

        // drained first: lava poured over water would turn to stone
        ArcadeTestKit.fill(helper, new BlockPos(2, 1, 2), new BlockPos(8, 4, 8), Blocks.AIR.defaultBlockState());
        client.tickServer();
        ArcadeTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.LAVA.defaultBlockState());
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isActive() && controller.isSuspended(), "lava is vanilla's");
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.BLOCK), "a suspended layer refuses nothing");
        ArcadeTestKit.fill(helper, new BlockPos(3, 1, 3), new BlockPos(7, 3, 7), Blocks.AIR.defaultBlockState());
        player.clearFire();
        client.tickServer();
        client.tickServer();
        helper.assertTrue(controller.isDriving(), "out of the lava again");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void busyHandsAndTheInteractionPolicy(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        ArcadeTestKit.force(player, ArcadeProfileIds.TRIAL);
        var frames = new ArcadeTestKit.Frames();
        ArcadeTestKit.step(player, frames.next());
        // the trial is combat only
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.ATTACK), "attacking under combat_only");
        helper.assertTrue(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.BLOCK), "breaking and placing under combat_only");

        var target = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(5, 1, 7));
        var result = AttackEntityCallback.EVENT.invoker().interact(player, helper.getLevel(), InteractionHand.MAIN_HAND, target, null);
        helper.assertValueEqual(result, InteractionResult.PASS, "an attack while running");

        frames.forward().hold(SPRINT);
        for (int i = 0; i < 6; i++) {
            ArcadeTestKit.step(player, frames.next());
        }
        ArcadeTestKit.step(player, frames.press(CROUCH));
        ArcadeTestKit.step(player, frames.press(ACTION));
        helper.assertValueEqual(ArcadeController.of(player).state().move, ArcadeMoves.ROLL, "crouch and action while running");
        result = AttackEntityCallback.EVENT.invoker().interact(player, helper.getLevel(), InteractionHand.MAIN_HAND, target, null);
        helper.assertValueEqual(result, InteractionResult.FAIL, "an attack mid-roll, hands busy");

        // the overworld profile allows everything
        ArcadeTestKit.force(player, ArcadeProfileIds.OVERWORLD);
        var standing = new ArcadeTestKit.Frames();
        for (int i = 0; i < 20; i++) {
            ArcadeTestKit.step(player, standing.next());
        }
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.BLOCK), "breaking and placing under full");
        var controller = ArcadeController.of(player);
        helper.assertFalse(controller.handsGoToMoves(false), "attack and use while standing go to the hands");
        helper.assertTrue(controller.handsGoToMoves(true), "attack and use with crouch held roll");

        // with the orbit camera, the hands reach out where the body faces: never to a block
        var orbiting = new ArcadeTestKit.Frames().orbit();
        ArcadeTestKit.step(player, orbiting.next());
        helper.assertTrue(controller.orbiting(), "a step taken with the orbit camera");
        helper.assertTrue(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.BLOCK), "breaking and placing with the orbit camera, even under full");
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.ENTITY), "using an entity with the orbit camera");
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.USE_ITEM), "using an item with the orbit camera");
        helper.assertFalse(ArcadeInteraction.refused(player, ArcadeInteraction.Kind.ATTACK), "attacking with the orbit camera");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void profilesReloadWithoutRelogging(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        var live = ArcadeProfiles.serverValues();
        helper.assertTrue(live.containsKey(ArcadeProfileIds.TRIAL) && live.containsKey(ArcadeProfileIds.OVERWORLD),
                "the reload listener should have read the profile files, it has " + live.keySet());

        // a profile of this test alone, so that tests running alongside keep theirs
        var key = ResourceKey.create(ArcadeRegistries.ARCADE_PROFILE, Identifier.fromNamespaceAndPath("mubble-gametest", "live"));
        var trial = live.get(ArcadeProfileIds.TRIAL);
        try {
            putLive(key, withRunSpeed(trial, 0.55D));
            ArcadeSources.add(player, new ArcadeSource(ArcadeTestKit.SOURCE, Optional.of(key), Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), AccessMode.FORCE), 100, Optional.empty()));
            client.tickServer();
            helper.assertValueEqual(player.getAttributeBaseValue(ArcadeAttributes.ARCADE_RUN_SPEED), 0.55D, "base run speed from the live profile");

            // what a reload with an edited file does
            putLive(key, withRunSpeed(trial, 0.25D));
            client.tickServer();
            helper.assertValueEqual(player.getAttributeBaseValue(ArcadeAttributes.ARCADE_RUN_SPEED), 0.25D, "base run speed after the reload");
        } finally {
            var restored = new HashMap<>(ArcadeProfiles.serverValues());
            restored.remove(key);
            ArcadeProfiles.setServerValues(restored);
        }
        finish(helper, client);
    }

    private static void putLive(ResourceKey<ArcadeProfile> key, ArcadeProfile profile) {
        var values = new HashMap<>(ArcadeProfiles.serverValues());
        values.put(key, profile);
        ArcadeProfiles.setServerValues(values);
    }

    private static ArcadeProfile withRunSpeed(ArcadeProfile profile, double runSpeed) {
        var physics = profile.physics();
        var g = physics.ground();
        var ground = new ArcadePhysics.Ground(g.walkSpeed(), runSpeed, g.accelTicks(), g.decelTicks(), g.turnSpeed(), g.overCapDrag(), g.analogRunThreshold(), g.stickDeadzone(), g.minGrip());
        var newPhysics = new ArcadePhysics(ground, physics.air(), physics.gravity(), physics.slope(), physics.bounce(), physics.effects(), physics.safetyCeiling());
        return new ArcadeProfile(newPhysics, profile.grace(), profile.interaction(), profile.costs(), profile.fallDamage(), profile.camera(), profile.validation(), profile.moves());
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void unlockingByCommandAndByReward(GameTestHelper helper) {
        var client = player(helper);
        var player = client.server();
        client.serverCommand("mubble arcade unlock @self mubble:dive");
        helper.assertTrue(ArcadeUnlocks.get(player).owns(ArcadeMoves.DIVE), "a move unlocked by command");
        client.serverCommand("mubble arcade unlock @self #mubble:wall");
        helper.assertTrue(ArcadeUnlocks.get(player).owns(ArcadeMoves.WALL_JUMP) && ArcadeUnlocks.get(player).owns(ArcadeMoves.WALL_SLIDE), "a tag unlocked by command");
        client.serverCommand("mubble arcade lock @self mubble:dive");
        helper.assertFalse(ArcadeUnlocks.get(player).owns(ArcadeMoves.DIVE), "a move locked by command");

        new ArcadeMoveReward(List.of(MoveSelector.move(ArcadeMoveIds.GROUND_POUND))).grant(player);
        helper.assertTrue(ArcadeUnlocks.get(player).owns(ArcadeMoves.GROUND_POUND), "a move given by a reward");
        helper.assertTrue(ArcadeUnlocks.get(player).owns(ArcadeMoves.GROUND_POUND_LAND), "the parts of a move come with it");
        finish(helper, client);
    }

    @GameTest(structure = ArcadeTestKit.LANE)
    public void theOverworldChargesFoodAndCountsJumps(GameTestHelper helper) {
        ArcadeTestKit.floor(helper);
        var client = ArcadeTestClient.join(helper, new BlockPos(5, 1, 5), ArcadeProfileIds.OVERWORLD);
        var player = client.server();
        var food = (FoodDataAccessor) player.getFoodData();
        int jumps = player.getStats().getValue(Stats.CUSTOM.get(Stats.JUMP));
        var frames = new ArcadeTestKit.Frames();
        for (int i = 0; i < 4; i++) {
            client.tick(frames.next());
        }
        float before = food.mubbleGametest$getExhaustionLevel();
        client.tick(frames.press(JUMP));
        helper.assertValueEqual(client.controller().state().move, ArcadeMoves.JUMP, "a jump");
        helper.assertTrue(food.mubbleGametest$getExhaustionLevel() > before, "a jump under the overworld profile should cost food");
        helper.assertValueEqual(player.getStats().getValue(Stats.CUSTOM.get(Stats.JUMP)), jumps + 1, "jumps counted");

        // too hungry to sprint, too hungry for the moves of speed
        player.getFoodData().setFoodLevel(6);
        client.tickServer();
        var controller = ArcadeController.of(player);
        helper.assertFalse(controller.allows(ArcadeMoves.ROLL), "a roll on an empty stomach");
        helper.assertTrue(controller.allows(ArcadeMoves.JUMP), "a jump on an empty stomach");
        client.leave();
        helper.succeed();
    }
}

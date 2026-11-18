package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.super_mario.world.power_up.action.TailWhipPowerUpAction;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * The spin of the Tanooki forms: everything around the holder is hit at once, and thrown up.
 */
public class TailWhipActionGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final float DAMAGE = 4.0F;

    private static final TailWhipPowerUpAction ACTION = new TailWhipPowerUpAction(
            TailWhipPowerUpAction.DEFAULT_RADIUS,
            TailWhipPowerUpAction.DEFAULT_VERTICAL_REACH,
            Optional.of(DAMAGE),
            TailWhipPowerUpAction.DEFAULT_UPWARD_SPEED,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            TailWhipPowerUpAction.DEFAULT_CHARGES
    );

    @GameTest
    public void aSpinHitsEveryEntityAroundAtOnceAndThrowsThemUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var east = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.east(2));
        var west = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.west(2));
        var far = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.north(6));

        helper.assertTrue(ACTION.trigger(player) == InteractionResult.SUCCESS, "the spin did not report a success");

        for (LivingEntity hit : new LivingEntity[]{east, west}) {
            helper.assertTrue(hit.getHealth() == hit.getMaxHealth() - DAMAGE, "an entity in reach should take the damage of the spin, health " + hit.getHealth());
            helper.assertTrue(hit.getDeltaMovement().y() >= TailWhipPowerUpAction.DEFAULT_UPWARD_SPEED - 1.0E-4D, "an entity hit should be thrown up");
            helper.assertTrue(Math.abs(hit.getDeltaMovement().x()) < 1.0E-4D, "straight up, not sideways");
        }
        helper.assertTrue(far.getHealth() == far.getMaxHealth(), "an entity out of reach should be left alone");
        helper.succeed();
    }

    @GameTest
    public void withoutADamageOfItsOwnItHitsLikeTheHolder(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.east(2));
        var action = new TailWhipPowerUpAction(3.0D, 1.5D, Optional.empty(), 0.6D, Optional.empty(), Optional.empty(), Optional.empty(), PowerUpCharges.none());

        action.trigger(player);

        helper.assertTrue(cow.getHealth() < cow.getMaxHealth(), "the spin should have hurt");
        helper.succeed();
    }

    @GameTest
    public void theHolderAndTheirPetsAreSpared(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var wolf = helper.spawnWithNoFreeWill(EntityTypes.WOLF, STAND.east(2));
        wolf.tame(player);

        ACTION.trigger(player);

        helper.assertTrue(wolf.getHealth() == wolf.getMaxHealth(), "a pet of the holder should be spared");
        helper.assertTrue(player.getHealth() == player.getMaxHealth(), "the holder should be spared");
        helper.succeed();
    }

    @GameTest
    public void goombasAreHitToo(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, STAND.east(2));

        ACTION.trigger(player);

        helper.assertTrue(goomba.isDeadOrDying() || goomba.getDeltaMovement().y() > 0.0D, "a goomba in reach should be hit");
        helper.succeed();
    }

    @GameTest
    public void aProjectileInTheSpinIsSentAwayNotUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        Arrow arrow = helper.spawn(EntityTypes.ARROW, STAND.east(2).above());
        arrow.setNoGravity(true);
        arrow.setDeltaMovement(-0.5D, 0.0D, 0.0D);

        ACTION.trigger(player);

        Vec3 movement = arrow.getDeltaMovement();
        helper.assertTrue(movement.x() > 0.4D, "the arrow should be sent away from the holder, moving " + movement);
        helper.assertTrue(Math.abs(movement.y()) < 1.0E-4D, "the arrow should not be sent up, moving " + movement);
        helper.assertTrue(arrow.getOwner() == player, "a deflected arrow belongs to whoever deflected it");
        helper.succeed();
    }

    @GameTest
    public void aSpinBumpsQuestionBlocksAndBreaksBricksAtOnce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        helper.setBlock(STAND.east(), SuperMarioBlocks.BRICK_BLOCK);
        helper.setBlock(STAND.west(), SuperMarioBlocks.QUESTION_BLOCK);
        helper.setBlock(STAND.north(), Blocks.STONE);

        ACTION.trigger(player);

        helper.assertBlockPresent(Blocks.AIR, STAND.east());
        var question = helper.getLevel().getBlockEntity(helper.absolutePos(STAND.west()), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK);
        helper.assertTrue(question.isPresent() && question.get().isBumping(), "the question block should be bumped");
        helper.assertBlockPresent(Blocks.STONE, STAND.north());
        helper.succeed();
    }

    @GameTest
    public void theSpinWaitsForItsCooldown(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setPowerUp(helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP)
                .getOrThrow(SuperMarioPowerUpIds.TANOOKI));
        var powerUp = player.getPowerUp().orElseThrow().value();

        helper.assertTrue(powerUp.canBeTriggered(player), "a fresh spin should be ready");
        powerUp.trigger(player);
        helper.assertFalse(powerUp.canBeTriggered(player), "a spin right after another should wait");

        for (int tick = 0; tick < 12; tick++) {
            TestPlayers.tick(player);
        }
        helper.assertTrue(powerUp.canBeTriggered(player), "the spin should be ready again after its cooldown");
        helper.succeed();
    }
}

package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.Flower;
import fr.hugman.mubble.super_mario.world.power_up.action.GrowFlowerPowerUpAction;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.power_up.PowerUpCharges;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * The action behind the Super Flower Pot. Where the flower is planted is the whole of it: the form is not
 * aimed like the ball throwers, so the direction the holder is looking only decides which side of them the
 * flower comes out of, and never where it goes afterwards.
 */
public class GrowFlowerActionGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double EPSILON = 1.0E-4D;

    /** The same action the flower power-up is built with, see {@code SuperMarioPowerUpProvider}. */
    private static final GrowFlowerPowerUpAction ACTION = new GrowFlowerPowerUpAction(
            SuperMarioEntityTypes.FLOWER,
            Flower.DEFAULT_SPEED,
            Flower.DEFAULT_LIFETIME,
            Flower.DEFAULT_MAX_CLIMB,
            false,
            PowerUpCharges.cooldownRecharge(1, 10)
    );

    @GameTest
    public void triggeringGrowsAFlower(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);

        var result = ACTION.trigger(player);

        helper.assertTrue(result == InteractionResult.SUCCESS, "the trigger did not report a success");
        helper.assertValueEqual(flowersAround(helper, player).size(), 1, "flowers grown by one trigger");
        helper.succeed();
    }

    @GameTest
    public void theHolderOwnsWhatTheyGrow(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);

        ACTION.trigger(player);

        helper.assertTrue(flowerOf(helper, player).getOwner() == player, "the flower is not owned by whoever grew it");
        helper.succeed();
    }

    @GameTest
    public void theFlowerCarriesTheNumbersOfTheAction(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        var action = new GrowFlowerPowerUpAction(SuperMarioEntityTypes.FLOWER, 0.25D, 17, 5.0D, true, PowerUpCharges.none());

        action.trigger(player);

        var flower = flowerOf(helper, player);
        helper.assertValueEqual(flower.getSpeed(), 0.25D, "the speed the action asked for");
        helper.assertValueEqual(flower.getLifetime(), 17, "the lifetime the action asked for");
        helper.assertValueEqual(flower.getMaxClimb(), 5.0D, "the height limit the action asked for");
        helper.assertTrue(flower.isStoppedByBlocks(), "the action asked for a flower stopped by blocks");
        helper.succeed();
    }

    /** The flower is 2×2 blocks: planted on the spot, it would grow right through its holder. */
    @GameTest
    public void theFlowerIsPlantedInFrontOfItsHolder(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setYRot(Direction.SOUTH.toYRot());

        ACTION.trigger(player);

        var flower = flowerOf(helper, player);
        helper.assertTrue(flower.getZ() > player.getZ() + 1.0D,
                "a flower planted by a player facing south should be south of them, was at " + flower.getZ() + " against " + player.getZ());
        helper.assertValueEqual(flower.getX(), player.getX(), "the x a flower was planted at");
        helper.succeed();
    }

    @GameTest
    public void theFlowerFollowsWhereTheHolderIsFacing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setYRot(Direction.WEST.toYRot());

        ACTION.trigger(player);

        var flower = flowerOf(helper, player);
        helper.assertTrue(flower.getX() < player.getX() - 1.0D,
                "a flower planted by a player facing west should be west of them, was at " + flower.getX() + " against " + player.getX());
        helper.succeed();
    }

    /** However the holder is looking, the flower is planted at their feet and goes straight up from there. */
    @GameTest
    public void lookingUpOrDownChangesNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);
        player.setXRot(-90.0F);

        ACTION.trigger(player);

        var flower = flowerOf(helper, player);
        double x = flower.getX();
        double z = flower.getZ();
        helper.assertValueEqual(flower.getY(), player.getY(), "a flower should be planted at the feet of its holder");

        // The heading only shows up once the flower has ticked: it is planted with no movement at all.
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> {
                    helper.assertTrue(flower.getDeltaMovement().horizontalDistance() < EPSILON, "a flower should never be aimed sideways");
                    helper.assertValueEqual(flower.getX(), x, "the x a flower drifted to");
                    helper.assertValueEqual(flower.getZ(), z, "the z a flower drifted to");
                })
                .thenSucceed();
    }

    @GameTest
    public void everyTriggerCostsACharge(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, STAND);

        helper.assertTrue(ACTION.canBeTriggered(player), "a fresh power-up should be triggerable");
        ACTION.trigger(player);

        helper.assertValueEqual(player.getPowerUpProperties().getChargeCount(), 0, "charges after one flower");
        helper.assertFalse(ACTION.canBeTriggered(player), "a spent power-up should not report itself as triggerable");
        helper.succeed();
    }

    private static List<Flower> flowersAround(GameTestHelper helper, ServerPlayer player) {
        return helper.getLevel().getEntitiesOfClass(Flower.class, around(player));
    }

    private static Flower flowerOf(GameTestHelper helper, ServerPlayer player) {
        return flowersAround(helper, player).stream().findFirst()
                .orElseThrow(() -> new AssertionError("no flower was grown at all"));
    }

    private static AABB around(ServerPlayer player) {
        return player.getBoundingBox().inflate(8.0D);
    }
}

package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import fr.hugman.mubble.world.power_up.PowerUp;

/**
 * The pound of {@code pounder.json} and the statue of {@code statue.json}, both started by a fresh press of
 * sneak in the air: a hang of two ticks, then a drop at 0.8 blocks per tick. The statue lasts 30 ticks at most.
 */
public class GroundPoundGameTest {
    private static final BlockPos STAND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);
    private static final double DROP_SPEED = 0.8D;
    private static final double EPSILON = 1.0E-4D;

    private static final Input SNEAK = TestPlayers.holdingSneak();
    private static final Input NOTHING = Input.EMPTY;

    @GameTest
    public void aFreshSneakInTheAirHangsThenDrops(GameTestHelper helper) {
        var player = airborne(helper, PowerUpFixtures.POUNDER, 5);

        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(player.isGroundPounding(), "sneaking in the air should start the pound");
        double hanging = player.getY();
        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(Math.abs(player.getY() - hanging) < EPSILON, "the pound should hang in place first");
        tick(player, SNEAK, 2);
        double before = player.getY();
        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(Math.abs(before - player.getY() - DROP_SPEED) < EPSILON, "the pound should drop at its speed, dropped " + (before - player.getY()));
        helper.assertFalse(player.isStatue(), "a pound with no statue should not turn to stone");
        helper.succeed();
    }

    @GameTest
    public void aFormWithoutAPoundCannotPoundOrTurnToStone(GameTestHelper helper) {
        var player = airborne(helper, PowerUpFixtures.FLOATER, 5);

        tick(player, SNEAK, 3);

        helper.assertFalse(player.isGroundPounding(), "a form without the ability should not pound");
        helper.assertFalse(player.isStatue(), "a form without the ability should not turn to stone");
        helper.succeed();
    }

    @GameTest
    public void aSneakHeldWhenLeavingTheGroundDoesNotPound(GameTestHelper helper) {
        var player = player(helper, PowerUpFixtures.STATUE, STAND);
        tick(player, SNEAK, 2);
        player.teleportTo(player.getX(), player.getY() + 4.0D, player.getZ());

        tick(player, SNEAK, 3);

        helper.assertFalse(player.onGround(), "the player is not in the air, the test proves nothing");
        helper.assertFalse(player.isGroundPounding(), "a sneak held since the ground should not pound");
        helper.assertFalse(player.isStatue(), "a sneak held since the ground should not turn to stone");
        helper.succeed();
    }

    @GameTest
    public void sneakingOnTheGroundIsPlainSneaking(GameTestHelper helper) {
        var player = player(helper, PowerUpFixtures.STATUE, STAND);

        TestPlayers.tick(player, NOTHING);
        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isStatue(), "sneaking on the ground should not turn to stone");
        helper.succeed();
    }

    @GameTest
    public void theStatueIsInvulnerableExceptToWhatBypassesInvulnerability(GameTestHelper helper) {
        var player = statue(helper);
        var level = helper.getLevel();

        helper.assertFalse(player.hurtServer(level, level.damageSources().generic(), 5.0F), "a statue should not be hurt");
        helper.assertTrue(player.getHealth() == player.getMaxHealth(), "a statue should keep its health");
        helper.assertTrue(player.isInvulnerableTo(level, level.damageSources().generic()), "a statue should be invulnerable");
        helper.assertFalse(player.isInvulnerableTo(level, level.damageSources().genericKill()), "what bypasses invulnerability still hurts a statue");
        helper.succeed();
    }

    @GameTest
    public void aPlayerWhoIsNotAStatueIsHurtAsUsual(GameTestHelper helper) {
        var player = player(helper, PowerUpFixtures.POUNDER, STAND.above(3));
        var level = helper.getLevel();

        helper.assertFalse(player.isInvulnerableTo(level, level.damageSources().generic()), "only a statue is invulnerable");
        helper.succeed();
    }

    @GameTest
    public void onlyTheStatueLeafPoundsAndItDefeatsTheEnemiesOfTheModOutright(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, STAND.east());
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.west());
        var statueLeaf = PowerUpFixtures.get(helper, SuperMarioPowerUpIds.STATUE_TANOOKI).value().abilities().groundPound();
        var superLeaf = PowerUpFixtures.get(helper, SuperMarioPowerUpIds.TANOOKI).value().abilities().groundPound();

        helper.assertTrue(superLeaf.isEmpty(), "the Super Leaf should not pound");
        helper.assertTrue(statueLeaf.flatMap(pound -> pound.statue()).isPresent(), "the Statue Leaf should turn to stone");
        helper.assertTrue(statueLeaf.orElseThrow().defeatsOutright(goomba), "the Statue Leaf should defeat a goomba outright");
        helper.assertFalse(statueLeaf.orElseThrow().defeatsOutright(cow), "the Statue Leaf should only defeat the enemies of the mod outright");
        helper.succeed();
    }

    @GameTest
    public void theImpactDefeatsAGoombaOutrightAndHurtsOtherMobsHard(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, STAND.east());
        var golem = helper.spawnWithNoFreeWill(EntityTypes.IRON_GOLEM, STAND.west());
        var far = helper.spawnWithNoFreeWill(EntityTypes.COW, STAND.north(5));
        var player = pounding(helper, PowerUpFixtures.POUNDER, 5);

        tick(player, SNEAK, 12);

        helper.assertTrue(player.onGround(), "the pound never landed, the test proves nothing");
        helper.assertTrue(goomba.isDeadOrDying(), "the impact should defeat a goomba in one");
        helper.assertTrue(golem.getHealth() <= golem.getMaxHealth() - 6.0F, "the impact should hurt other mobs hard, health " + golem.getHealth());
        helper.assertTrue(far.getHealth() == far.getMaxHealth(), "the impact should not reach far");
        helper.succeed();
    }

    @GameTest
    public void thePoundSparesThePetsOfThePounder(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var wolf = helper.spawnWithNoFreeWill(EntityTypes.WOLF, STAND.east());
        var player = pounding(helper, PowerUpFixtures.POUNDER, 5);
        wolf.tame(player);

        tick(player, SNEAK, 12);

        helper.assertTrue(wolf.getHealth() == wolf.getMaxHealth(), "a pet should be spared");
        helper.assertTrue(player.getHealth() == player.getMaxHealth(), "the pounder takes no fall damage");
        helper.succeed();
    }

    @GameTest
    public void thePoundBreaksBricksAndBumpsWhatDoesNotBreak(GameTestHelper helper) {
        var player = pounding(helper, PowerUpFixtures.POUNDER, 5);
        helper.setBlock(STAND, SuperMarioBlocks.BRICK_BLOCK);
        helper.setBlock(STAND.below().east(), SuperMarioBlocks.BRICK_BLOCK);
        helper.setBlock(STAND.below().west(), SuperMarioBlocks.QUESTION_BLOCK);

        tick(player, SNEAK, 12);

        helper.assertBlockPresent(Blocks.AIR, STAND);
        helper.assertBlockPresent(Blocks.AIR, STAND.below().east());
        var question = helper.getLevel().getBlockEntity(helper.absolutePos(STAND.below().west()), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK);
        helper.assertTrue(question.isPresent() && question.get().isBumping(), "the block beside should be bumped");
        helper.succeed();
    }

    @GameTest
    public void theStatueLastsAsLongAsSneakDoes(GameTestHelper helper) {
        var player = statue(helper);
        tick(player, SNEAK, 12);
        helper.assertTrue(player.onGround(), "the pound never landed, the test proves nothing");
        helper.assertTrue(player.isStatue(), "the statue should outlast the pound while sneak is held");

        TestPlayers.tick(player, NOTHING);

        helper.assertFalse(player.isStatue(), "letting go of sneak should end the statue");
        helper.succeed();
    }

    @GameTest
    public void theStatueEndsAtItsMaximumDuration(GameTestHelper helper) {
        var player = statue(helper);

        tick(player, SNEAK, 25);
        helper.assertTrue(player.isStatue(), "the statue ended early");
        tick(player, SNEAK, 15);

        helper.assertFalse(player.isStatue(), "the statue should end by itself at its maximum duration");
        helper.succeed();
    }

    @GameTest
    public void losingTheFormEndsTheStatueAndTheInvulnerability(GameTestHelper helper) {
        var player = statue(helper);

        player.clearPowerUp();
        TestPlayers.tick(player, SNEAK);

        helper.assertFalse(player.isStatue(), "losing the form should end the statue");
        helper.assertFalse(player.isGroundPounding(), "losing the form should end the pound");
        var level = helper.getLevel();
        helper.assertFalse(player.isInvulnerableTo(level, level.damageSources().generic()), "losing the form should end the invulnerability");
        helper.succeed();
    }

    @GameTest
    public void aStatueDoesNotMoveSideways(GameTestHelper helper) {
        var player = statue(helper);
        tick(player, SNEAK, 12);
        double x = player.getX();
        double z = player.getZ();

        tick(player, TestPlayers.holdingSneakAndForward(), 5);

        helper.assertTrue(Math.abs(player.getX() - x) < EPSILON && Math.abs(player.getZ() - z) < EPSILON, "a statue should stay where it stands");
        helper.succeed();
    }

    @GameTest
    public void theStatueAndPoundStatesAreSaved(GameTestHelper helper) {
        var player = statue(helper);
        var registries = helper.getLevel().registryAccess();
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        player.saveWithoutId(output);

        Player reloaded = TestPlayers.mock(helper);
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, registries, output.buildResult()));

        helper.assertTrue(reloaded.isStatue(), "a statue should carry on through a save");
        helper.assertTrue(reloaded.isGroundPounding(), "a pound should carry on through a save");
        helper.succeed();
    }

    @GameTest
    public void theFlagsReachTheOtherPlayers(GameTestHelper helper) {
        var player = statue(helper);

        var watched = TestPlayers.mock(helper);
        watched.getEntityData().assignValues(player.getEntityData().getNonDefaultValues());

        helper.assertTrue(watched.isStatue(), "the statue flag should reach the other players");
        helper.assertTrue(watched.isGroundPounding(), "the pound flag should reach the other players");
        helper.succeed();
    }

    private static ServerPlayer player(GameTestHelper helper, ResourceKey<PowerUp> powerUp, BlockPos pos) {
        Arena.buildFloor(helper);
        var player = TestPlayers.survival(TestPlayers.at(helper, pos));
        player.setGameMode(GameType.SURVIVAL);
        new ServerboundPlayerLoadedPacket().handle(player.connection);
        player.setPowerUp(PowerUpFixtures.get(helper, powerUp));
        TestPlayers.tick(player, NOTHING);
        return player;
    }

    private static ServerPlayer airborne(GameTestHelper helper, ResourceKey<PowerUp> powerUp, int height) {
        var player = player(helper, powerUp, STAND.above(height));
        helper.assertFalse(player.onGround(), "the player is not in the air, the test proves nothing");
        return player;
    }

    /** Already pounding, after its first tick of sneak. */
    private static ServerPlayer pounding(GameTestHelper helper, ResourceKey<PowerUp> powerUp, int height) {
        var player = airborne(helper, powerUp, height);
        TestPlayers.tick(player, SNEAK);
        helper.assertTrue(player.isGroundPounding(), "the pound never started, the test proves nothing");
        return player;
    }

    private static ServerPlayer statue(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = pounding(helper, PowerUpFixtures.STATUE, 5);
        helper.assertTrue(player.isStatue(), "the statue never started, the test proves nothing");
        return player;
    }

    private static void tick(ServerPlayer player, Input keys, int ticks) {
        for (int tick = 0; tick < ticks; tick++) {
            TestPlayers.tick(player, keys);
        }
    }
}

package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.projectile.Boomerang;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.super_mario.world.power_up.action.ThrowBoomerangPowerUpAction;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.EasingType;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Every test flies a short boomerang along {@code +Z}, so that it stays inside its own arena.
 */
public class BoomerangGameTest {
    private static final BlockPos OWNER_POS = new BlockPos(4, Arena.FLOOR_Y + 1, 1);
    private static final Vec3 START = new Vec3(4.5D, Arena.FLOOR_Y + 1.15D, 1.5D);
    private static final BlockPos IN_PATH = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    private static final Boomerang.Flight SHORT = new Boomerang.Flight(4.0F, 8, EasingType.OUT_QUAD, 2, 0.6F, 0.15F, 0.35F, 0.25F, 2.0F, 200, 1.0F, 0.0F, 0.5F, 0.6F, 8, 20);
    private static final ThrowBoomerangPowerUpAction SHORT_THROW = new ThrowBoomerangPowerUpAction(SHORT, Boomerang.Look.DEFAULT, 1,
            ThrowBoomerangPowerUpAction.DEFAULT.throwSound(), ThrowBoomerangPowerUpAction.DEFAULT.readySound(), ThrowBoomerangPowerUpAction.DEFAULT.readyParticle());

    @GameTest(maxTicks = 100)
    public void fliesOutAndComesBackToBeCaught(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, OWNER_POS);
        var boomerang = launch(helper, player, SHORT);
        double startZ = boomerang.getZ();
        var furthest = new double[]{startZ};
        var caught = new AtomicBoolean();

        helper.onEachTick(() -> {
            furthest[0] = Math.max(furthest[0], boomerang.getZ());
            caught.compareAndSet(false, boomerang.getPhase() == Boomerang.CAUGHT);
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out");
            helper.assertTrue(furthest[0] - startZ > SHORT.range() - 0.5D, "the boomerang turned around before reaching its range, at " + (furthest[0] - startZ));
            helper.assertTrue(caught.get(), "the boomerang went away without being caught");
        });
    }

    @GameTest(maxTicks = 100)
    public void followsAnOwnerWhoMoved(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, OWNER_POS);
        var boomerang = launch(helper, player, SHORT);
        var caught = new AtomicBoolean();
        helper.onEachTick(() -> caught.compareAndSet(false, boomerang.getPhase() == Boomerang.CAUGHT));

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(boomerang.getPhase() == Boomerang.HANG, "the boomerang has not turned around yet"))
                .thenExecute(() -> {
                    var moved = helper.absoluteVec(new Vec3(1.5D, Arena.FLOOR_Y + 1, 2.5D));
                    player.teleportTo(moved.x, moved.y, moved.z);
                })
                .thenWaitUntil(() -> helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out"))
                .thenExecute(() -> helper.assertTrue(caught.get(), "the boomerang lost track of its owner"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 200)
    public void missedPassesShrinkUntilItVanishes(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = launch(helper, null, SHORT);
        var lastRange = new float[]{boomerang.getRange()};

        helper.onEachTick(() -> {
            if (!boomerang.isRemoved()) {
                helper.assertTrue(boomerang.getRange() <= lastRange[0], "a pass got longer");
                lastRange[0] = boomerang.getRange();
            }
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out");
            helper.assertValueEqual(boomerang.getPass(), 2, "passes flown before vanishing");
        });
    }

    @GameTest(maxTicks = 100)
    public void reboundsOnWalls(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(new BlockPos(4, Arena.FLOOR_Y + 1, 4), Blocks.STONE);
        var boomerang = launch(helper, null, SHORT);
        double wallFace = helper.absoluteVec(new Vec3(0, 0, 4)).z;

        helper.failIfEver(() -> helper.assertTrue(boomerang.isRemoved() || boomerang.getZ() < wallFace, "the boomerang went through the wall"));
        helper.succeedWhen(() -> helper.assertTrue(boomerang.getDeltaMovement().z < 0.0D, "the boomerang has not bounced back"));
    }

    @GameTest(maxTicks = 60)
    public void bumpsTheBumpableBlocksItHits(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var pos = new BlockPos(4, Arena.FLOOR_Y + 1, 4);
        helper.setBlock(pos, SuperMarioBlocks.QUESTION_BLOCK);
        var block = helper.getLevel().getBlockEntity(helper.absolutePos(pos), SuperMarioBlockEntityTypes.BUMPABLE_BLOCK).orElseThrow();
        launch(helper, null, SHORT);

        helper.succeedWhen(() -> helper.assertTrue(block.isBumping(), "the block was not bumped"));
    }

    @GameTest(maxTicks = 100)
    public void hitsAnEntityOncePerPass(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, IN_PATH);
        var boomerang = launch(helper, null, SHORT);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(boomerang.getPass() > 0 || boomerang.isRemoved(), "the first pass is not over"))
                .thenExecute(() -> helper.assertValueEqual(pig.getMaxHealth() - pig.getHealth(), SHORT.damage(), "damage taken over the first pass"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void destroysTheProjectilesItCrosses(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var snowball = helper.spawn(EntityTypes.SNOWBALL, new Vec3(4.5D, START.y, 4.5D));
        snowball.setNoGravity(true);
        snowball.setDeltaMovement(0.0D, 0.0D, -0.05D);
        var boomerang = launch(helper, null, SHORT);

        helper.succeedWhen(() -> {
            helper.assertTrue(snowball.isRemoved(), "the snowball is still there");
            helper.assertFalse(boomerang.isRemoved(), "destroying the snowball put the boomerang out");
        });
    }

    @GameTest(maxTicks = 100)
    public void leavesProjectilesAtRestAlone(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var snowball = helper.spawn(EntityTypes.SNOWBALL, new Vec3(4.5D, START.y, 3.5D));
        snowball.setNoGravity(true);
        var boomerang = launch(helper, null, SHORT);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(boomerang.getPass() > 0 || boomerang.isRemoved(), "the first pass is not over"))
                .thenExecute(() -> helper.assertFalse(snowball.isRemoved(), "a projectile at rest was destroyed"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 100)
    public void sparesItsOwnerAndTheirPets(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, OWNER_POS);
        Wolf wolf = helper.spawnWithNoFreeWill(EntityTypes.WOLF, IN_PATH);
        wolf.tame(player);
        float wolfHealth = wolf.getHealth();
        float playerHealth = player.getHealth();
        var boomerang = launch(helper, player, SHORT);

        helper.succeedWhen(() -> {
            helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out");
            helper.assertValueEqual(wolf.getHealth(), wolfHealth, "the health of the pet");
            helper.assertValueEqual(player.getHealth(), playerHealth, "the health of the owner");
        });
    }

    @GameTest(maxTicks = 100)
    public void bringsBackWhatItPickedUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, OWNER_POS);
        var ingot = helper.spawnItem(Items.GOLD_INGOT, new Vec3(4.5D, Arena.FLOOR_Y + 1, 3.5D));
        var coin = helper.spawn(MubbleEntityTypes.COLLECTIBLE, new Vec3(4.5D, START.y, 4.5D));
        coin.setItem(new ItemStack(SuperMarioItems.COIN));
        coin.setFixed(true);
        var orb = new ExperienceOrb(helper.getLevel(), helper.absoluteVec(new Vec3(4.5D, Arena.FLOOR_Y + 1, 5.0D)), Vec3.ZERO, 5);
        helper.getLevel().addFreshEntity(orb);
        var boomerang = launch(helper, player, SHORT);
        var rode = new HashSet<Entity>();
        var spots = Map.of(ingot, ingot.position(), orb, orb.position());

        // pinned until grabbed: an orb would otherwise fly to the player by itself
        helper.onEachTick(() -> {
            boomerang.getPassengers().forEach(rode::add);
            spots.forEach((entity, spot) -> {
                if (!rode.contains(entity) && !entity.isRemoved()) {
                    entity.setPos(spot);
                    entity.setDeltaMovement(Vec3.ZERO);
                }
            });
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out");
            helper.assertTrue(rode.containsAll(List.of(ingot, coin, orb)), "the boomerang did not carry everything it touched");
            helper.assertTrue(player.getInventory().contains(new ItemStack(Items.GOLD_INGOT)), "the ingot never reached the owner");
            helper.assertTrue(player.getInventory().contains(new ItemStack(SuperMarioItems.COIN)), "the coin never reached the owner");
            helper.assertTrue(player.totalExperience > 0, "the experience never reached the owner");
        });
    }

    @GameTest(maxTicks = 100)
    public void dropsWhatDoesNotFitAtTheFeetOfItsOwner(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = TestPlayers.at(helper, OWNER_POS);
        // a full inventory takes anything in creative
        player.setGameMode(GameType.SURVIVAL);
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            player.getInventory().setItem(slot, new ItemStack(Items.DIRT, 64));
        }
        var ingot = helper.spawnItem(Items.GOLD_INGOT, new Vec3(4.5D, Arena.FLOOR_Y + 1, 3.5D));
        var boomerang = launch(helper, player, SHORT);

        helper.succeedWhen(() -> {
            helper.assertTrue(boomerang.isRemoved(), "the boomerang is still out");
            helper.assertFalse(ingot.isRemoved(), "the ingot was lost");
            helper.assertFalse(ingot.isPassenger(), "the ingot is still riding");
            helper.assertTrue(ingot.distanceTo(player) < 1.0D, "the ingot was not dropped at the feet of the owner");
        });
    }

    @GameTest
    public void dropsWhatItCarriesWhenItVanishes(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = hovering(helper);
        var ingot = carried(helper, boomerang);

        boomerang.vanish();

        helper.assertTrue(boomerang.isRemoved(), "the boomerang did not vanish");
        helper.assertFalse(ingot.isPassenger(), "the ingot is still riding");
        helper.assertTrue(ingot.hasPickUpDelay(), "the ingot can be grabbed the moment it drops");
        helper.succeed();
    }

    @GameTest
    public void releasesWhatItCarriesWhenKilled(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = hovering(helper);
        var ingot = carried(helper, boomerang);

        boomerang.kill(helper.getLevel());

        helper.assertFalse(ingot.isRemoved(), "the ingot went away with the boomerang");
        helper.assertFalse(ingot.isPassenger(), "the ingot is still riding");
        helper.succeed();
    }

    @GameTest
    public void whatItCarriesSurvivesASaveAndReload(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = hovering(helper);
        carried(helper, boomerang);
        var level = helper.getLevel();

        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
        boomerang.saveWithoutId(output);
        var input = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), output.buildResult());
        var reloaded = new AtomicReference<Entity>();
        EntityType.loadEntityRecursive(SuperMarioEntityTypes.BOOMERANG, input, level, EntitySpawnReason.LOAD, entity -> {
            reloaded.compareAndSet(null, entity);
            return entity;
        });

        helper.assertTrue(reloaded.get() instanceof Boomerang, "the boomerang did not come back");
        var passengers = reloaded.get().getPassengers();
        helper.assertValueEqual(passengers.size(), 1, "carried entities after reloading");
        helper.assertTrue(passengers.getFirst() instanceof ItemEntity item && item.getItem().is(Items.GOLD_INGOT), "the ingot did not come back");
        helper.succeed();
    }

    @GameTest
    public void nobodyElseCanGrabWhatItCarries(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = hovering(helper);
        var ingot = carried(helper, boomerang);
        var other = TestPlayers.inLevel(helper);
        other.teleportTo(ingot.getX(), ingot.getY() - 0.5D, ingot.getZ());

        TestPlayers.tick(other);

        helper.assertTrue(ingot.isPassenger(), "the ingot was taken off the boomerang");
        helper.assertFalse(other.getInventory().contains(new ItemStack(Items.GOLD_INGOT)), "someone else grabbed the ingot");
        helper.assertTrue(ingot.hasPickUpDelay(), "mobs could grab the ingot");
        helper.succeed();
    }

    @GameTest
    public void onlyOneBoomerangAtATime(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = boomerangThrower(helper);

        helper.assertTrue(SHORT_THROW.trigger(player) == InteractionResult.SUCCESS, "the first throw failed");
        helper.assertFalse(SHORT_THROW.canBeTriggered(player), "a second boomerang could be thrown while the first is out");
        helper.succeed();
    }

    @GameTest
    public void asManyBoomerangsAsTheActionAllows(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = boomerangThrower(helper);
        var twoAtOnce = new ThrowBoomerangPowerUpAction(SHORT, Boomerang.Look.DEFAULT, 2,
                ThrowBoomerangPowerUpAction.DEFAULT.throwSound(), ThrowBoomerangPowerUpAction.DEFAULT.readySound(), ThrowBoomerangPowerUpAction.DEFAULT.readyParticle());
        player.setPowerUpProperties(twoAtOnce.setUpProperties());

        helper.assertTrue(twoAtOnce.trigger(player) == InteractionResult.SUCCESS, "the first throw failed");
        helper.assertTrue(twoAtOnce.canBeTriggered(player), "the second boomerang was held back");
        helper.assertTrue(twoAtOnce.trigger(player) == InteractionResult.SUCCESS, "the second throw failed");
        helper.assertFalse(twoAtOnce.canBeTriggered(player), "a third boomerang could be thrown");
        helper.succeed();
    }

    @GameTest(maxTicks = 100)
    public void aMissedBoomerangCoolsDownBeforeTheNextThrow(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = boomerangThrower(helper);
        SHORT_THROW.trigger(player);
        var boomerang = helper.getLevel().getEntitiesOfClass(Boomerang.class, player.getBoundingBox().inflate(4.0D)).getFirst();

        boomerang.vanish();
        TestPlayers.tick(player);
        helper.assertFalse(SHORT_THROW.canBeTriggered(player), "the throw came back without a cooldown");
        helper.assertTrue(player.getPowerUpProperties().isCoolingDown(), "no cooldown was started");

        helper.succeedWhen(() -> {
            TestPlayers.tick(player);
            helper.assertTrue(SHORT_THROW.canBeTriggered(player), "the cooldown never ran out");
        });
    }

    @GameTest(maxTicks = 100)
    public void aCaughtBoomerangCanBeThrownAgainAtOnce(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var player = boomerangThrower(helper);
        SHORT_THROW.trigger(player);
        var boomerang = helper.getLevel().getEntitiesOfClass(Boomerang.class, player.getBoundingBox().inflate(4.0D)).getFirst();

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(boomerang.getPhase() == Boomerang.CAUGHT, "the boomerang was not caught"))
                .thenExecute(() -> {
                    TestPlayers.tick(player);
                    helper.assertTrue(SHORT_THROW.canBeTriggered(player), "the catch did not free the throw");
                    helper.assertFalse(player.getPowerUpProperties().isCoolingDown(), "a catch started the miss cooldown");
                })
                .thenSucceed();
    }

    @GameTest
    public void aSummonedBoomerangFliesOnItsDefaults(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var boomerang = helper.spawn(SuperMarioEntityTypes.BOOMERANG, START);

        helper.assertValueEqual(boomerang.getFlight(), Boomerang.Flight.DEFAULT, "flight of a summoned boomerang");
        helper.assertTrue(boomerang.getItem().is(SuperMarioItems.BOOMERANG), "a summoned boomerang shows the wrong item");
        helper.succeed();
    }

    private static ServerPlayer boomerangThrower(GameTestHelper helper) {
        var player = TestPlayers.at(helper, OWNER_POS);
        player.setPowerUp(PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.BOOMERANG));
        return player;
    }

    private static Boomerang launch(GameTestHelper helper, @Nullable Entity owner, Boomerang.Flight flight) {
        var boomerang = new Boomerang(helper.getLevel(), flight, Boomerang.Look.DEFAULT);
        boomerang.setPos(helper.absoluteVec(START));
        boomerang.setDeltaMovement(0.0D, 0.0D, 1.0D);
        if (owner != null) {
            boomerang.setOwner(owner);
        }
        helper.getLevel().addFreshEntity(boomerang);
        return boomerang;
    }

    /** A boomerang that stays where it is: its way out is next to nothing, and it hangs there for good. */
    private static Boomerang hovering(GameTestHelper helper) {
        return launch(helper, null, new Boomerang.Flight(1.0E-4F, 1, EasingType.LINEAR, 100000, 0.6F, 0.15F, 0.35F, 0.25F, 0.0F, 100000, 1.0F, 0.0F, 0.0F, 0.6F, 8, 20));
    }

    private static ItemEntity carried(GameTestHelper helper, Boomerang boomerang) {
        var ingot = helper.spawnItem(Items.GOLD_INGOT, new Vec3(1.5D, Arena.FLOOR_Y + 1, 6.5D));
        helper.assertTrue(ingot.startRiding(boomerang, true, true), "the ingot could not ride the boomerang");
        return ingot;
    }
}

package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.super_mario.tags.SuperMarioDamageTypeTags;
import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.TestPlayers;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * The generated side of the mod: damage types, their tags, the attribute modifiers the power-ups carry,
 * and the environment attributes the biome modifications hand out. None of it is written by hand, so all
 * of it goes quiet rather than loud when a generator stops emitting something — usually noticed a version
 * later.
 */
public class GeneratedDataGameTest {
    private static final BlockPos GROUND = new BlockPos(4, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void everyDamageTypeIsLoaded(GameTestHelper helper) {
        var damageTypes = helper.getLevel().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);

        for (var key : List.of(
                SuperMarioDamageTypeIds.STOMP,
                SuperMarioDamageTypeIds.KOOPA_SHELL,
                SuperMarioDamageTypeIds.FIREBALL,
                SuperMarioDamageTypeIds.ICEBALL,
                SuperMarioDamageTypeIds.GOLD_FIREBALL)) {
            helper.assertTrue(damageTypes.get(key).isPresent(), key.identifier() + " is missing from the data pack");
        }

        helper.succeed();
    }

    /**
     * A goomba is squashed outright by whatever is in this tag, and takes normal damage from anything
     * else. Today that is stomping and koopa shells — the fireballs deliberately are not in it.
     */
    @GameTest
    public void onlySomeDamageTypesAreInstantKills(GameTestHelper helper) {
        var damageTypes = helper.getLevel().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);

        helper.assertTrue(damageTypes.getOrThrow(SuperMarioDamageTypeIds.STOMP).is(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS),
                "stomping should squash a goomba outright");
        helper.assertTrue(damageTypes.getOrThrow(SuperMarioDamageTypeIds.KOOPA_SHELL).is(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS),
                "a koopa shell should squash a goomba outright");
        helper.assertFalse(damageTypes.getOrThrow(SuperMarioDamageTypeIds.FIREBALL).is(SuperMarioDamageTypeTags.INSTANT_KILLS_GOOMBAS),
                "a fireball should do its own damage, not an instant kill");

        helper.succeed();
    }

    @GameTest
    public void agoombaDiesOutrightToATaggedDamageType(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);

        // One point of damage, but through a tagged type: the goomba turns it into a killing blow.
        goomba.hurtServer(helper.getLevel(), helper.getLevel().damageSources().source(SuperMarioDamageTypeIds.STOMP), 1.0F);

        helper.assertTrue(goomba.isDeadOrDying(), "a stomp should kill a goomba outright, whatever the damage");
        helper.succeed();
    }

    @GameTest
    public void agoombaSurvivesAPinprickFromAPlainFireball(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var goomba = helper.spawnWithNoFreeWill(SuperMarioEntityTypes.GOOMBA, GROUND);

        goomba.hurtServer(helper.getLevel(), helper.getLevel().damageSources().source(SuperMarioDamageTypeIds.FIREBALL), 1.0F);

        helper.assertFalse(goomba.isDeadOrDying(), "an untagged damage type should do exactly the damage it says");
        helper.succeed();
    }

    /**
     * The cloud form drifts down at half the usual gravity and jumps higher for it, so the drops it makes
     * routine should not be the ones that hurt: it falls further before the ground bites, and takes less
     * out of it when it does. The exact numbers are a balance matter, only their direction is asserted.
     */
    @GameTest
    public void theCloudFormSoftensItsFalls(GameTestHelper helper) {
        var player = TestPlayers.mock(helper);
        double bareSafeFall = player.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
        double bareMultiplier = player.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER);

        player.setPowerUp(PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.CLOUD));

        helper.assertTrue(player.getAttributeValue(Attributes.SAFE_FALL_DISTANCE) > bareSafeFall,
                "the cloud form should fall further than a bare player before taking any damage");
        helper.assertTrue(player.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER) < bareMultiplier,
                "the cloud form should take less out of a fall than a bare player");

        helper.succeed();
    }

    /**
     * The transforms are put on every biome by a biome modification rather than written in a data pack,
     * so this is the one place that notices if that registration stops running.
     */
    @GameTest
    public void thebiomeModificationsReachedThisBiome(GameTestHelper helper) {
        var attributes = helper.getLevel().environmentAttributes();
        var here = helper.absolutePos(GROUND);

        helper.assertFalse(attributes.getValue(SuperMarioEnvironmentAttributes.FIREBALL_MELTS, here).isEmpty(),
                "no melting transform on this biome, fireballs would leave ice alone");
        helper.assertFalse(attributes.getValue(SuperMarioEnvironmentAttributes.ICEBALL_FREEZES, here).isEmpty(),
                "no freezing transform on this biome, iceballs would leave water alone");

        helper.succeed();
    }
}

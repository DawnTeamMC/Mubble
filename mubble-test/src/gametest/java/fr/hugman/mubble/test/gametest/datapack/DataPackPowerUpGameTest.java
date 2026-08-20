package fr.hugman.mubble.test.gametest.datapack;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpCosmectics;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.ShootProjectilePowerUpAction;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

/**
 * Loading of the power-ups written by hand in the data pack of this module, see
 * {@link PowerUpFixtures}.
 */
public class DataPackPowerUpGameTest {
    /** Where the mob taking the attribute modifiers stands, one block above its footing. */
    private static final BlockPos TARGET_POS = new BlockPos(4, 1, 3);

    @GameTest
    public void dataPackPowerUpsAreLoaded(GameTestHelper helper) {
        var powerUps = powerUps(helper);

        for (ResourceKey<PowerUp> key : List.of(PowerUpFixtures.EMPTY, PowerUpFixtures.ATTRIBUTES_ONLY, PowerUpFixtures.REFERENCED_ACTION)) {
            helper.assertTrue(powerUps.get(key).isPresent(), key.identifier() + " was not loaded from the data pack");
        }

        helper.succeed();
    }

    @GameTest
    public void aPowerUpCanLeaveEveryFieldOut(GameTestHelper helper) {
        PowerUp powerUp = powerUps(helper).getOrThrow(PowerUpFixtures.EMPTY).value();

        helper.assertTrue(powerUp.name().isEmpty(), "an empty power-up got a name out of nowhere");
        helper.assertTrue(powerUp.spriteId().isEmpty(), "an empty power-up got a sprite out of nowhere");
        helper.assertTrue(powerUp.action().isEmpty(), "an empty power-up got an action out of nowhere");
        helper.assertTrue(powerUp.attributesModifiers().isEmpty(), "an empty power-up got attribute modifiers out of nowhere");
        helper.assertValueEqual(powerUp.cosmectics(), PowerUpCosmectics.EMPTY, "the cosmetics of an empty power-up");

        helper.succeed();
    }

    /**
     * Every shipped power-up inlines its action, so this is the only coverage of an action referenced
     * by id, which is what a data pack sharing one action between several power-ups would write.
     */
    @GameTest
    public void aPowerUpActionCanBeReferencedById(GameTestHelper helper) {
        PowerUp powerUp = powerUps(helper).getOrThrow(PowerUpFixtures.REFERENCED_ACTION).value();
        Holder<PowerUpAction> action = powerUp.action().orElseThrow(() -> new AssertionError("the power-up lost the action it references"));

        helper.assertTrue(action.is(PowerUpFixtures.SNOWBALL_BARRAGE), "the action was not resolved to the entry it points at");
        helper.assertTrue(action.value() instanceof ShootProjectilePowerUpAction, "the referenced action was not decoded as a shoot projectile action");

        var shoot = (ShootProjectilePowerUpAction) action.value();
        helper.assertValueEqual(shoot.projectile(), EntityTypes.SNOWBALL, "the projectile of the referenced action");
        helper.assertValueEqual(shoot.maxProjectiles(), Optional.of(5), "the projectile count of the referenced action");

        helper.succeed();
    }

    /**
     * Attribute modifiers written in a data pack must reach the attribute container, and leave it
     * again once the power-up is gone.
     */
    @GameTest
    public void dataPackAttributeModifiersApplyToLivingEntities(GameTestHelper helper) {
        helper.setBlock(TARGET_POS.below(), Blocks.STONE);
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, TARGET_POS);
        float baseMaxHealth = pig.getMaxHealth();

        Holder<PowerUp> powerUp = powerUps(helper).getOrThrow(PowerUpFixtures.ATTRIBUTES_ONLY);

        PowerUp.onChange(pig, Optional.empty(), Optional.of(powerUp));
        helper.assertValueEqual(pig.getMaxHealth(), baseMaxHealth + 10.0F, "the max health of a pig holding the power-up");

        PowerUp.onChange(pig, Optional.of(powerUp), Optional.empty());
        helper.assertValueEqual(pig.getMaxHealth(), baseMaxHealth, "the max health of a pig that lost the power-up");

        helper.succeed();
    }

    private static HolderGetter<PowerUp> powerUps(GameTestHelper helper) {
        return helper.getLevel().registryAccess().lookupOrThrow(MubbleRegistries.POWER_UP);
    }
}

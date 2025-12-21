package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.power_up.PowerUpBuilder;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.ShootProjectilePowerUpAction;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.references.PowerUpsKeys.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class MubblePowerUpProvider extends FabricDynamicRegistryProvider {
    public MubblePowerUpProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(MubbleRegistries.POWER_UP));
    }

    @Override
    public String getName() {
        return "Power-Ups";
    }

    public static void bootstrap(BootstrapContext<PowerUp> context) {
        context.register(MINI, new PowerUpBuilder()
                .name(MINI)
                .obtainSound(MubbleSounds.POWER_UP_OBTAIN_MINI)
                .canSprintOnWater(true)
                .attributesModifier(Attributes.SCALE, -0.67, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.GRAVITY, -0.3, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.JUMP_STRENGTH, 0.25, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.SAFE_FALL_DISTANCE, 6, ADD_VALUE)
                .attributesModifier(Attributes.MAX_HEALTH, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.STEP_HEIGHT, -0.67, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.FALL_DAMAGE_MULTIPLIER, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ATTACK_DAMAGE, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ATTACK_SPEED, 0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.BLOCK_BREAK_SPEED, -0.35, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.BLOCK_INTERACTION_RANGE, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ENTITY_INTERACTION_RANGE, -0.4, ADD_MULTIPLIED_BASE)
                .build());
        context.register(MEGA, new PowerUpBuilder()
                .name(MEGA)
                .attributesModifier(Attributes.SCALE, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.GRAVITY, 0.5, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.MOVEMENT_SPEED, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.JUMP_STRENGTH, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.SAFE_FALL_DISTANCE, 12, ADD_VALUE)
                .attributesModifier(Attributes.MAX_HEALTH, 0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.STEP_HEIGHT, 2, ADD_VALUE)
                .attributesModifier(Attributes.FALL_DAMAGE_MULTIPLIER, -0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ATTACK_DAMAGE, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ATTACK_SPEED, -0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.BLOCK_BREAK_SPEED, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.BLOCK_INTERACTION_RANGE, 1.2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.ENTITY_INTERACTION_RANGE, 1.2, ADD_MULTIPLIED_BASE)
                .build());
        context.register(FIRE, new PowerUpBuilder()
                .name(FIRE)
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        MubbleEntityTypes.FIREBALL,
                        MubbleSounds.FIREBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
        context.register(ICE, new PowerUpBuilder()
                .name(ICE)
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        MubbleEntityTypes.ICEBALL,
                        MubbleSounds.ICEBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
    }
}
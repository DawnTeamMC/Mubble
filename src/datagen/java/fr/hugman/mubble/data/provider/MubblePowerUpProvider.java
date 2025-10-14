package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.power_up.PowerUpBuilder;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.action.ShootProjectilePowerUpAction;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import fr.hugman.mubble.sound.MubbleSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.power_up.PowerUps.*;
import static net.minecraft.entity.attribute.EntityAttributeModifier.Operation.*;

public class MubblePowerUpProvider extends FabricDynamicRegistryProvider {
    public MubblePowerUpProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(MubbleRegistryKeys.POWER_UP));
    }

    @Override
    public String getName() {
        return "Power-Ups";
    }

    public static void register(Registerable<PowerUp> registerable) {
        registerable.register(MINI, new PowerUpBuilder()
                .name(MINI)
                .obtainSound(MubbleSounds.POWER_UP_OBTAIN_MINI)
                .canSprintOnWater(true)
                .attributesModifier(EntityAttributes.SCALE, -0.67, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.GRAVITY, -0.3, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.JUMP_STRENGTH, 0.25, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.SAFE_FALL_DISTANCE, 6, ADD_VALUE)
                .attributesModifier(EntityAttributes.MAX_HEALTH, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.STEP_HEIGHT, -0.67, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.FALL_DAMAGE_MULTIPLIER, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ATTACK_DAMAGE, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ATTACK_SPEED, 0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.BLOCK_BREAK_SPEED, -0.35, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.BLOCK_INTERACTION_RANGE, -0.4, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ENTITY_INTERACTION_RANGE, -0.4, ADD_MULTIPLIED_BASE)
                .build());
        registerable.register(MEGA, new PowerUpBuilder()
                .name(MEGA)
                .attributesModifier(EntityAttributes.SCALE, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.GRAVITY, 0.5, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.MOVEMENT_SPEED, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.JUMP_STRENGTH, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.SAFE_FALL_DISTANCE, 12, ADD_VALUE)
                .attributesModifier(EntityAttributes.MAX_HEALTH, 0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.STEP_HEIGHT, 2, ADD_VALUE)
                .attributesModifier(EntityAttributes.FALL_DAMAGE_MULTIPLIER, -0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ATTACK_DAMAGE, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ATTACK_SPEED, -0.6, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.BLOCK_BREAK_SPEED, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.BLOCK_INTERACTION_RANGE, 1.2, ADD_MULTIPLIED_BASE)
                .attributesModifier(EntityAttributes.ENTITY_INTERACTION_RANGE, 1.2, ADD_MULTIPLIED_BASE)
                .build());
        registerable.register(FIRE, new PowerUpBuilder()
                .name(FIRE)
                .action(RegistryEntry.of(new ShootProjectilePowerUpAction(
                        MubbleEntityTypes.FIREBALL,
                        MubbleSounds.FIREBALL_THROW,
                        0.4f
                )))
                .build());
        registerable.register(ICE, new PowerUpBuilder()
                .name(ICE)
                .action(RegistryEntry.of(new ShootProjectilePowerUpAction(
                        MubbleEntityTypes.ICEBALL,
                        MubbleSounds.ICEBALL_THROW,
                        0.4f
                )))
                .build());
    }
}
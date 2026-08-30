package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.power_up.action.SpawnCloudPlatformPowerUpAction;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpBuilder;
import fr.hugman.mubble.world.power_up.action.ShootProjectilePowerUpAction;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class SuperMarioPowerUpProvider extends FabricDynamicRegistryProvider {
    public SuperMarioPowerUpProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
        //TODO: some power-ups should be lost when taking too much damage
        context.register(MINI, builder(MINI)
                .obtainSound(SuperMarioSounds.POWER_UP_OBTAIN_MINI)
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
        context.register(MEGA, builder(MEGA)
                .attributesModifier(Attributes.SCALE, 2, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.GRAVITY, 0.5, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.MOVEMENT_SPEED, 1.75, ADD_MULTIPLIED_BASE)
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
        context.register(FIRE, builder(FIRE, true)
                .emissiveOverlay()
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        SuperMarioEntityTypes.FIREBALL,
                        SuperMarioSounds.FIREBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
        context.register(ICE, builder(ICE, true)
                .emissiveOverlay()
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        SuperMarioEntityTypes.ICEBALL,
                        SuperMarioSounds.ICEBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
        context.register(GOLD, builder(GOLD, true)
                .emissiveOverlay()
                .obtainSound(SuperMarioSounds.POWER_UP_OBTAIN_GOLD)
                .emitSound(SuperMarioSounds.POWER_UP_EMIT_GOLD)
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        SuperMarioEntityTypes.GOLD_FIREBALL,
                        SuperMarioSounds.GOLD_FIREBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .particle(SuperMarioParticleTypes.COIN_SPARKLE)
                .build());
        context.register(CLOUD, builder(CLOUD)
                .action(Holder.direct(new SpawnCloudPlatformPowerUpAction(SuperMarioEntityTypes.CLOUD_PLATFORM, Optional.of(3))))
                .attributesModifier(Attributes.GRAVITY, -0.5, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.JUMP_STRENGTH, 0.35, ADD_MULTIPLIED_BASE)
                .attributesModifier(Attributes.SAFE_FALL_DISTANCE, 9, ADD_VALUE)
                .attributesModifier(Attributes.FALL_DAMAGE_MULTIPLIER, -0.5, ADD_MULTIPLIED_BASE)
                .build());
    }

    public static PowerUpBuilder builder(ResourceKey<PowerUp> key, boolean withOverlay) {
        var builder = new PowerUpBuilder()
                .name(key)
                .obtainSound(SuperMarioSounds.POWER_UP_OBTAIN)
                .refillSound(SuperMarioSounds.POWER_UP_REFILL)
                .looseSound(SuperMarioSounds.POWER_UP_LOOSE);
        if (withOverlay) {
            builder.humanoidOverlay(key);
        }
        return builder;
    }

    public static PowerUpBuilder builder(ResourceKey<PowerUp> key) {
        return builder(key, false);
    }
}
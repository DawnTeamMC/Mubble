package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
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

import static fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys.*;
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
        context.register(MINI, create(MINI)
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
        context.register(MEGA, create(MEGA)
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
        context.register(FIRE, create(FIRE)
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        SuperMarioEntityTypes.FIREBALL,
                        SuperMarioSounds.FIREBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
        context.register(ICE, create(ICE)
                .action(Holder.direct(new ShootProjectilePowerUpAction(
                        SuperMarioEntityTypes.ICEBALL,
                        SuperMarioSounds.ICEBALL_THROW,
                        0.4f,
                        Optional.of(3),
                        Optional.empty()
                )))
                .build());
    }

    public static PowerUpBuilder create(ResourceKey<PowerUp> key) {
        return new PowerUpBuilder()
            .name(key)
            .obtainSound(SuperMarioSounds.POWER_UP_OBTAIN)
            .looseSound(SuperMarioSounds.POWER_UP_LOOSE);
    }
}
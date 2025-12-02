package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.entity.damage.MubbleDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class MubbleDamageTypeProvider extends FabricDynamicRegistryProvider {
    public MubbleDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(RegistryKeys.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
        return "Damage Types";
    }

    public static void register(Registerable<DamageType> registerable) {
		registerable.register(MubbleDamageTypes.STOMP, new DamageType("mubble.stomp", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f));
		registerable.register(MubbleDamageTypes.KOOPA_SHELL, new DamageType("mubble.koopa_shell", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f));
    }
}
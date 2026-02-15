package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class SuperMarioDamageTypeProvider extends FabricDynamicRegistryProvider {
    public SuperMarioDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
        return "Damage Types";
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
		context.register(SuperMarioDamageTypeKeys.STOMP, new DamageType(SuperMario.MOD_ID + ".stomp", 0.1f));
		context.register(SuperMarioDamageTypeKeys.KOOPA_SHELL, new DamageType(SuperMario.MOD_ID + ".koopa_shell", 0.1f));
		context.register(SuperMarioDamageTypeKeys.FIREBALL, new DamageType(SuperMario.MOD_ID + ".fireball", 0.1f, DamageEffects.BURNING));
		context.register(SuperMarioDamageTypeKeys.ICEBALL, new DamageType(SuperMario.MOD_ID + ".iceball", 0.1f, DamageEffects.FREEZING));
        context.register(SuperMarioDamageTypeKeys.GOLD_FIREBALL, new DamageType(SuperMario.MOD_ID + ".gold_fireball", 0.1f));
    }
}
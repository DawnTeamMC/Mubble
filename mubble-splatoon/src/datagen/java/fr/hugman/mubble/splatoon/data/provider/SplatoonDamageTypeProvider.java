package fr.hugman.mubble.splatoon.data.provider;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.world.damagesource.SplatoonDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class SplatoonDamageTypeProvider extends FabricDynamicRegistryProvider {
    public SplatoonDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
        context.register(SplatoonDamageTypes.INK, new DamageType(Splatoon.MOD_ID + ".ink", 0.0f));
    }
}

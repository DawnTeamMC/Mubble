package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.references.ZeldaDamageTypeIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class ZeldaDamageTypeProvider extends FabricDynamicRegistryProvider {
    public ZeldaDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
        context.register(ZeldaDamageTypeIds.LOFTWING_CRASH, new DamageType(Zelda.MOD_ID + ".loftwing_crash", 0.1f));
    }
}

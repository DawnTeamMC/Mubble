package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.references.MubbleDamageTypeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import java.util.concurrent.CompletableFuture;

public class MubbleDamageTypeProvider extends FabricDynamicRegistryProvider {
    public MubbleDamageTypeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
		context.register(MubbleDamageTypeKeys.STOMP, new DamageType("mubble.stomp", 0.1f));
		context.register(MubbleDamageTypeKeys.KOOPA_SHELL, new DamageType("mubble.koopa_shell", 0.1f));
		context.register(MubbleDamageTypeKeys.FIREBALL, new DamageType("mubble.fireball", 0.1f, DamageEffects.BURNING));
		context.register(MubbleDamageTypeKeys.ICEBALL, new DamageType("mubble.iceball", 0.1f, DamageEffects.FREEZING));
    }
}
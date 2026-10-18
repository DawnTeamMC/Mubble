package fr.hugman.mubble.zelda.data.provider;

import fr.hugman.mubble.world.item.spawn_egg.VariantSpawnEggInfo;
import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.references.LoftwingVariantIds;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.LoftwingVariant;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ZeldaLoftwingVariantProvider extends FabricDynamicRegistryProvider {
    /** Thought extinct by the people of Skyloft, so a twentieth as common as any other breed. */
    private static final int CRIMSON_WEIGHT = 1;

    public ZeldaLoftwingVariantProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(ZeldaRegistries.LOFTWING_VARIANT));
    }

    @Override
    public String getName() {
        return "Loftwing Variants";
    }

    public static void bootstrap(BootstrapContext<LoftwingVariant> context) {
        // The plain blue bird every Skyloftian rides: no name of its own, and no spawn egg either.
        context.register(LoftwingVariantIds.BLUE, new LoftwingVariant(
                Optional.empty(),
                assets("blue"),
                LoftwingVariant.DEFAULT_WEIGHT,
                Optional.empty()
        ));
        register(context, LoftwingVariantIds.GREEN, "green", LoftwingVariant.DEFAULT_WEIGHT);
        register(context, LoftwingVariantIds.YELLOW, "yellow", LoftwingVariant.DEFAULT_WEIGHT);
        register(context, LoftwingVariantIds.VIOLET, "violet", LoftwingVariant.DEFAULT_WEIGHT);
        register(context, LoftwingVariantIds.CRIMSON, "crimson", CRIMSON_WEIGHT);
    }

    private static void register(BootstrapContext<LoftwingVariant> context, ResourceKey<LoftwingVariant> key, String name, int weight) {
        context.register(key, new LoftwingVariant(
                Optional.of(Component.translatable(variantKey(name))),
                assets(name),
                weight,
                Optional.of(new VariantSpawnEggInfo(Component.translatable(spawnEggKey(name))))
        ));
    }

    private static LoftwingVariant.LoftwingAssetInfo assets(String name) {
        return new LoftwingVariant.LoftwingAssetInfo(new ClientAsset.ResourceTexture(Zelda.id("entity/loftwing/" + name)));
    }

    public static String variantKey(String name) {
        return "entity." + Zelda.MOD_ID + ".loftwing." + name;
    }

    public static String spawnEggKey(String name) {
        return "item." + Zelda.MOD_ID + "." + name + "_loftwing_spawn_egg";
    }
}

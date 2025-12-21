package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.entity.monster.goomba.GoombaVariant;
import fr.hugman.mubble.references.GoombaVariantKeys;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.item.spawn_egg.VariantSpawnEggInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MubbleGoombaVariantProvider extends FabricDynamicRegistryProvider {
    public MubbleGoombaVariantProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(MubbleRegistries.GOOMBA_VARIANT));
    }

    @Override
    public String getName() {
        return "Goomba Variants";
    }

    public static void bootstrap(BootstrapContext<GoombaVariant> context) {
		context.register(GoombaVariantKeys.NORMAL, new GoombaVariant(
				Optional.empty(),
				new GoombaVariant.GoombaAssetInfo(
						new ClientAsset.ResourceTexture(Mubble.id("entity/goomba/normal/normal")),
						new ClientAsset.ResourceTexture(Mubble.id("entity/goomba/normal/surprised"))
				),
				Map.of(),
				Optional.empty()
		));
		context.register(GoombaVariantKeys.MINI, new GoombaVariant(
				Optional.of(Component.translatable("entity.mubble.goomba.mini")),
				new GoombaVariant.GoombaAssetInfo(
						new ClientAsset.ResourceTexture(Mubble.id("entity/goomba/mini/normal")),
						new ClientAsset.ResourceTexture(Mubble.id("entity/goomba/mini/surprised"))
				),
				Map.of(
						Attributes.MAX_HEALTH, 2.0D,
						Attributes.SCALE, 0.5D,
						Attributes.MOVEMENT_SPEED, 0.25D,
						Attributes.ATTACK_DAMAGE, 0.75D,
						Attributes.JUMP_STRENGTH, 0.65D,
						Attributes.GRAVITY, 0.12D
				),
				Optional.of(new VariantSpawnEggInfo(
						Component.translatable("item.mubble.mini_goomba_spawn_egg")
				))
		));
    }
}
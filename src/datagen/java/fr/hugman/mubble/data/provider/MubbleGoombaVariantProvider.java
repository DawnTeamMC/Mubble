package fr.hugman.mubble.data.provider;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.entity.GoombaVariants;
import fr.hugman.mubble.item.spawn_egg.VariantSpawnEggInfo;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.AssetInfo;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MubbleGoombaVariantProvider extends FabricDynamicRegistryProvider {
    public MubbleGoombaVariantProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(MubbleRegistryKeys.GOOMBA_VARIANT));
    }

    @Override
    public String getName() {
        return "Goomba Variants";
    }

    public static void register(Registerable<GoombaVariant> registerable) {
		registerable.register(GoombaVariants.NORMAL, new GoombaVariant(
				Optional.empty(),
				new GoombaVariant.GoombaAssetInfo(
						new AssetInfo.TextureAssetInfo(Mubble.id("entity/goomba/normal/normal")),
						new AssetInfo.TextureAssetInfo(Mubble.id("entity/goomba/normal/surprised"))
				),
				Map.of(),
				Optional.empty()
		));
		registerable.register(GoombaVariants.MINI, new GoombaVariant(
				Optional.of(Text.translatable("entity.mubble.goomba.mini")),
				new GoombaVariant.GoombaAssetInfo(
						new AssetInfo.TextureAssetInfo(Mubble.id("entity/goomba/mini/normal")),
						new AssetInfo.TextureAssetInfo(Mubble.id("entity/goomba/mini/surprised"))
				),
				Map.of(
						EntityAttributes.MAX_HEALTH, 2.0D,
						EntityAttributes.SCALE, 0.5D,
						EntityAttributes.MOVEMENT_SPEED, 0.25D,
						EntityAttributes.ATTACK_DAMAGE, 0.75D,
						EntityAttributes.JUMP_STRENGTH, 0.65D,
						EntityAttributes.GRAVITY, 0.12D
				),
				Optional.of(new VariantSpawnEggInfo(
						Text.translatable("item.mubble.mini_goomba_spawn_egg")
				))
		));
    }
}
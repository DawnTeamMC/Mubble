package fr.hugman.mubble.super_mario.data.provider;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.references.GoombaVariantIds;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import fr.hugman.mubble.world.item.spawn_egg.VariantSpawnEggInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SuperMarioGoombaVariantProvider extends FabricDynamicRegistryProvider {
    public SuperMarioGoombaVariantProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(SuperMarioRegistries.GOOMBA_VARIANT));
    }

    @Override
    public String getName() {
        return "Goomba Variants";
    }

    public static void bootstrap(BootstrapContext<GoombaVariant> context) {
		context.register(GoombaVariantIds.NORMAL, new GoombaVariant(
				Optional.empty(),
				new GoombaVariant.GoombaAssetInfo(
						new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/normal/normal")),
						new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/normal/surprised"))
				),
				Map.of(),
				Optional.empty()
		));
		context.register(GoombaVariantIds.MINI, new GoombaVariant(
				Optional.of(Component.translatable("entity." + SuperMario.MOD_ID + ".goomba.mini")),
				new GoombaVariant.GoombaAssetInfo(
						new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/normal")),
						new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/surprised"))
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
						Component.translatable("item." + SuperMario.MOD_ID + ".mini_goomba_spawn_egg")
				))
		));
    }
}
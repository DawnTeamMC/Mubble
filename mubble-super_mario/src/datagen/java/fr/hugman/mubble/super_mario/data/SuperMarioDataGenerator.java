package fr.hugman.mubble.super_mario.data;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.data.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

public class SuperMarioDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider(SuperMarioModelProvider::new);
		pack.addProvider(SuperMarioAtlasProvider::new);
		pack.addProvider(SuperMarioSoundsProvider::new);
		pack.addProvider(SuperMarioEnglishLangProvider::new);

		// Data Pack
		pack.addProvider(SuperMarioDamageTypeProvider::new);

		// - Loot tables
		pack.addProvider(SuperMarioBlockLootSubProvider::new);
		pack.addProvider(SuperMarioLootSubProvider::new);

		// - Variants
		pack.addProvider(SuperMarioGoombaVariantProvider::new);

        // - Power-Ups
        pack.addProvider(SuperMarioPowerUpProvider::new);

		// - Tags
		var blockTags = pack.addProvider(SuperMarioBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new SuperMarioItemTagProvider(output, registriesFuture, blockTags));
		pack.addProvider(SuperMarioEntityTypeTagsProvider::new);
		pack.addProvider(SuperMarioDamageTypeTagsProvider::new);
		pack.addProvider(SuperMarioPowerUpTagsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, SuperMarioDamageTypeProvider::bootstrap);
		registryBuilder.add(SuperMarioRegistries.GOOMBA_VARIANT, SuperMarioGoombaVariantProvider::bootstrap);
		registryBuilder.add(MubbleRegistries.POWER_UP, SuperMarioPowerUpProvider::bootstrap);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return SuperMario.MOD_ID;
	}
}

package fr.hugman.mubble.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.provider.*;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

public class MubbleDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider(MubbleModelProvider::new);
		pack.addProvider(MubbleAtlasProvider::new);
		pack.addProvider(MubbleSoundsProvider::new);
		pack.addProvider(MubbleEnglishLangProvider::new);

		// Data Pack
		pack.addProvider(MubbleDamageTypeProvider::new);

		// - Loot tables
		pack.addProvider(MubbleBlockLootSubProvider::new);

		// - Variants
		pack.addProvider(MubbleGoombaVariantProvider::new);

        // - Power-Ups
        pack.addProvider(MubblePowerUpProvider::new);

		// - Tags
		var blockTags = pack.addProvider(MubbleBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new MubbleItemTagProvider(output, registriesFuture, blockTags));
		pack.addProvider(MubbleEntityTypeTagsProvider::new);
		pack.addProvider(MubbleDamageTypeTagsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, MubbleDamageTypeProvider::bootstrap);
		registryBuilder.add(MubbleRegistries.GOOMBA_VARIANT, MubbleGoombaVariantProvider::bootstrap);
		registryBuilder.add(MubbleRegistries.POWER_UP, MubblePowerUpProvider::bootstrap);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Mubble.MOD_ID;
	}
}

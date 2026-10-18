package fr.hugman.mubble.zelda.data;

import fr.hugman.mubble.zelda.Zelda;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.data.provider.ZeldaBlockTagsProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaDamageTypeProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaEnglishLangProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaGameplayLootSubProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaLoftwingVariantProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaModelProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaRecipeProvider;
import fr.hugman.mubble.zelda.data.provider.ZeldaSoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

public class ZeldaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider(ZeldaModelProvider::new);
		pack.addProvider(ZeldaSoundsProvider::new);
		pack.addProvider(ZeldaEnglishLangProvider::new);

		// Data Pack
		pack.addProvider(ZeldaDamageTypeProvider::new);
		pack.addProvider(ZeldaRecipeProvider::new);
		pack.addProvider(ZeldaGameplayLootSubProvider::new);
		pack.addProvider(ZeldaLoftwingVariantProvider::new);

		// - Tags
		pack.addProvider(ZeldaBlockTagsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, ZeldaDamageTypeProvider::bootstrap);
		registryBuilder.add(ZeldaRegistries.LOFTWING_VARIANT, ZeldaLoftwingVariantProvider::bootstrap);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Zelda.MOD_ID;
	}
}

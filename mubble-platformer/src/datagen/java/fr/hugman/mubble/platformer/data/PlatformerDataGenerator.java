package fr.hugman.mubble.platformer.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.Nullable;

public class PlatformerDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider((output, registries) -> new PlatformerLangProvider(output, PlatformerTranslations.ENGLISH, "en_us", registries));
		pack.addProvider((output, registries) -> new PlatformerLangProvider(output, PlatformerTranslations.FRENCH, "fr_fr", registries));
		pack.addProvider(PlatformerControlifyProvider::new);

		// Data Pack
		pack.addProvider(PlatformerBlockTagsProvider::new);
		pack.addProvider(PlatformerMoveTagsProvider::new);
		pack.addProvider(PlatformerProfileProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(PlatformerRegistries.PLATFORMER_PROFILE, PlatformerProfileProvider::bootstrap);
	}

	/** The content of the layer lives in the namespace of the core. */
	@Override
	@Nullable
	public String getEffectiveModId() {
		return Mubble.MOD_ID;
	}
}

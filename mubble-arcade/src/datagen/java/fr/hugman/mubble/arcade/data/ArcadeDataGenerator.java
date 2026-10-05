package fr.hugman.mubble.arcade.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.Nullable;

public class ArcadeDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider((output, registries) -> new ArcadeLangProvider(output, ArcadeTranslations.ENGLISH, "en_us", registries));
		pack.addProvider((output, registries) -> new ArcadeLangProvider(output, ArcadeTranslations.FRENCH, "fr_fr", registries));
		pack.addProvider(ArcadeGuideProvider::new);

		// Data Pack
		pack.addProvider(ArcadeBlockTagsProvider::new);
		pack.addProvider(ArcadeMoveTagsProvider::new);
		pack.addProvider(ArcadeProfileProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(ArcadeRegistries.ARCADE_PROFILE, ArcadeProfileProvider::bootstrap);
	}

	/** The content of the layer lives in the namespace of the core. */
	@Override
	@Nullable
	public String getEffectiveModId() {
		return Mubble.MOD_ID;
	}
}

package fr.hugman.mubble.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.arcade.ArcadeProfileProvider;
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
		pack.addProvider(MubbleEnglishLangProvider::new);
		pack.addProvider(MubbleFrenchLangProvider::new);

		// Data Pack
		pack.addProvider(MubbleBlockTagsProvider::new);
		pack.addProvider(ArcadeMoveTagsProvider::new);
		pack.addProvider(ArcadeProfileProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(MubbleRegistries.ARCADE_PROFILE, ArcadeProfileProvider::bootstrap);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Mubble.MOD_ID;
	}
}

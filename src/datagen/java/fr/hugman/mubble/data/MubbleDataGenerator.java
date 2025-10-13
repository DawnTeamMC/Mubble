package fr.hugman.mubble.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.data.provider.*;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
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

		// - Loot tables
		pack.addProvider(MubbleBlockLootTableProvider::new);

		// - Variants
		pack.addProvider(MubbleGoombaVariantProvider::new);

		// - Tags
		pack.addProvider(MubbleBlockTagProvider::new);
		pack.addProvider(MubbleEntityTypeTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(MubbleRegistryKeys.GOOMBA_VARIANT, MubbleGoombaVariantProvider::register);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Mubble.MOD_ID;
	}
}

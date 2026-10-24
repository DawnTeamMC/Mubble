package fr.hugman.mubble.splatoon.data;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.data.provider.SplatoonBlockTagsProvider;
import fr.hugman.mubble.splatoon.data.provider.SplatoonDamageTypeProvider;
import fr.hugman.mubble.splatoon.data.provider.SplatoonDamageTypeTagsProvider;
import fr.hugman.mubble.splatoon.data.provider.SplatoonEnglishLangProvider;
import fr.hugman.mubble.splatoon.data.provider.SplatoonModelProvider;
import fr.hugman.mubble.splatoon.data.provider.SplatoonSoundsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

public class SplatoonDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource pack
		pack.addProvider(SplatoonEnglishLangProvider::new);
		pack.addProvider(SplatoonModelProvider::new);
		pack.addProvider(SplatoonSoundsProvider::new);

		// Data pack
		pack.addProvider(SplatoonDamageTypeProvider::new);
		pack.addProvider(SplatoonBlockTagsProvider::new);
		pack.addProvider(SplatoonDamageTypeTagsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, SplatoonDamageTypeProvider::bootstrap);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Splatoon.MOD_ID;
	}
}

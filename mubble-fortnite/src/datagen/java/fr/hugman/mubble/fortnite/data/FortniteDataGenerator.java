package fr.hugman.mubble.fortnite.data;

import fr.hugman.mubble.fortnite.Fortnite;
import fr.hugman.mubble.fortnite.data.provider.FortniteEnglishLangProvider;
import fr.hugman.mubble.fortnite.data.provider.FortniteModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

public class FortniteDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Resource Pack
		pack.addProvider(FortniteModelProvider::new);
		pack.addProvider(FortniteEnglishLangProvider::new);
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Fortnite.MOD_ID;
	}
}

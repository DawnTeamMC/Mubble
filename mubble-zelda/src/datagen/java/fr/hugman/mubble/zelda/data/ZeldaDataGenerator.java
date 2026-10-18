package fr.hugman.mubble.zelda.data;

import fr.hugman.mubble.zelda.Zelda;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

public class ZeldaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.createPack();
	}

	@Override
	@Nullable
	public String getEffectiveModId() {
		return Zelda.MOD_ID;
	}
}

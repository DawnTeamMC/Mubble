package fr.hugman.mubble;

import fr.hugman.dawn.Registrar;
import fr.hugman.mubble.registry.SuperMarioContent;
import fr.hugman.mubble.world.MubbleGamerules;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
	public static final Registrar REGISTRAR = new Registrar("mubble");
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
		MubbleGamerules.init();

		SuperMarioContent.init(REGISTRAR);
	}

	public static Identifier id(String path) {
		return REGISTRAR.id(path);
	}
}
package fr.hugman.mubble.zelda;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Legend of Zelda module: content from the Zelda licence, in its own {@code zelda} namespace.
 */
public class Zelda implements ModInitializer {
    public static final String MOD_ID = "zelda";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

package fr.hugman.mubble.zelda;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.core.registries.ZeldaBuiltInRegistries;
import fr.hugman.mubble.zelda.network.syncher.ZeldaEntityDataSerializers;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.item.ZeldaCreativeModeTabs;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import fr.hugman.mubble.zelda.world.level.biome.ZeldaBiomeModifications;
import fr.hugman.mubble.zelda.world.level.block.ZeldaBlocks;
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
        Reflection.initialize(ZeldaBlocks.class);
        Reflection.initialize(ZeldaEntityDataSerializers.class);
        Reflection.initialize(ZeldaEntityTypes.class);
        Reflection.initialize(ZeldaDataComponents.class);
        Reflection.initialize(ZeldaItems.class);
        Reflection.initialize(ZeldaSounds.class);

        ZeldaEntityTypes.registerAttributes();
        ZeldaEntityTypes.registerSpawnPlacements();

        ZeldaCreativeModeTabs.appendItemGroups();

        ZeldaBuiltInRegistries.register();

        ZeldaBiomeModifications.register();

        ZeldaInteraction.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

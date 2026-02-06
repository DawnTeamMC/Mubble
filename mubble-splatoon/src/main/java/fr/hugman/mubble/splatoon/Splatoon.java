package fr.hugman.mubble.splatoon;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.core.registries.SplatoonBuiltInRegistries;
import fr.hugman.mubble.splatoon.core.registries.SplatoonRegistries;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import fr.hugman.mubble.splatoon.world.level.block.SplatoonBlocks;
import fr.hugman.mubble.splatoon.world.item.SplatoonCreativeModeTabs;
import fr.hugman.mubble.splatoon.world.item.SplatoonItems;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeaponTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Splatoon implements ModInitializer {
    public static final String MOD_ID = "splatoon";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(SplatoonRegistries.class);

        Reflection.initialize(SplatoonBlocks.class);
        Reflection.initialize(SplatoonItems.class);
        Reflection.initialize(SplatoonCreativeModeTabs.class);
        Reflection.initialize(SplatoonSounds.class);
        Reflection.initialize(SplatoonEntityTypes.class);
        Reflection.initialize(SplatoonDataComponents.class);
        Reflection.initialize(SplatoonWeaponTypes.class);

        SplatoonBuiltInRegistries.register();
        SplatoonCreativeModeTabs.appendItemGroups();

    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

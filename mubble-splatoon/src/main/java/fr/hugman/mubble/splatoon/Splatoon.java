package fr.hugman.mubble.splatoon;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.splatoon.core.component.SplatoonDataComponents;
import fr.hugman.mubble.splatoon.core.registries.SplatoonBuiltInRegistries;
import fr.hugman.mubble.splatoon.core.registries.SplatoonRegistries;
import fr.hugman.mubble.splatoon.network.protocol.common.custom.SplatoonPayloadTypes;
import fr.hugman.mubble.splatoon.sounds.SplatoonSounds;
import fr.hugman.mubble.splatoon.world.attribute.SplatoonEnvironmentAttributes;
import fr.hugman.mubble.splatoon.world.entity.SplatoonEntityTypes;
import fr.hugman.mubble.splatoon.world.item.SplatoonCreativeModeTabs;
import fr.hugman.mubble.splatoon.world.item.SplatoonItems;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeaponTypes;
import fr.hugman.mubble.splatoon.world.level.attachment.SplatoonAttachmentTypes;
import fr.hugman.mubble.splatoon.world.level.ink.InkSync;
import fr.hugman.mubble.splatoon.world.level.ink.InkWeathering;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Splatoon implements ModInitializer {
    public static final String MOD_ID = "splatoon";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(SplatoonRegistries.class);

        Reflection.initialize(SplatoonItems.class);
        Reflection.initialize(SplatoonCreativeModeTabs.class);
        Reflection.initialize(SplatoonSounds.class);
        Reflection.initialize(SplatoonEntityTypes.class);
        Reflection.initialize(SplatoonDataComponents.class);
        Reflection.initialize(SplatoonWeaponTypes.class);
        Reflection.initialize(SplatoonAttachmentTypes.class);
        Reflection.initialize(SplatoonEnvironmentAttributes.class);

        SplatoonBuiltInRegistries.register();
        SplatoonCreativeModeTabs.appendItemGroups();
        SplatoonPayloadTypes.registerTypes();
        registerInkEvents();
    }

    private static void registerInkEvents() {
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, generated) -> InkWeathering.onChunkLoad(level, chunk));
        ServerChunkEvents.CHUNK_UNLOAD.register(InkWeathering::onChunkUnload);
        ServerTickEvents.END_LEVEL_TICK.register(level -> {
            InkWeathering.tick(level);
            InkSync.flush(level);
        });
        ServerLevelEvents.UNLOAD.register((server, level) -> {
            InkWeathering.forget(level);
            InkSync.forget(level);
        });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

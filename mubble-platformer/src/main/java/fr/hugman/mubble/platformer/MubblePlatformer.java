package fr.hugman.mubble.platformer;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.item.PlatformerDataComponents;
import fr.hugman.mubble.platformer.item.PlatformerItems;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.network.PlatformerEntityDataSerializers;
import fr.hugman.mubble.platformer.network.PlatformerPayloadTypes;
import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import fr.hugman.mubble.platformer.reward.PlatformerRewardTypes;
import fr.hugman.mubble.platformer.server.PlatformerServerNetworking;
import fr.hugman.mubble.platformer.server.commands.PlatformerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;

/**
 * The platformer movement layer: Super Mario styled moves, a camera that orbits the player and a server
 * that checks every step. Its content lives in the {@code mubble} namespace, next to the core's.
 */
public class MubblePlatformer implements ModInitializer {
    public static final String MOD_ID = "mubble_platformer";

    @Override
    public void onInitialize() {
        Reflection.initialize(PlatformerBuiltInRegistries.class);
        Reflection.initialize(PlatformerDataComponents.class);
        Reflection.initialize(PlatformerEntityDataSerializers.class);
        Reflection.initialize(PlatformerAttributes.class);
        Reflection.initialize(PlatformerMoves.class);
        Reflection.initialize(PlatformerAttachments.class);
        Reflection.initialize(PlatformerRewardTypes.class);
        Reflection.initialize(PlatformerItems.class);
        PlatformerItems.registerCreativeTabs();

        PlatformerBuiltInRegistries.register();
        PlatformerPayloadTypes.registerTypes();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PlatformerCommand.register(dispatcher, registryAccess));

        DataResourceLoader.get().registerReloadListener(Mubble.id("platformer_profiles"), PlatformerProfiles.ReloadListener::new);
        PlatformerServerNetworking.register();
        PlatformerInteraction.register();
    }
}

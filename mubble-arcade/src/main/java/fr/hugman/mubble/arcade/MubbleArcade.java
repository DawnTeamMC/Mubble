package fr.hugman.mubble.arcade;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.item.ArcadeDataComponents;
import fr.hugman.mubble.arcade.item.ArcadeItems;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.network.ArcadeEntityDataSerializers;
import fr.hugman.mubble.arcade.network.ArcadePayloadTypes;
import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.reward.ArcadeRewardTypes;
import fr.hugman.mubble.arcade.server.ArcadeServerNetworking;
import fr.hugman.mubble.arcade.server.commands.ArcadeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;

/**
 * The arcade movement layer: Super Mario styled moves, a camera that orbits the player and a server
 * that checks every step. Its content lives in the {@code mubble} namespace, next to the core's.
 */
public class MubbleArcade implements ModInitializer {
    public static final String MOD_ID = "mubble_arcade";

    @Override
    public void onInitialize() {
        Reflection.initialize(ArcadeBuiltInRegistries.class);
        Reflection.initialize(ArcadeDataComponents.class);
        Reflection.initialize(ArcadeEntityDataSerializers.class);
        Reflection.initialize(ArcadeAttributes.class);
        Reflection.initialize(ArcadeMoves.class);
        Reflection.initialize(ArcadeAttachments.class);
        Reflection.initialize(ArcadeRewardTypes.class);
        Reflection.initialize(ArcadeItems.class);
        ArcadeItems.registerCreativeTabs();

        ArcadeBuiltInRegistries.register();
        ArcadePayloadTypes.registerTypes();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ArcadeCommand.register(dispatcher, registryAccess));

        DataResourceLoader.get().registerReloadListener(Mubble.id("arcade_profiles"), ArcadeProfiles.ReloadListener::new);
        ArcadeServerNetworking.register();
        ArcadeInteraction.register();
    }
}

package fr.hugman.mubble;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.commands.MubbleCommands;
import fr.hugman.mubble.advancements.MubbleCriteriaTriggers;
import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.network.protocol.MubbleServerReceivers;
import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.world.attribute.MubbleAttributeTypes;
import fr.hugman.mubble.world.voyage.environment.EnvironmentController;
import fr.hugman.mubble.world.voyage.session.VoyageSessions;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.item.consume_effects.MubbleConsumeEffectTypes;
import fr.hugman.mubble.world.power_up.action.PowerUpActionTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
    public static final String MOD_ID = "mubble";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(MubbleEntityTypes.class);
        Reflection.initialize(MubbleDataComponents.class);
        Reflection.initialize(MubbleCriteriaTriggers.class);
        Reflection.initialize(MubbleConsumeEffectTypes.class);
        Reflection.initialize(MubbleAttributeTypes.class);

        Reflection.initialize(PowerUpActionTypes.class);

        MubbleBuiltInRegistries.register();

        MubblePayloadTypes.registerTypes();
        MubbleServerReceivers.register();
        MubbleCommands.register();
        VoyageSessions.register();
        EnvironmentController.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
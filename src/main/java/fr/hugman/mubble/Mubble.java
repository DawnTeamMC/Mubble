package fr.hugman.mubble;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.block.entity.MubbleBlockEntityTypes;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.command.MubbleCommands;
import fr.hugman.mubble.component.MubbleDataComponentTypes;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.item_group.MubbleItemGroups;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.item.consume.MubbleConsumeEffectTypes;
import fr.hugman.mubble.network.MubbleServerReceivers;
import fr.hugman.mubble.network.payload.MubblePayloads;
import fr.hugman.mubble.power_up.action.PowerUpActionTypes;
import fr.hugman.mubble.registry.MubbleRegistries;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.world.MubbleBiomeModifications;
import fr.hugman.mubble.world.level.gamerules.MubbleGameRules;
import fr.hugman.mubble.world.attribute.MubbleAttributeTypes;
import fr.hugman.mubble.world.attribute.MubbleEnvironmentAttributes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
    public static final String MOD_ID = "mubble";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(MubbleBlocks.class);
        Reflection.initialize(MubbleBlockEntityTypes.class);
        Reflection.initialize(MubbleDataComponentTypes.class);
        Reflection.initialize(MubbleItems.class);
        Reflection.initialize(MubbleSounds.class);
        Reflection.initialize(MubbleScreenHandlerTypes.class);
        Reflection.initialize(MubbleConsumeEffectTypes.class);
        Reflection.initialize(MubbleGameRules.class);
        Reflection.initialize(MubbleAttributeTypes.class);
        Reflection.initialize(MubbleEnvironmentAttributes.class);
        MubbleEntityTypes.registerAttributes();

        MubbleItemGroups.appendItemGroups();

        Reflection.initialize(PowerUpActionTypes.class);

        MubbleRegistries.register();

        MubblePayloads.registerTypes();
        MubbleServerReceivers.register();
        MubbleCommands.register();

        MubbleBiomeModifications.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
package fr.hugman.mubble;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.block.MubbleBlockEntityTypes;
import fr.hugman.mubble.block.MubbleBlocks;
import fr.hugman.mubble.command.MubbleCommands;
import fr.hugman.mubble.entity.MubbleEntityTypes;
import fr.hugman.mubble.item.MubbleItemGroups;
import fr.hugman.mubble.item.MubbleItems;
import fr.hugman.mubble.item.consume.MubbleConsumeEffectTypes;
import fr.hugman.mubble.network.MubbleServerReceivers;
import fr.hugman.mubble.network.payload.MubblePayloads;
import fr.hugman.mubble.power_up.action.PowerUpActionTypes;
import fr.hugman.mubble.registry.MubbleRegistries;
import fr.hugman.mubble.screen.MubbleScreenHandlerTypes;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.world.MubbleGamerules;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mubble implements ModInitializer {
    public static final String MOD_ID = "mubble";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(MubbleBlocks.class);
        Reflection.initialize(MubbleBlockEntityTypes.class);
        Reflection.initialize(MubbleItems.class);
        Reflection.initialize(MubbleSounds.class);
        Reflection.initialize(MubbleScreenHandlerTypes.class);
        Reflection.initialize(MubbleConsumeEffectTypes.class);
        MubbleEntityTypes.registerAttributes();

        MubbleItemGroups.appendItemGroups();

        MubbleGamerules.init();

        Reflection.initialize(PowerUpActionTypes.class);

        MubbleRegistries.register();

        MubblePayloads.registerTypes();
        MubbleServerReceivers.register();
        MubbleCommands.register();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
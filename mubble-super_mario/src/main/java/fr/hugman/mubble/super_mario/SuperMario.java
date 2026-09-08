package fr.hugman.mubble.super_mario;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.super_mario.commands.SuperMarioCommands;
import fr.hugman.mubble.super_mario.core.component.SuperMarioDataComponents;
import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import fr.hugman.mubble.super_mario.core.attachment.SuperMarioAttachmentTypes;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioBuiltInRegistries;
import fr.hugman.mubble.super_mario.network.protocol.SuperMarioServerReceivers;
import fr.hugman.mubble.super_mario.network.protocol.common.custom.SuperMarioPayloadTypes;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityEvents;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.freeze.FreezeEvents;
import fr.hugman.mubble.super_mario.world.inventory.SuperMarioMenuTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioCreativeModeTabs;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.biome.SuperMarioBiomeModifications;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.super_mario.world.level.gamerules.SuperMarioGameRules;
import fr.hugman.mubble.super_mario.world.power_up.action.SuperMarioPowerUpActionTypes;
import fr.hugman.mubble.world.power_up.action.PowerUpActionTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuperMario implements ModInitializer {
    public static final String MOD_ID = "super_mario";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(SuperMarioBlocks.class);
        Reflection.initialize(SuperMarioBlockEntityTypes.class);
        Reflection.initialize(SuperMarioEntityTypes.class);
        Reflection.initialize(SuperMarioDataComponents.class);
        Reflection.initialize(SuperMarioItems.class);
        Reflection.initialize(SuperMarioSounds.class);
        Reflection.initialize(SuperMarioMenuTypes.class);
        Reflection.initialize(SuperMarioGameRules.class);
        Reflection.initialize(SuperMarioPowerUpActionTypes.class);

        Reflection.initialize(SuperMarioParticleTypes.class);
        Reflection.initialize(SuperMarioEnvironmentAttributes.class);
        Reflection.initialize(SuperMarioAttachmentTypes.class);
        SuperMarioEntityTypes.registerAttributes();

        SuperMarioCreativeModeTabs.appendItemGroups();

        Reflection.initialize(PowerUpActionTypes.class);

        SuperMarioBuiltInRegistries.register();

        SuperMarioBiomeModifications.register();

        SuperMarioPayloadTypes.registerTypes();
        SuperMarioServerReceivers.register();

        SuperMarioCommands.register();

        // Events
        SuperMarioEntityEvents.registerListeners();
        FreezeEvents.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

package fr.hugman.mubble.test.unit.support;

import com.google.common.reflect.Reflection;
import fr.hugman.mubble.core.component.MubbleDataComponents;
import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import fr.hugman.mubble.super_mario.core.component.SuperMarioDataComponents;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.inventory.SuperMarioMenuTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import fr.hugman.mubble.super_mario.world.power_up.action.SuperMarioPowerUpActionTypes;
import fr.hugman.mubble.world.attribute.MubbleAttributeTypes;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.item.consume_effects.MubbleConsumeEffectTypes;
import fr.hugman.mubble.world.power_up.action.PowerUpActionTypes;

/**
 * Runs the static initialisers that fill the built-in registries, exactly like the mod initialisers
 * do. Without this, a registry looks empty and every check below it passes for the wrong reason.
 * <p>
 * The order matters where one holder class reads another: block entity types name their blocks, and
 * items point at entity types for the spawn eggs.
 */
public final class Registrations {
    private static boolean done;

    private Registrations() {
    }

    public static synchronized void registerEverything() {
        if (done) {
            return;
        }
        TestBootstrap.bootstrap();

        Reflection.initialize(MubbleEntityTypes.class);
        Reflection.initialize(MubbleDataComponents.class);
        Reflection.initialize(MubbleConsumeEffectTypes.class);
        Reflection.initialize(MubbleAttributeTypes.class);
        Reflection.initialize(PowerUpActionTypes.class);

        Reflection.initialize(SuperMarioBlocks.class);
        Reflection.initialize(SuperMarioBlockEntityTypes.class);
        Reflection.initialize(SuperMarioEntityTypes.class);
        Reflection.initialize(SuperMarioDataComponents.class);
        Reflection.initialize(SuperMarioItems.class);
        Reflection.initialize(SuperMarioSounds.class);
        Reflection.initialize(SuperMarioMenuTypes.class);
        Reflection.initialize(SuperMarioParticleTypes.class);
        Reflection.initialize(SuperMarioPowerUpActionTypes.class);

        done = true;
    }
}

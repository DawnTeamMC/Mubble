package fr.hugman.mubble.super_mario.world.entity.item;

import fr.hugman.mubble.sounds.SoundConfig;
import fr.hugman.mubble.super_mario.core.particles.SuperMarioParticleTypes;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Cosmetics shared by every way a Super Mario collectible can come to life (placed by hand, bumped out of a
 * block, left behind by a popping bubble...).
 */
public final class SuperMarioCollectibles {
    private SuperMarioCollectibles() {
    }

    public static void configure(CollectibleEntity entity, ItemStack stack) {
        entity.setCollectSound(collectSound());
        entity.setBounceSound(new SoundConfig(SuperMarioSounds.COIN_BOUNCE, 1.0f, 1.0f));
        entity.setCollectParticle(sparkle(stack));
    }

    public static SoundConfig collectSound() {
        return new SoundConfig(SuperMarioSounds.COIN_COLLECT, 0.2f, 1.0f);
    }

    @Nullable
    public static ParticleOptions sparkle(ItemStack stack) {
        if (stack.is(SuperMarioItems.COIN)) {
            return SuperMarioParticleTypes.COIN_SPARKLE;
        }
        if (stack.is(SuperMarioItems.RED_COIN)) {
            return SuperMarioParticleTypes.RED_COIN_SPARKLE;
        }
        if (stack.is(SuperMarioItems.BLUE_COIN)) {
            return SuperMarioParticleTypes.BLUE_COIN_SPARKLE;
        }
        if (stack.is(SuperMarioItems.FLOWER_COIN)) {
            return SuperMarioParticleTypes.FLOWER_COIN_SPARKLE;
        }
        return null;
    }
}

package fr.hugman.mubble.super_mario.core.registries;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class SuperMarioRegistries {
    public static final ResourceKey<Registry<GoombaVariant>> GOOMBA_VARIANT = createRegistryKey("goomba_variant");

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(SuperMario.id(path));
    }
}

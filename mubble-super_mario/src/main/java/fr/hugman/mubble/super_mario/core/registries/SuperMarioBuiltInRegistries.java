package fr.hugman.mubble.super_mario.core.registries;

import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class SuperMarioBuiltInRegistries {
    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.create(key).buildAndRegister();
    }

    public static void register() {
        DynamicRegistries.registerSynced(SuperMarioRegistries.GOOMBA_VARIANT, GoombaVariant.DIRECT_CODEC);
    }
}

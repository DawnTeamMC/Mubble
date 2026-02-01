package fr.hugman.mubble.super_mario.references;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.core.registries.SuperMarioRegistries;
import fr.hugman.mubble.super_mario.world.entity.monster.goomba.GoombaVariant;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GoombaVariantKeys {
    public static final ResourceKey<GoombaVariant> NORMAL = createKey("normal");
    public static final ResourceKey<GoombaVariant> MINI = createKey("mini");

    private static ResourceKey<GoombaVariant> createKey(String path) {
        return createKey(SuperMario.id(path));
    }

    public static ResourceKey<GoombaVariant> createKey(Identifier id) {
        return ResourceKey.create(SuperMarioRegistries.GOOMBA_VARIANT, id);
    }
}
package fr.hugman.mubble.references;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.entity.monster.goomba.GoombaVariant;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GoombaVariantKeys {
    public static final ResourceKey<GoombaVariant> NORMAL = createKey("normal");
    public static final ResourceKey<GoombaVariant> MINI = createKey("mini");

    private static ResourceKey<GoombaVariant> createKey(String path) {
        return createKey(Mubble.id(path));
    }

    public static ResourceKey<GoombaVariant> createKey(Identifier id) {
        return ResourceKey.create(MubbleRegistries.GOOMBA_VARIANT, id);
    }
}
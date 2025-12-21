package fr.hugman.mubble.world.entity.monster.goomba;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GoombaVariants {
    public static final ResourceKey<GoombaVariant> NORMAL = of("normal");
    public static final ResourceKey<GoombaVariant> MINI = of("mini");

    private static ResourceKey<GoombaVariant> of(String path) {
        return of(Mubble.id(path));
    }

    public static ResourceKey<GoombaVariant> of(Identifier id) {
        return ResourceKey.create(MubbleRegistries.GOOMBA_VARIANT, id);
    }
}
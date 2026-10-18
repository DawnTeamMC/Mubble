package fr.hugman.mubble.zelda.references;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.references.BlockItemId;

public class ZeldaBlockItemIds {
    public static final BlockItemId LOFTWING_EGG = createKey("loftwing_egg");
    public static final BlockItemId CRACKED_LOFTWING_EGG = createKey("cracked_loftwing_egg");
    public static final BlockItemId HATCHING_LOFTWING_EGG = createKey("hatching_loftwing_egg");

    private static BlockItemId createKey(String path) {
        var id = Zelda.id(path);
        return BlockItemId.create(id, id);
    }
}

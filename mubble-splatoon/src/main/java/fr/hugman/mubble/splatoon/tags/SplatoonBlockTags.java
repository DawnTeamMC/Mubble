package fr.hugman.mubble.splatoon.tags;

import fr.hugman.mubble.splatoon.Splatoon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SplatoonBlockTags {
    /**
     * Blocks that ink does not stick to.
     */
    public static final TagKey<Block> UNINKABLE = create("uninkable");
    /**
     * Blocks without collision that ink covers as a whole, over their own texture, rather than on the faces of a
     * grid: grass, flowers, vines... They do not keep ink off the surface they stand on either.
     */
    public static final TagKey<Block> INK_COATED = create("ink_coated");

    private static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, Splatoon.id(path));
    }
}

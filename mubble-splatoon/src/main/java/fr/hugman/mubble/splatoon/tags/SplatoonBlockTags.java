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

    private static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, Splatoon.id(path));
    }
}

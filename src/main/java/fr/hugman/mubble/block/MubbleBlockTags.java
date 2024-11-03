package fr.hugman.mubble.block;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleBlockTags {
    public static final TagKey<Block> MELTABLE_TO_AIR = of("meltable/air");
    public static final TagKey<Block> MELTABLE_TO_ICE = of("meltable/ice");
    public static final TagKey<Block> MELTABLE_TO_WATER = of("meltable/water");

    private static TagKey<Block> of(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Mubble.id(path));
    }
}

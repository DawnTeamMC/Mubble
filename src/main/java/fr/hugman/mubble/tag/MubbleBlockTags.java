package fr.hugman.mubble.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MubbleBlockTags {
    public static final TagKey<Block> BEANSTALK_PLANTABLE_ON = TagKey.of(RegistryKeys.BLOCK, new Identifier("mubble", "beanstalk_plantable_on"));
}

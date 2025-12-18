package fr.hugman.mubble.world.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.Optional;

public record BlockTransform(
        TagPredicate<Block> predicate, //TODO switch for a generic block predicate
        BlockState result,
        Optional<RegistryEntry<SoundEvent>> sound
) {
    public static final Codec<BlockTransform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagPredicate.createCodec(RegistryKeys.BLOCK).fieldOf("predicate").forGetter(BlockTransform::predicate),
            BlockState.CODEC.fieldOf("result").forGetter(BlockTransform::result),
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(BlockTransform::sound)
    ).apply(instance, BlockTransform::new));

    public static BlockTransform testList(List<BlockTransform> list, RegistryEntry<Block> block) {
        for (BlockTransform transform : list) {
            if (transform.predicate.test(block)) {
                return transform;
            }
        }
        return null;
    }
}

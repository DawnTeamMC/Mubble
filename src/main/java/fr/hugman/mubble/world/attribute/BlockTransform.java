package fr.hugman.mubble.world.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record BlockTransform(
        TagPredicate<Block> predicate, //TODO switch for a generic block predicate
        BlockState result,
        Optional<Holder<SoundEvent>> sound
) {
    public static final Codec<BlockTransform> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagPredicate.codec(Registries.BLOCK).fieldOf("predicate").forGetter(BlockTransform::predicate),
            BlockState.CODEC.fieldOf("result").forGetter(BlockTransform::result),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(BlockTransform::sound)
    ).apply(instance, BlockTransform::new));

    public static BlockTransform testList(List<BlockTransform> list, Holder<Block> block) {
        for (BlockTransform transform : list) {
            if (transform.predicate.matches(block)) {
                return transform;
            }
        }
        return null;
    }
}

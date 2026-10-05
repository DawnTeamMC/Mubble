package fr.hugman.mubble.arcade.access;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * A move, or a tag of moves, written {@code mubble:backflip} or {@code #mubble:aerial}.
 * <p>
 * A tag is kept as a tag rather than expanded when read, so that a source naming
 * {@code #mubble:aerial} also covers the aerial moves a data pack adds to that tag later on.
 */
public record MoveSelector(Either<ResourceKey<ArcadeMove>, TagKey<ArcadeMove>> value) {
    public static final Codec<MoveSelector> CODEC = Codec.STRING.comapFlatMap(MoveSelector::parse, MoveSelector::toString);
    public static final StreamCodec<ByteBuf, MoveSelector> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            string -> parse(string).getOrThrow(),
            MoveSelector::toString
    );

    public static MoveSelector move(ResourceKey<ArcadeMove> key) {
        return new MoveSelector(Either.left(key));
    }

    public static MoveSelector tag(TagKey<ArcadeMove> tag) {
        return new MoveSelector(Either.right(tag));
    }

    public static DataResult<MoveSelector> parse(String string) {
        boolean isTag = string.startsWith("#");
        var idString = isTag ? string.substring(1) : string;
        return Identifier.read(idString).map(id -> isTag
                ? tag(TagKey.create(ArcadeRegistries.ARCADE_MOVE, id))
                : move(ResourceKey.create(ArcadeRegistries.ARCADE_MOVE, id)));
    }

    /** Hands every move this selector currently covers to {@code consumer}. An unknown move or tag covers nothing. */
    public void forEachMove(Consumer<ArcadeMove> consumer) {
        this.value.ifLeft(key -> ArcadeBuiltInRegistries.ARCADE_MOVE.getOptional(key).ifPresent(consumer))
                .ifRight(tag -> ArcadeBuiltInRegistries.ARCADE_MOVE.get(tag).ifPresent(set -> set.forEach(holder -> consumer.accept(holder.value()))));
    }

    @Override
    public String toString() {
        return this.value.map(key -> key.identifier().toString(), tag -> "#" + tag.location());
    }
}

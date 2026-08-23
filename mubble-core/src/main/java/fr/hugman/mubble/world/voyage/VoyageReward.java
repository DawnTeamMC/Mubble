package fr.hugman.mubble.world.voyage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Something a voyage hands over on completion.
 *
 * <pre>{@code
 * { "item": "minecraft:carrot", "count": 1 }
 * }</pre>
 *
 * <p><strong>Not an {@link ItemStack}.</strong> {@code ItemStack.CODEC} resolves the item's default
 * components, and those are not bound yet while dynamic registries load — reading one here fails
 * with "Item minecraft:carrot does not have components yet" and takes the whole data pack down with
 * it. An item and a count are all a reward needs to name anyway, and they can be turned into a stack
 * whenever one is actually being given out.
 *
 * <p>Handing rewards out is phase 5. This exists now so that the content written for phase 2 does
 * not have to be rewritten then.
 */
public record VoyageReward(Item item, int count) {
    public static final Codec<VoyageReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(VoyageReward::item),
            Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(VoyageReward::count)
    ).apply(instance, VoyageReward::new));

    public ItemStack toStack() {
        return new ItemStack(this.item, this.count);
    }
}

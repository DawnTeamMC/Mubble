package fr.hugman.mubble.world.voyage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
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
 */
public record VoyageReward(Item item, int count) {
    public static final Codec<VoyageReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(VoyageReward::item),
            Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(VoyageReward::count)
    ).apply(instance, VoyageReward::new));

    public ItemStack toStack() {
        return new ItemStack(this.item, this.count);
    }

    /**
     * Gives this to {@code player}, dropping at their feet whatever will not fit.
     *
     * <p>Split by stack size the way {@code /give} does, because a reward is allowed to name more
     * than one stack's worth and {@code Inventory#add} of an oversized stack is not a thing.
     *
     * <p>Anything dropped has no pickup delay and is aimed at the player, so a full inventory costs
     * them a moment rather than the reward.
     *
     * <p>Nothing is dropped for a player with infinite materials, and that is vanilla's doing rather
     * than a case worth handling: {@code Inventory#add} deletes an overflow outright for them and
     * reports success. Dropping it as well would hand a creative player items they did not fit.
     */
    public void grantTo(ServerPlayer player) {
        int maxStackSize = new ItemStack(this.item).getMaxStackSize();
        int remaining = this.count;

        while (remaining > 0) {
            int size = Math.min(maxStackSize, remaining);
            remaining -= size;

            ItemStack stack = new ItemStack(this.item, size);
            player.getInventory().add(stack);
            if (!stack.isEmpty()) {
                ItemEntity dropped = player.drop(stack, false);
                if (dropped != null) {
                    dropped.setNoPickUpDelay();
                    dropped.setTarget(player.getUUID());
                }
            }
        }

        player.containerMenu.broadcastChanges();
    }
}

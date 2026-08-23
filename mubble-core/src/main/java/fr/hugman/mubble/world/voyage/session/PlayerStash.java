package fr.hugman.mubble.world.voyage.session;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Everything a voyage takes away from a player, and gives back.
 *
 * <p>A voyage runs the player with an empty inventory in a level that is deleted afterwards, so
 * whatever they walked in with has to live somewhere else for the duration. That somewhere is this,
 * held in {@link VoyageSessionData} and written to disk, because the one way this can go badly wrong
 * is losing somebody's inventory.
 *
 * <p><strong>The ender chest is deliberately not here.</strong> It is not in the level being deleted
 * and nothing about a voyage touches it, so stashing it would only add a way to lose it.
 *
 * @param inventory        every occupied slot, armour and offhand included
 * @param selectedSlot     the hotbar slot that was in hand
 * @param effects          active effects, cleared on entry
 * @param attributes       attribute base values and modifiers, so nothing a voyage grants leaks out
 * @param gameMode         restored so a voyage can put the player in adventure without stranding them there
 * @param health           restored exactly; a voyage is not a way to heal
 * @param foodLevel        hunger, likewise
 * @param saturation       the hidden half of hunger, restored so eating behaves the same afterwards
 * @param totalExperience  the total, not the level: level and progress are derived back from it exactly
 * @param returnDimension  where they were
 * @param returnPos        exactly where, so this is not a respawn
 * @param returnYRot       and which way they were facing
 * @param returnXRot       and how far up
 */
public record PlayerStash(
        List<ItemStackWithSlot> inventory,
        int selectedSlot,
        List<MobEffectInstance> effects,
        List<AttributeInstance.Packed> attributes,
        GameType gameMode,
        float health,
        int foodLevel,
        float saturation,
        int totalExperience,
        ResourceKey<Level> returnDimension,
        Vec3 returnPos,
        float returnYRot,
        float returnXRot
) {
    public static final Codec<PlayerStash> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStackWithSlot.CODEC.listOf().optionalFieldOf("inventory", List.of()).forGetter(PlayerStash::inventory),
            Codec.INT.optionalFieldOf("selected_slot", 0).forGetter(PlayerStash::selectedSlot),
            MobEffectInstance.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(PlayerStash::effects),
            AttributeInstance.Packed.LIST_CODEC.optionalFieldOf("attributes", List.of()).forGetter(PlayerStash::attributes),
            GameType.CODEC.fieldOf("game_mode").forGetter(PlayerStash::gameMode),
            Codec.FLOAT.fieldOf("health").forGetter(PlayerStash::health),
            Codec.INT.fieldOf("food_level").forGetter(PlayerStash::foodLevel),
            Codec.FLOAT.fieldOf("saturation").forGetter(PlayerStash::saturation),
            Codec.INT.fieldOf("total_experience").forGetter(PlayerStash::totalExperience),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("return_dimension").forGetter(PlayerStash::returnDimension),
            Vec3.CODEC.fieldOf("return_pos").forGetter(PlayerStash::returnPos),
            Codec.FLOAT.fieldOf("return_y_rot").forGetter(PlayerStash::returnYRot),
            Codec.FLOAT.fieldOf("return_x_rot").forGetter(PlayerStash::returnXRot)
    ).apply(instance, PlayerStash::new));

    /** {@return everything {@code player} is currently carrying and standing on} */
    public static PlayerStash of(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        List<ItemStackWithSlot> items = new ArrayList<>();
        // getItem covers the equipment slots as well as the main 36, so armour, offhand, body and
        // saddle all come along without being enumerated separately.
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
                items.add(new ItemStackWithSlot(slot, stack.copy()));
            }
        }

        return new PlayerStash(
                List.copyOf(items),
                inventory.getSelectedSlot(),
                player.getActiveEffects().stream().map(MobEffectInstance::new).toList(),
                player.getAttributes().pack(),
                player.gameMode(),
                player.getHealth(),
                player.getFoodData().getFoodLevel(),
                player.getFoodData().getSaturationLevel(),
                player.totalExperience,
                player.level().dimension(),
                player.position(),
                player.getYRot(),
                player.getXRot()
        );
    }

    /**
     * Empties {@code player} out, ready to enter a voyage.
     *
     * <p>Separate from {@link #of} on purpose: taking the stash has to succeed before anything is
     * destroyed, so a failure between the two leaves the player untouched rather than empty.
     */
    public static void clear(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            inventory.setItem(slot, ItemStack.EMPTY);
        }
        inventory.setSelectedSlot(0);

        player.removeAllEffects();
        player.setHealth(player.getMaxHealth());
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5.0F);
        player.setExperienceLevels(0);
        player.setExperiencePoints(0);
        player.totalExperience = 0;
        player.containerMenu.broadcastChanges();
    }

    /**
     * Puts {@code player} back exactly as they were, and returns them to where they were standing.
     *
     * <p>Safe to call on a player who is not in a voyage level — recovery after a crash does exactly
     * that, since the level they were in no longer exists.
     */
    public void restoreTo(ServerPlayer player) {
        ServerLevel level = player.level().getServer().getLevel(this.returnDimension);
        if (level == null) {
            // Only reachable if the return dimension itself went away — a data pack removing a
            // dimension, say. The overworld is wrong, but it is somewhere, and losing the inventory
            // as well would make a bad day worse.
            level = player.level().getServer().overworld();
            Mubble.LOGGER.error("Return dimension {} no longer exists; returning {} to the overworld instead",
                    this.returnDimension.identifier(), player.getPlainTextName());
        }
        player.teleportTo(level, this.returnPos.x(), this.returnPos.y(), this.returnPos.z(),
                Set.of(), this.returnYRot, this.returnXRot, false);

        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            inventory.setItem(slot, ItemStack.EMPTY);
        }
        for (ItemStackWithSlot item : this.inventory) {
            if (item.isValidInContainer(inventory.getContainerSize())) {
                inventory.setItem(item.slot(), item.stack());
            }
        }
        inventory.setSelectedSlot(this.selectedSlot);

        player.removeAllEffects();
        for (MobEffectInstance effect : this.effects) {
            player.addEffect(new MobEffectInstance(effect));
        }

        player.getAttributes().apply(this.attributes);
        player.setGameMode(this.gameMode);
        player.setHealth(this.health);
        player.getFoodData().setFoodLevel(this.foodLevel);
        player.getFoodData().setSaturation(this.saturation);

        // Rebuilt from the total rather than restoring level and progress separately, because the
        // two have to agree and the total is the only one of the three that is authoritative.
        player.setExperienceLevels(0);
        player.setExperiencePoints(0);
        player.totalExperience = 0;
        player.giveExperiencePoints(this.totalExperience);

        player.containerMenu.broadcastChanges();
    }
}

package fr.hugman.mubble.world.voyage.session;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

/**
 * Everything a voyage takes away from a player, and gives back.
 *
 * <p>A voyage runs the player empty-handed in a level that is deleted afterwards, so whatever they
 * walked in with has to live somewhere else for the duration. That somewhere is this, held in
 * {@link VoyageSessionData} and written to disk, because the one way this can go badly wrong is
 * losing somebody's inventory.
 *
 * <h2>Why this is the player's whole save tag</h2>
 *
 * <p>The first version enumerated fields — inventory, effects, attributes, hunger, and so on. That
 * is wrong by construction: it restores exactly the list somebody thought of, and silently ignores
 * everything else. Mubble's own power-up survived a voyage that way, and any other mod attaching
 * state to a player would too.
 *
 * <p>So the stash is {@code saveWithoutId} — the same serialisation the game uses to write a player
 * to disk. Anything a mod persists through {@code addAdditionalSaveData}, which is the normal way to
 * do it, comes along without this class knowing the mod exists.
 *
 * <p><strong>Except the ender chest.</strong> It is not in the level being deleted, nothing about a
 * voyage reads it, and rolling it back would delete anything the player put in during one. Dropping
 * its key from the snapshot is <em>not</em> enough on its own — {@code Player} reads it with
 * {@code listOrEmpty}, so an absent key empties the chest rather than leaving it be. The key is
 * dropped on the way in and the player's current contents are written back into the tag on the way
 * out, which is the only combination that actually leaves it alone.
 *
 * @param data            the player's save tag, taken on entry, minus the ender chest
 * @param returnDimension where they were
 * @param returnPos       exactly where, so this is not a respawn
 * @param returnYRot      and which way they were facing
 * @param returnXRot      and how far up
 */
public record PlayerStash(
        CompoundTag data,
        ResourceKey<Level> returnDimension,
        Vec3 returnPos,
        float returnYRot,
        float returnXRot
) {
    private static final Logger PROBLEMS = LogUtils.getLogger();

    /** {@link net.minecraft.world.entity.player.Player} writes the ender chest under this key. */
    private static final String ENDER_CHEST_KEY = "EnderItems";

    public static final Codec<PlayerStash> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CompoundTag.CODEC.fieldOf("data").forGetter(PlayerStash::data),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("return_dimension").forGetter(PlayerStash::returnDimension),
            Vec3.CODEC.fieldOf("return_pos").forGetter(PlayerStash::returnPos),
            Codec.FLOAT.fieldOf("return_y_rot").forGetter(PlayerStash::returnYRot),
            Codec.FLOAT.fieldOf("return_x_rot").forGetter(PlayerStash::returnXRot)
    ).apply(instance, PlayerStash::new));

    /** {@return everything {@code player} is, and where they are standing} */
    public static PlayerStash of(ServerPlayer player) {
        try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(PROBLEMS)) {
            TagValueOutput output = TagValueOutput.createWithContext(problems, player.registryAccess());
            player.saveWithoutId(output);

            CompoundTag data = output.buildResult();
            data.remove(ENDER_CHEST_KEY);
            return new PlayerStash(data, player.level().dimension(), player.position(), player.getYRot(), player.getXRot());
        }
    }

    /**
     * Empties {@code player} out, ready to enter a voyage.
     *
     * <p>Separate from {@link #of} on purpose: taking the stash has to succeed before anything is
     * destroyed, so a failure between the two leaves the player untouched rather than empty.
     *
     * <p><strong>This half cannot be generic, and that is the honest limit of the design.</strong>
     * Restoring works from a saved tag, so it covers anything that was there. Clearing has to know
     * what to reset, and loading a stripped tag does not do it: the usual way to read persisted state
     * is {@code input.read(key).ifPresent(...)}, so an absent key leaves the field exactly as it was.
     * Mubble's own power-up reads that way, and most mods will too.
     *
     * <p>So this resets vanilla's player state and Mubble's, and a third party's state will survive
     * into a trial. It will still be put back correctly on the way out, which is the direction that
     * matters: nothing gained inside a voyage escapes it.
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
        player.setRemainingFireTicks(0);
        player.clearFire();
        player.fallDistance = 0.0D;
        player.setDeltaMovement(Vec3.ZERO);

        ((PowerUpHolder) player).clearPowerUp();
        ((PowerUpHolder) player).setPowerUpProperties(null);

        player.containerMenu.broadcastChanges();
    }

    /**
     * {@return the snapshot with the player's <em>current</em> ender chest written into it}
     *
     * <p>Loading a tag with no {@code EnderItems} empties the chest, so leaving it alone means
     * putting today's contents into yesterday's tag rather than omitting the key.
     */
    private CompoundTag withCurrentEnderChest(ServerPlayer player) {
        try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(PROBLEMS)) {
            TagValueOutput output = TagValueOutput.createWithContext(problems, player.registryAccess());
            player.getEnderChestInventory().storeAsSlots(output.list(ENDER_CHEST_KEY, ItemStackWithSlot.CODEC));

            CompoundTag merged = this.data.copy();
            CompoundTag enderChest = output.buildResult();
            if (enderChest.contains(ENDER_CHEST_KEY)) {
                merged.put(ENDER_CHEST_KEY, enderChest.get(ENDER_CHEST_KEY));
            }
            return merged;
        }
    }

    /**
     * Puts {@code player} back exactly as they were, and returns them to where they were standing.
     *
     * <p>Clears first and loads second, which matters more than it looks. Loading alone would not
     * undo a power-up picked up <em>during</em> the voyage, because the snapshot has no key for one
     * and the reader ignores absent keys. Clearing first means the snapshot is applied to a blank
     * player rather than layered over whatever the voyage left behind.
     *
     * <p>Safe to call on a player who is not in a voyage level — recovery after a crash does exactly
     * that, since the level they were in no longer exists.
     */
    public void restoreTo(ServerPlayer player) {
        ServerLevel level = player.level().getServer().getLevel(this.returnDimension);
        if (level == null) {
            // Only reachable if the return dimension itself went away — a data pack removing one,
            // say. The overworld is wrong, but it is somewhere, and losing the inventory as well
            // would make a bad day worse.
            level = player.level().getServer().overworld();
            Mubble.LOGGER.error("Return dimension {} no longer exists; returning {} to the overworld instead",
                    this.returnDimension.identifier(), player.getPlainTextName());
        }

        clear(player);
        // Moved before the load so that the load's own position write lands in the level the player
        // is actually in, rather than briefly placing them at overworld coordinates inside a trial.
        player.teleportTo(level, this.returnPos.x(), this.returnPos.y(), this.returnPos.z(),
                Set.of(), this.returnYRot, this.returnXRot, false);

        try (ProblemReporter.ScopedCollector problems = new ProblemReporter.ScopedCollector(PROBLEMS)) {
            player.load(TagValueInput.create(problems, player.registryAccess(), this.withCurrentEnderChest(player)));
        }

        // Effects are the one thing a load does not tell the client about; everything else here
        // either lives in synched entity data or is resent by the next player tick.
        player.level().getServer().getPlayerList().sendActivePlayerEffects(player);
        player.containerMenu.broadcastChanges();
    }
}

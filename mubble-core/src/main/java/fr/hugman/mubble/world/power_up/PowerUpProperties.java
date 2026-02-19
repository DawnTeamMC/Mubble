package fr.hugman.mubble.world.power_up;

import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class PowerUpProperties {
    private boolean dirty;

    public ChargeCounting chargeCounting = ChargeCounting.FROM_ACTIVE_ENTITIES;
    public int maxCharges;

    private int cooldown;
    private int chargeCount;
    private final List<UUID> chargeEntities;

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpProperties> STREAM_CODEC = StreamCodec.composite(
            ChargeCounting.STREAM_CODEC, p -> p.chargeCounting,
            ByteBufCodecs.INT, p -> p.maxCharges,
            ByteBufCodecs.INT, p -> p.cooldown,
            ByteBufCodecs.INT, p -> p.chargeCount,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), p -> p.chargeEntities,
            PowerUpProperties::new
    );

    public PowerUpProperties(int cooldown, List<UUID> chargeEntities) {
        this.cooldown = cooldown;
        this.chargeEntities = chargeEntities;
    }

    public PowerUpProperties(
            ChargeCounting chargeCounting,
            int maxCharges,
            int cooldown,
            int chargeCount,
            List<UUID> chargeEntities
    ) {
        this.chargeCounting = chargeCounting;
        this.maxCharges = maxCharges;
        this.cooldown = cooldown;
        this.chargeCount = chargeCount;
        this.chargeEntities = chargeEntities;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        this.dirty = true;
    }

    public int getChargeCount() {
        return this.chargeCount;
    }

    public boolean checkDirty() {
        if(this.dirty) {
            this.dirty = false;
            return true;
        }
        return false;
    }

    public void addEntity(UUID uuid) {
        this.chargeEntities.add(uuid);
        this.chargeCount--;
        this.dirty = true;
    }

    public void reset() {
        this.chargeCounting = ChargeCounting.NONE;
        clear();
    }

    public void clear() {
        this.cooldown = 0;
        this.chargeCount = this.maxCharges;
        this.chargeEntities.clear();
        this.dirty = true;
    }

    public void tick() {
        if(this.chargeCounting == ChargeCounting.FROM_ACTIVE_ENTITIES) {
            this.chargeCount = this.maxCharges - this.chargeEntities.size();
        }
        if(this.cooldown > 0) {
            this.cooldown--;
            if(this.cooldown == 0 && this.chargeCounting == ChargeCounting.COOLDOWN_RECHARGE) {
                this.chargeCount++;
            }
            this.dirty = true;
        }
    }

    /**
     * Checks that happen every second in case there is something going wrong that could lock the player away from using their power-up.
     */
    public void doSoftChecks(Player player) {
        this.removeInvalidEntities(player.level());
    }

    /**
     * Refreshes the projectiles list by removing invalid projectiles.
     */
    public void removeInvalidEntities(Level level) {
        if (this.chargeEntities.removeIf(uuid -> level.getEntity(uuid) == null)) {
            this.dirty = true;
        }
    }

    public void removeEntity(UUID uuid) {
        if (this.chargeEntities.remove(uuid)) {
            this.dirty = true;
        }
    }

    public enum ChargeCounting {
        NONE,
        FROM_ACTIVE_ENTITIES, // based on the number of active entities tied to the power-up trigger
        ONLY_DECREASE,        // never increases once the power-up is triggered
        COOLDOWN_RECHARGE;    // charges up once the cooldown is over

        public static final IntFunction<ChargeCounting> BY_ID = ByIdMap.continuous(ChargeCounting::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, ChargeCounting> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChargeCounting::ordinal);
    }
}

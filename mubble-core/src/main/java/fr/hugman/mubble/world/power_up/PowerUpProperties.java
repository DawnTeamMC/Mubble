package fr.hugman.mubble.world.power_up;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

public final class PowerUpProperties {
    private boolean dirty;

    public ChargeCounting chargeCounting;
    public int maxCharges;

    private int cooldown;
    private int chargeCount;
    private final List<UUID> chargeEntities;

    public static final Codec<PowerUpProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChargeCounting.CODEC.fieldOf("charge_counting").forGetter(p -> p.chargeCounting),
            Codec.INT.fieldOf("max_charges").forGetter(p -> p.maxCharges),
            Codec.INT.fieldOf("cooldown").forGetter(p -> p.cooldown),
            Codec.INT.fieldOf("charge_count").forGetter(p -> p.chargeCount),
            Codec.list(UUIDUtil.CODEC).fieldOf("charge_entities").forGetter(p -> p.chargeEntities)
    ).apply(instance, PowerUpProperties::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpProperties> STREAM_CODEC = StreamCodec.composite(
            ChargeCounting.STREAM_CODEC, p -> p.chargeCounting,
            ByteBufCodecs.INT, p -> p.maxCharges,
            ByteBufCodecs.INT, p -> p.cooldown,
            ByteBufCodecs.INT, p -> p.chargeCount,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), p -> p.chargeEntities,
            PowerUpProperties::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<PowerUpProperties>> OPTIONAL_STREAM_CODEC = ByteBufCodecs.optional(STREAM_CODEC);

    public PowerUpProperties(
            ChargeCounting chargeCounting,
            int maxCharges
    ) {
        this(chargeCounting, maxCharges, 0, maxCharges, new ArrayList<>());
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
        this.chargeEntities = new ArrayList<>(chargeEntities);
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        this.dirty = true;
    }

    public int getChargeCount() {
        return this.chargeCount;
    }

    public boolean checkDirty() {
        if (this.dirty) {
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

    public void tick() {
        if (this.chargeCounting == ChargeCounting.FROM_ACTIVE_ENTITIES) {
            this.chargeCount = this.maxCharges - this.chargeEntities.size();
        }
        if (this.cooldown > 0) {
            this.cooldown--;
            if (this.cooldown == 0 && this.chargeCounting == ChargeCounting.COOLDOWN_RECHARGE) {
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

    public enum ChargeCounting implements StringRepresentable {
        NONE(0, "none"),
        FROM_ACTIVE_ENTITIES(1, "from_active_entities"), // based on the number of active entities tied to the power-up trigger
        ONLY_DECREASE(2, "only_decrease"),               // never increases once the power-up is triggered
        COOLDOWN_RECHARGE(3, "cooldown_recharge");       // charges up once the cooldown is over

        public static final IntFunction<ChargeCounting> BY_ID = ByIdMap.continuous(ChargeCounting::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final Codec<ChargeCounting> CODEC = StringRepresentable.fromEnum(ChargeCounting::values);
        public static final StreamCodec<ByteBuf, ChargeCounting> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChargeCounting::ordinal);

        private final int id;
        private final String name;

        ChargeCounting(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        public int id() {
            return this.id;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}

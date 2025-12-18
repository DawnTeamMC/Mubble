package fr.hugman.mubble.power_up;

import java.util.List;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class PowerUpProperties {
    private boolean dirty;
    private int cooldown;
    public final List<UUID> projectiles;

    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpProperties> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, powerUpProperties -> powerUpProperties.cooldown,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), powerUpProperties -> powerUpProperties.projectiles,
            PowerUpProperties::new
    );

    public PowerUpProperties(int cooldown, List<UUID> projectiles) {
        this.cooldown = cooldown;
        this.projectiles = projectiles;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public List<UUID> getProjectiles() {
        return projectiles;
    }

    public void reset() {
        this.cooldown = 0;
        this.projectiles.clear();
        this.dirty = true;
    }

    public boolean checkDirty() {
        if(this.dirty) {
            this.dirty = false;
            return true;
        }
        return false;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        this.dirty = true;
    }

    public void addProjectile(UUID uuid) {
        this.projectiles.add(uuid);
        this.dirty = true;
    }

    public void tick() {
        if(this.cooldown > 0) {
            this.cooldown--;
            this.dirty = true;
        }
    }

    /**
     * Checks that happen every second in case there is something going wrong that could lock the player away from using their power-up.
     */
    public void doSoftChecks(Player player) {
        this.removeInvalidProjectiles(player.level());
    }

    /**
     * Refreshes the projectiles list by removing invalid projectiles.
     */
    public void removeInvalidProjectiles(Level world) {
        if (this.projectiles.removeIf(uuid -> world.getEntity(uuid) == null)) {
            this.dirty = true;
        }
    }

    public void removeProjectile(UUID uuid) {
        if (this.projectiles.remove(uuid)) {
            this.dirty = true;
        }
    }
}

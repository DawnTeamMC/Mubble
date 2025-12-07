package fr.hugman.mubble.power_up;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public final class PowerUpProperties {
    private boolean dirty;
    private int cooldown;
    public List<UUID> projectiles;

    public static final PacketCodec<RegistryByteBuf, PowerUpProperties> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, powerUpProperties -> powerUpProperties.cooldown,
            Uuids.PACKET_CODEC.collect(PacketCodecs.toList()), powerUpProperties -> powerUpProperties.projectiles,
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
    public void doSoftChecks(PlayerEntity player) {
        this.removeInvalidProjectiles(player.getEntityWorld());
    }

    /**
     * Refreshes the projectiles list by removing invalid projectiles.
     */
    public void removeInvalidProjectiles(World world) {
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

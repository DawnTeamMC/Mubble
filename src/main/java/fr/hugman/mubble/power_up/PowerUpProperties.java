package fr.hugman.mubble.power_up;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PowerUpProperties {
    public int cooldown = 0;
    public List<UUID> projectiles = new ArrayList<>();

    public void reset() {
        this.cooldown = 0;
        this.projectiles.clear();
    }

    public void tick() {
        if(this.cooldown > 0) {
            this.cooldown--;
        }
    }

    /**
     * Refreshes the projectiles list by removing invalid projectiles.
     */
    public void removeInvalidProjectiles(World world) {
        this.projectiles.removeIf(uuid -> world.getEntity(uuid) == null);
    }
}

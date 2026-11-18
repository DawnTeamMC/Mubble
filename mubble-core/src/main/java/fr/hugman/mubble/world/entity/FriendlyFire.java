package fr.hugman.mubble.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;

public final class FriendlyFire {
    private FriendlyFire() {
    }

    /**
     * @return whether an attack of {@code owner} spares {@code target}: the owner themselves, their team and allies, and their pets
     */
    public static boolean spares(Entity owner, Entity target) {
        if (target == owner || owner.isAlliedTo(target) || target.isAlliedTo(owner)) {
            return true;
        }
        return target instanceof OwnableEntity ownable && ownable.getRootOwner() == owner;
    }
}

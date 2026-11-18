package fr.hugman.mubble.platformer;

import fr.hugman.mubble.platformer.tags.PlatformerItemTags;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * When the orbit camera aims over the shoulder rather than framing the player: as long as they hold
 * a weapon that shoots where the view points (a bow, a crossbow, a trident, or an item of
 * {@link PlatformerItemTags#AIMS_WHILE_HELD}), not only while it charges, so that shooting several times
 * in a row keeps the camera where it is, or have a power-up that shoots, as the fire flower does (see
 * {@link PowerUp#aims()}). The camera then sits close behind the right shoulder, and using the weapon
 * turns the body to face the crosshair.
 */
public final class PlatformerShoulder {
    private PlatformerShoulder() {
    }

    /** Whether {@code entity} aims over the shoulder right now: a weapon that aims is in either hand, or its power-up aims. */
    public static boolean aims(LivingEntity entity) {
        if (entity instanceof PowerUpHolder holder && holder.getPowerUp().map(powerUp -> powerUp.value().aims()).orElse(false)) {
            return true;
        }
        for (var hand : InteractionHand.values()) {
            if (aimsWith(entity.getItemInHand(hand))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether holding {@code stack} aims: its use draws a bow, loads a crossbow or raises a trident,
     * or it is an item of {@link PlatformerItemTags#AIMS_WHILE_HELD}.
     */
    public static boolean aimsWith(ItemStack stack) {
        if (stack.is(PlatformerItemTags.AIMS_WHILE_HELD)) {
            return true;
        }
        var animation = stack.getUseAnimation();
        return animation == ItemUseAnimation.BOW || animation == ItemUseAnimation.CROSSBOW || animation == ItemUseAnimation.TRIDENT;
    }

    /** The rotation that looks from {@code eye} at {@code target}, as vanilla's {@code Entity.lookAt}: pitch in x, yaw in y. */
    public static Vec2 lookAt(Vec3 eye, Vec3 target) {
        double dx = target.x - eye.x;
        double dy = target.y - eye.y;
        double dz = target.z - eye.z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        float yaw = Mth.wrapDegrees((float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F);
        float pitch = Mth.wrapDegrees((float) -(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG));
        return new Vec2(pitch, yaw);
    }
}

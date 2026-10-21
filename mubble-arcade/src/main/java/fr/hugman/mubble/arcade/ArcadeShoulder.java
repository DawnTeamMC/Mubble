package fr.hugman.mubble.arcade;

import fr.hugman.mubble.arcade.tags.ArcadeItemTags;
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
 * {@link ArcadeItemTags#AIMS_WHILE_HELD}), not only while it charges, so that shooting several times
 * in a row keeps the camera where it is. The body then faces the crosshair, as in the first person
 * view, and the camera sits close behind the right shoulder.
 */
public final class ArcadeShoulder {
    private ArcadeShoulder() {
    }

    /** Whether {@code entity} aims over the shoulder right now: a weapon that aims is in either hand. */
    public static boolean aims(LivingEntity entity) {
        for (var hand : InteractionHand.values()) {
            if (aimsWith(entity.getItemInHand(hand))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether holding {@code stack} aims: its use draws a bow, loads a crossbow or raises a trident,
     * or it is an item of {@link ArcadeItemTags#AIMS_WHILE_HELD}.
     */
    public static boolean aimsWith(ItemStack stack) {
        if (stack.is(ArcadeItemTags.AIMS_WHILE_HELD)) {
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

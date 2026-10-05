package fr.hugman.mubble.arcade.sim;

import fr.hugman.mubble.arcade.ArcadeProfile;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.ArcadeAttributes;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * The numbers one step runs on: the profile, read through the attributes and vanilla effects of the
 * player at that moment.
 * <p>
 * Both sides build it from what they know of the player, and attributes, effects and equipment are
 * synced: in the steady state the two tunings agree, which the server checks every tick anyway.
 *
 * @param profile          the active profile
 * @param allowed          which moves may be entered right now
 * @param runSpeed         value of {@code mubble:arcade_run_speed}
 * @param speedRatio       how much faster than its base the vanilla movement speed is, sprinting aside:
 *                         speed and slowness scale every run speed through it
 * @param jumpHeight       value of {@code mubble:arcade_jump_height}, jump boost included
 * @param gravityScale     vanilla gravity attribute over its default value
 * @param airDrag          value of {@code mubble:arcade_air_drag}
 * @param airControl       value of {@code mubble:arcade_air_control}
 * @param coyoteTicks      value of {@code mubble:arcade_coyote_ticks}
 * @param rollBoost        value of {@code mubble:arcade_roll_boost}
 * @param wallSlideSpeed   value of {@code mubble:arcade_wall_slide_speed}
 * @param groundPoundSpeed value of {@code mubble:arcade_ground_pound_speed}
 * @param sneakingSpeed    value of the vanilla sneaking speed attribute
 * @param stepHeight       value of the vanilla step height attribute
 * @param levitation       amplifier of the levitation effect, or -1
 * @param slowFalling      whether slow falling is on
 * @param waterEfficiency  value of the vanilla water movement efficiency attribute: depth strider
 * @param dolphinsGrace    whether dolphin's grace is on
 */
public record ArcadeTuning(
        ArcadeProfile profile,
        Predicate<ArcadeMove> allowed,
        double runSpeed,
        double speedRatio,
        double jumpHeight,
        double gravityScale,
        double airDrag,
        double airControl,
        int coyoteTicks,
        double rollBoost,
        double wallSlideSpeed,
        double groundPoundSpeed,
        double sneakingSpeed,
        double stepHeight,
        int levitation,
        boolean slowFalling,
        double waterEfficiency,
        boolean dolphinsGrace
) {
    /** The vanilla default of the gravity attribute, which the profile gravities are written against. */
    public static final double VANILLA_GRAVITY = 0.08D;
    private static final Identifier SPRINTING_MODIFIER = Identifier.withDefaultNamespace("sprinting");

    public static ArcadeTuning of(Player player, ArcadeProfile profile, Predicate<ArcadeMove> allowed) {
        var jumpBoost = player.getEffect(MobEffects.JUMP_BOOST);
        var levitation = player.getEffect(MobEffects.LEVITATION);
        double jumpBoostScale = jumpBoost == null ? 1.0D : 1.0D + profile.physics().effects().jumpBoostHeightPerLevel() * (jumpBoost.getAmplifier() + 1);
        return new ArcadeTuning(
                profile,
                allowed,
                player.getAttributeValue(ArcadeAttributes.ARCADE_RUN_SPEED),
                speedRatio(player),
                player.getAttributeValue(ArcadeAttributes.ARCADE_JUMP_HEIGHT) * jumpBoostScale,
                player.getAttributeValue(Attributes.GRAVITY) / VANILLA_GRAVITY,
                player.getAttributeValue(ArcadeAttributes.ARCADE_AIR_DRAG),
                player.getAttributeValue(ArcadeAttributes.ARCADE_AIR_CONTROL),
                (int) Math.round(player.getAttributeValue(ArcadeAttributes.ARCADE_COYOTE_TICKS)),
                player.getAttributeValue(ArcadeAttributes.ARCADE_ROLL_BOOST),
                player.getAttributeValue(ArcadeAttributes.ARCADE_WALL_SLIDE_SPEED),
                player.getAttributeValue(ArcadeAttributes.ARCADE_GROUND_POUND_SPEED),
                player.getAttributeValue(Attributes.SNEAKING_SPEED),
                player.getAttributeValue(Attributes.STEP_HEIGHT),
                levitation == null ? -1 : levitation.getAmplifier(),
                player.hasEffect(MobEffects.SLOW_FALLING),
                player.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY),
                player.hasEffect(MobEffects.DOLPHINS_GRACE)
        );
    }

    /**
     * The vanilla movement speed over its base value, without the sprinting bonus: the arcade layer
     * has its own run, and only wants to hear about speed, slowness and whatever else modifies the
     * walking pace.
     */
    private static double speedRatio(Player player) {
        var instance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance == null || instance.getBaseValue() <= 0.0D) {
            return 1.0D;
        }
        double value = instance.getValue();
        var sprinting = instance.getModifier(SPRINTING_MODIFIER);
        if (sprinting != null && sprinting.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            value /= 1.0D + sprinting.amount();
        }
        return value / instance.getBaseValue();
    }

    public double walkSpeed() {
        return this.profile.physics().ground().walkSpeed() * this.speedRatio;
    }

    public double effectiveRunSpeed() {
        return this.runSpeed * this.speedRatio;
    }

    /** Gravity of the states that are not a jump, before the vanilla gravity attribute scales it. */
    public double baseGravity() {
        return this.profile.physics().gravity().baseGravity();
    }
}

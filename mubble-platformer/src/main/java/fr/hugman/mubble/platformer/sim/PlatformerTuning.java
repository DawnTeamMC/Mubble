package fr.hugman.mubble.platformer.sim;

import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.PlatformerAttributes;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
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
 * @param runSpeed         value of {@code mubble:platformer_run_speed}
 * @param speedRatio       how much faster than its base the vanilla movement speed is, sprinting aside:
 *                         speed and slowness scale every run speed through it
 * @param jumpHeight       value of {@code mubble:platformer_jump_height}, scaled by the vanilla jump
 *                         strength attribute and jump boost (see {@link #jumpStrengthScale})
 * @param gravityScale     vanilla gravity attribute over its default value
 * @param airDrag          value of {@code mubble:platformer_air_drag}
 * @param airDragModifier  value of the vanilla air drag modifier attribute, see {@link #modifiedAirDrag}
 * @param airControl       value of {@code mubble:platformer_air_control}
 * @param coyoteTicks      value of {@code mubble:platformer_coyote_ticks}
 * @param rollBoost        value of {@code mubble:platformer_roll_boost}
 * @param wallSlideSpeed   value of {@code mubble:platformer_wall_slide_speed}
 * @param groundPoundSpeed value of {@code mubble:platformer_ground_pound_speed}
 * @param sneakingSpeed    value of the vanilla sneaking speed attribute
 * @param stepHeight       value of the vanilla step height attribute
 * @param levitation       amplifier of the levitation effect, or -1
 * @param slowFalling      whether slow falling is on
 * @param waterEfficiency  value of the vanilla water movement efficiency attribute: depth strider
 * @param dolphinsGrace    whether dolphin's grace is on
 */
public record PlatformerTuning(
        PlatformerProfile profile,
        Predicate<PlatformerMove> allowed,
        double runSpeed,
        double speedRatio,
        double jumpHeight,
        double gravityScale,
        double airDrag,
        double airDragModifier,
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

    public static PlatformerTuning of(Player player, PlatformerProfile profile, Predicate<PlatformerMove> allowed) {
        var levitation = player.getEffect(MobEffects.LEVITATION);
        return new PlatformerTuning(
                profile,
                allowed,
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_RUN_SPEED),
                speedRatio(player),
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_JUMP_HEIGHT) * jumpStrengthScale(player),
                player.getAttributeValue(Attributes.GRAVITY) / VANILLA_GRAVITY,
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_AIR_DRAG),
                player.getAttributeValue(Attributes.AIR_DRAG_MODIFIER),
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_AIR_CONTROL),
                (int) Math.round(player.getAttributeValue(PlatformerAttributes.PLATFORMER_COYOTE_TICKS)),
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_ROLL_BOOST),
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_WALL_SLIDE_SPEED),
                player.getAttributeValue(PlatformerAttributes.PLATFORMER_GROUND_POUND_SPEED),
                player.getAttributeValue(Attributes.SNEAKING_SPEED),
                player.getAttributeValue(Attributes.STEP_HEIGHT),
                levitation == null ? -1 : levitation.getAmplifier(),
                player.hasEffect(MobEffects.SLOW_FALLING),
                player.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY),
                player.hasEffect(MobEffects.DOLPHINS_GRACE)
        );
    }

    /**
     * How much higher than a plain vanilla jump the vanilla jump strength attribute and jump boost make
     * a jump: vanilla takes off at their sum ({@code LivingEntity.getJumpPower}), and a height goes
     * with the square of the take-off speed. The jump factor of the block underfoot, which vanilla
     * also folds in there, is left to {@link PlatformerWorld#jumpFactor}.
     */
    private static double jumpStrengthScale(Player player) {
        double strength = player.getAttributeValue(Attributes.JUMP_STRENGTH) + player.getJumpBoostPower();
        double ratio = strength / Attributes.JUMP_STRENGTH.value().getDefaultValue();
        return ratio * ratio;
    }

    /**
     * The vanilla movement speed over its base value, without the sprinting bonus: the platformer layer
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

    /** What horizontal speed keeps each tick in the air, the vanilla air drag modifier applied. */
    public double effectiveAirDrag() {
        return this.modifiedAirDrag(this.airDrag);
    }

    /**
     * {@code drag} with the vanilla air drag modifier attribute applied, the way vanilla applies it to
     * its own air drags ({@code LivingEntity.computeModifiedFriction}): above 1 it takes more speed
     * away, below 1 less, and at 0 none.
     */
    public double modifiedAirDrag(double drag) {
        return Mth.clamp(1.0D - (1.0D - drag) * this.airDragModifier, 0.0D, 1.0D);
    }

    /** Gravity of the states that are not a jump, before the vanilla gravity attribute scales it. */
    public double baseGravity() {
        return this.profile.physics().gravity().baseGravity();
    }
}

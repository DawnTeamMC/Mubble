package fr.hugman.mubble.platformer;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.move.GroundPoundMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.move.RollMove;
import fr.hugman.mubble.platformer.move.WallSlideMove;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;

/**
 * The attributes of the platformer movement layer.
 * <p>
 * Each one is a number gameplay is expected to tweak: perks, items, Vessels and effects do it with
 * plain vanilla attribute modifiers. While a profile is active, it puts a transient modifier of its own
 * on these attributes (see {@link #PROFILE}), which brings them to the values of the profile, so a
 * modifier always reads relative to the profile in use. Without an active profile they sit at their
 * defaults and nothing reads them.
 */
public class PlatformerAttributes {
    public static final Holder<Attribute> PLATFORMER_RUN_SPEED = register("platformer_run_speed", 0.4D, 0.0D, 4.0D);
    public static final Holder<Attribute> PLATFORMER_JUMP_HEIGHT = register("platformer_jump_height", 1.0D, 0.0D, 16.0D);
    public static final Holder<Attribute> PLATFORMER_ROLL_BOOST = register("platformer_roll_boost", 0.1D, 0.0D, 4.0D);
    public static final Holder<Attribute> PLATFORMER_AIR_DRAG = register("platformer_air_drag", 0.99D, 0.0D, 1.0D);
    public static final Holder<Attribute> PLATFORMER_AIR_CONTROL = register("platformer_air_control", 1.0D, 0.0D, 4.0D);
    public static final Holder<Attribute> PLATFORMER_COYOTE_TICKS = register("platformer_coyote_ticks", 3.0D, 0.0D, 40.0D);
    public static final Holder<Attribute> PLATFORMER_WALL_SLIDE_SPEED = register("platformer_wall_slide_speed", 0.15D, 0.0D, 4.0D);
    public static final Holder<Attribute> PLATFORMER_GROUND_POUND_SPEED = register("platformer_ground_pound_speed", 1.2D, 0.0D, 8.0D);

    /** Every platformer attribute, in a stable order. */
    public static final List<Holder<Attribute>> PLATFORMER = List.of(
            PLATFORMER_RUN_SPEED, PLATFORMER_JUMP_HEIGHT, PLATFORMER_ROLL_BOOST, PLATFORMER_AIR_DRAG,
            PLATFORMER_AIR_CONTROL, PLATFORMER_COYOTE_TICKS, PLATFORMER_WALL_SLIDE_SPEED, PLATFORMER_GROUND_POUND_SPEED
    );

    /** The id of the modifiers the active profile puts on the attributes it sets. */
    public static final Identifier PROFILE_MODIFIER_ID = Mubble.id("platformer_profile");

    /**
     * The modifier the active profile puts on each attribute it sets. They are transient: nothing of the
     * profile is saved with the player, and they come and go with the profile.
     * <p>
     * A platformer attribute takes the value of the profile: the modifier adds the gap between it and the
     * default. Being added to the base, it is scaled along with it by the modifiers that multiply the base.
     */
    public static final Map<Holder<Attribute>, Function<PlatformerProfile, AttributeModifier>> PROFILE = Map.ofEntries(
            value(PLATFORMER_RUN_SPEED, profile -> profile.physics().ground().runSpeed()),
            value(PLATFORMER_JUMP_HEIGHT, profile -> profile.physics().gravity().jumpHeightMultiplier()),
            value(PLATFORMER_ROLL_BOOST, profile -> profile.settingsOrDefault(PlatformerMoves.ROLL).get(RollMove.BOOST)),
            value(PLATFORMER_AIR_DRAG, profile -> profile.physics().air().drag()),
            value(PLATFORMER_AIR_CONTROL, profile -> profile.physics().air().control()),
            value(PLATFORMER_COYOTE_TICKS, profile -> profile.grace().coyoteTicks()),
            value(PLATFORMER_WALL_SLIDE_SPEED, profile -> profile.settingsOrDefault(PlatformerMoves.WALL_SLIDE).get(WallSlideMove.MAX_FALL_SPEED)),
            value(PLATFORMER_GROUND_POUND_SPEED, profile -> profile.settingsOrDefault(PlatformerMoves.GROUND_POUND).get(GroundPoundMove.DROP_SPEED))
    );

    private static Holder<Attribute> register(String path, double defaultValue, double min, double max) {
        var attribute = new RangedAttribute("attribute.name." + Mubble.MOD_ID + "." + path, defaultValue, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Mubble.id(path), attribute);
    }

    /** An attribute the profile gives a value of its own. */
    private static Map.Entry<Holder<Attribute>, Function<PlatformerProfile, AttributeModifier>> value(Holder<Attribute> attribute, ToDoubleFunction<PlatformerProfile> value) {
        double defaultValue = attribute.value().getDefaultValue();
        return modifier(attribute, AttributeModifier.Operation.ADD_VALUE, profile -> value.applyAsDouble(profile) - defaultValue);
    }

    private static Map.Entry<Holder<Attribute>, Function<PlatformerProfile, AttributeModifier>> modifier(Holder<Attribute> attribute, AttributeModifier.Operation operation, ToDoubleFunction<PlatformerProfile> amount) {
        return Map.entry(attribute, profile -> new AttributeModifier(PROFILE_MODIFIER_ID, amount.applyAsDouble(profile), operation));
    }

    /**
     * Puts the platformer attributes of {@code player} back to their defaults, once per player: the
     * profile used to write its values in their base, which saves from back then still carry.
     */
    public static void resetLegacyBases(Player player) {
        if (player.hasAttached(PlatformerAttachments.LEGACY_BASES_RESET)) {
            return;
        }
        for (var attribute : PLATFORMER) {
            var instance = player.getAttribute(attribute);
            if (instance != null) {
                instance.setBaseValue(attribute.value().getDefaultValue());
            }
        }
        player.setAttached(PlatformerAttachments.LEGACY_BASES_RESET, true);
    }
}

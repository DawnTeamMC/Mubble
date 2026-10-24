package fr.hugman.mubble.platformer;

import fr.hugman.mubble.Mubble;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/**
 * The attributes of the platformer movement layer.
 * <p>
 * Each one is a number gameplay is expected to tweak: perks, items, Vessels and effects do it with
 * plain vanilla attribute modifiers. While a profile is active, its own values are the base values of
 * these attributes, so a modifier always reads relative to the profile in use. Without an active
 * profile they sit at their defaults and nothing reads them.
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

    private static Holder<Attribute> register(String path, double defaultValue, double min, double max) {
        var attribute = new RangedAttribute("attribute.name." + Mubble.MOD_ID + "." + path, defaultValue, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Mubble.id(path), attribute);
    }
}

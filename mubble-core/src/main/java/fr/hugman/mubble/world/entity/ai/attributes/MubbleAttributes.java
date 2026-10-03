package fr.hugman.mubble.world.entity.ai.attributes;

import fr.hugman.mubble.Mubble;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/**
 * The attributes of the arcade movement layer.
 * <p>
 * Each one is a number gameplay is expected to tweak: perks, items, Vessels and effects do it with
 * plain vanilla attribute modifiers. While a profile is active, its own values are the base values of
 * these attributes, so a modifier always reads relative to the profile in use. Without an active
 * profile they sit at their defaults and nothing reads them.
 */
public class MubbleAttributes {
    public static final Holder<Attribute> ARCADE_RUN_SPEED = register("arcade_run_speed", 0.4D, 0.0D, 4.0D);
    public static final Holder<Attribute> ARCADE_JUMP_HEIGHT = register("arcade_jump_height", 1.0D, 0.0D, 16.0D);
    public static final Holder<Attribute> ARCADE_ROLL_BOOST = register("arcade_roll_boost", 0.1D, 0.0D, 4.0D);
    public static final Holder<Attribute> ARCADE_AIR_DRAG = register("arcade_air_drag", 0.99D, 0.0D, 1.0D);
    public static final Holder<Attribute> ARCADE_AIR_CONTROL = register("arcade_air_control", 1.0D, 0.0D, 4.0D);
    public static final Holder<Attribute> ARCADE_COYOTE_TICKS = register("arcade_coyote_ticks", 3.0D, 0.0D, 40.0D);
    public static final Holder<Attribute> ARCADE_WALL_SLIDE_SPEED = register("arcade_wall_slide_speed", 0.15D, 0.0D, 4.0D);
    public static final Holder<Attribute> ARCADE_GROUND_POUND_SPEED = register("arcade_ground_pound_speed", 1.2D, 0.0D, 8.0D);

    /** Every arcade attribute, in a stable order. */
    public static final List<Holder<Attribute>> ARCADE = List.of(
            ARCADE_RUN_SPEED, ARCADE_JUMP_HEIGHT, ARCADE_ROLL_BOOST, ARCADE_AIR_DRAG,
            ARCADE_AIR_CONTROL, ARCADE_COYOTE_TICKS, ARCADE_WALL_SLIDE_SPEED, ARCADE_GROUND_POUND_SPEED
    );

    private static Holder<Attribute> register(String path, double defaultValue, double min, double max) {
        var attribute = new RangedAttribute("attribute.name." + Mubble.MOD_ID + "." + path, defaultValue, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Mubble.id(path), attribute);
    }
}

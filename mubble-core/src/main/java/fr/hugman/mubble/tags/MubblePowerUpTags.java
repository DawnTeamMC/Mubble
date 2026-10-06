package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.tags.TagKey;

public class MubblePowerUpTags {
	public static final TagKey<PowerUp> CAN_RUN_ON_WATER = bind("can_run_on_water");
	public static final TagKey<PowerUp> LOST_TO_WATER = bind("lost_to_water");
	public static final TagKey<PowerUp> LOST_TO_RAIN = bind("lost_to_rain");

	public static TagKey<PowerUp> bind(String path) {
		return TagKey.create(MubbleRegistries.POWER_UP, Mubble.id(path));
	}
}

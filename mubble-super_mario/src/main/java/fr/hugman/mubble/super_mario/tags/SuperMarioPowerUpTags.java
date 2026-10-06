package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.world.power_up.PowerUp;
import net.minecraft.tags.TagKey;

public class SuperMarioPowerUpTags {
	public static final TagKey<PowerUp> DISABLES_STOMPING = bind("disables_stomping");
	public static final TagKey<PowerUp> CAN_WALK_ON_CLOUDS = bind("can_walk_on_clouds");

	public static TagKey<PowerUp> bind(String path) {
		return TagKey.create(MubbleRegistries.POWER_UP, SuperMario.id(path));
	}
}

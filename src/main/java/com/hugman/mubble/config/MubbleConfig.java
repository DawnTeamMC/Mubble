package com.hugman.mubble.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;

@Config(name = "mubble")
@Config.Gui.Background("minecraft:textures/block/red_concrete.png")
public class MubbleConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("biomes")
	@ConfigEntry.Gui.TransitiveObject
	public BiomesCategory biomes = new BiomesCategory();

	@Config(name = "biomes")
	public static class BiomesCategory implements ConfigData {
		@ConfigEntry.Gui.RequiresRestart
		public boolean press_garden = true;
		@ConfigEntry.Gui.RequiresRestart
		public boolean scarlet_forest = true;
	}
}

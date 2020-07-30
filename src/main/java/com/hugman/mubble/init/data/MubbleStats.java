package com.hugman.mubble.init.data;

import com.hugman.mubble.Mubble;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleStats {
	public static final Identifier INSPECT_PLACER = register("inspect_placer", StatFormatter.DEFAULT);
	public static final Identifier INTERACT_WITH_TIMESWAP_TABLE = register("interact_with_timeswap_table", StatFormatter.DEFAULT);
	public static final Identifier OPEN_PRESENT = register("open_present", StatFormatter.DEFAULT);
	public static final Identifier TALKED_TO_TOAD = register("talked_to_toad", StatFormatter.DEFAULT);

	private static Identifier register(String name, StatFormatter statFormatter) {
		Registry.register(Registry.CUSTOM_STAT, Mubble.id(name), Mubble.id(name));
		Stats.CUSTOM.getOrCreateStat(Mubble.id(name), statFormatter);
		return Mubble.id(name);
	}
}

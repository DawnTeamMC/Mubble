package hugman.mubble.init.data;

import hugman.mubble.Mubble;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleStats {
	public static final Identifier INSPECT_PLACER = register("inspect_placer", StatFormatter.DEFAULT);;
	public static final Identifier INTERACT_WITH_TIMESWAP_TABLE = register("interact_with_timeswap_table", StatFormatter.DEFAULT);
	public static final Identifier OPEN_PRESENT = register("open_present", StatFormatter.DEFAULT);
	public static final Identifier TALKED_TO_TOAD = register("talked_to_toad", StatFormatter.DEFAULT);

	private static Identifier register(String name, StatFormatter statFormatter) {
		Identifier identifier = new Identifier(Mubble.MOD_ID, name);
		Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
		Stats.CUSTOM.getOrCreateStat(identifier, statFormatter);
		return identifier;
	}
}

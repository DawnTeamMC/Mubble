package hugman.mubble.init;

import hugman.mubble.Mubble;
import io.github.kvverti.bannerpp.api.LoomPattern;
import io.github.kvverti.bannerpp.api.LoomPatterns;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleBannerPatterns
{
	public static final LoomPattern STAR = register("star", true);
	
	private static LoomPattern register(String name, boolean special)
	{
		return Registry.register(LoomPatterns.REGISTRY, new Identifier(Mubble.MOD_ID, name), new LoomPattern(special));
	}
}
package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.carver.PermafrostCaveWorldCarver;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;

public class MubbleCarvers
{
	public static final Carver<ProbabilityConfig> PERMAFROST_CAVE_WORLD_CARVER = register("permafrost_cave", new PermafrostCaveWorldCarver(ProbabilityConfig::deserialize));
	
	private static <C extends CarverConfig, F extends Carver<C>> F register(String name, F carver) {
		return Registry.register(Registry.CARVER, new Identifier(Mubble.MOD_ID, name), carver);
	}
}
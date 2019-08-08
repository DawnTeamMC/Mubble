package hugman.mubble.init.world;

import hugman.mubble.objects.world.carver.PermafrostCaveWorldCarver;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class MubbleCarvers
{
	public static final WorldCarver<ProbabilityConfig> PERMAFROST_CAVE_WORLD_CARVER = new PermafrostCaveWorldCarver(ProbabilityConfig::deserialize);
}
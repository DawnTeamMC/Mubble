package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import hugman.mubble.util.CustomDimensionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

public class MubbleDimensions
{
	public final static DimensionType PERMAFROST = register("permafrost", new CustomDimensionType(PermafrostDimension::new, false, VoronoiBiomeAccessType.INSTANCE));
	
	private static DimensionType register(String name, DimensionType dimension) {
		return Registry.register(Registry.DIMENSION, new Identifier(Mubble.MOD_ID, name), dimension);
	}
}
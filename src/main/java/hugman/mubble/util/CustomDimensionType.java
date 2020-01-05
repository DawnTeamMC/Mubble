package hugman.mubble.util;

import java.util.function.BiFunction;

import hugman.mubble.init.world.MubbleDimensions;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

public class CustomDimensionType extends DimensionType
{
	public CustomDimensionType(BiFunction<World, DimensionType, ? extends Dimension> factory, boolean hasSkylight, BiomeAccessType biomeAccessType)
	{
		super(Registry.DIMENSION.getRawId(MubbleDimensions.PERMAFROST), "_" + Registry.DIMENSION.getId(MubbleDimensions.PERMAFROST).getPath(), "DIM_" + Registry.DIMENSION.getId(MubbleDimensions.PERMAFROST).getPath().toUpperCase(), factory, hasSkylight, biomeAccessType);
	}
}
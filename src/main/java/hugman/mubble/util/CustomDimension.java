package hugman.mubble.util;

import java.util.function.BiFunction;

import hugman.mubble.init.world.MubbleDimensions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

public class CustomDimension extends ModDimension
{
	private final BiFunction<World, DimensionType, ? extends Dimension> dimClass;
	
	public CustomDimension(BiFunction<World, DimensionType, ? extends Dimension> dimClass, ResourceLocation name)
	{
		MubbleDimensions.DIMENSIONS.add(this);
		this.setRegistryName(name);
		this.dimClass = dimClass;
	}
	
	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
	{
		return this.dimClass;
	}
}
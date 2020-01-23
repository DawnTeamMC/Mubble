package hugman.mubble.util;

import java.util.function.BiFunction;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

public class CustomModDimension extends ModDimension
{
	private final BiFunction<World, DimensionType, ? extends Dimension> dimClass;

	public CustomModDimension(BiFunction<World, DimensionType, ? extends Dimension> dimClass)
	{
		this.dimClass = dimClass;
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
	{
		return this.dimClass;
	}
}
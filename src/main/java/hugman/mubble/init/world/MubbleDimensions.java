package hugman.mubble.init.world;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.objects.world.dimension.PermafrostDimension;
import hugman.mubble.util.CustomDimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class MubbleDimensions
{
    public static final List<CustomDimension> DIMENSIONS = new ArrayList<CustomDimension>();

	public static DimensionType PERMAFROST;
	
	public static void registerDimensions()
	{
		PERMAFROST = DimensionManager.registerDimension(PermafrostDimension.getName(), new CustomDimension(PermafrostDimension::new, PermafrostDimension.getName()), null, true);
	}
}
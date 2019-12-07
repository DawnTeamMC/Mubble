package hugman.mubble.init.world;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import hugman.mubble.util.CustomDimension;
import net.minecraftforge.common.ModDimension;

public class MubbleDimensions
{
    public static final List<ModDimension> DIMENSIONS = new ArrayList<ModDimension>();

	public static ModDimension PERMAFROST = register("permafrost", new CustomDimension(PermafrostDimension::new));
	
	private static ModDimension register(String name, ModDimension dimension)
	{
		ModDimension fDimension = dimension.setRegistryName(Mubble.MOD_ID, name);
		MubbleDimensions.DIMENSIONS.add(fDimension);
		return fDimension;
	}
}
package hugman.mod.init;

import hugman.mod.world.dimension.nintendo.DimensionNintendo;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/** 
 * Init class - used to initialize dimensions.
 */
public class DimensionInit
{
	public static final DimensionType NINTENDO = DimensionType.register("Nintendo", "_nintendo", 2, DimensionNintendo.class, false);
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(2, NINTENDO);
	}
}
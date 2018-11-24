package hugman.mod.init;

import hugman.mod.world.dimension.smash.DimensionWorldOfLight;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/** 
 * Init class - used to initialize dimensions.
 */
public class DimensionInit
{
	public static final DimensionType WORLD_OF_LIGHT = DimensionType.register("World Of Light", "_world_of_light", 2, DimensionWorldOfLight.class, false);
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(2, WORLD_OF_LIGHT);
	}
}
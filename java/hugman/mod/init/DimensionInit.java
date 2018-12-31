package hugman.mod.init;

import java.io.IOException;

import hugman.mod.util.handlers.MoveFiles;
import hugman.mod.world.dimension.smash.DimensionWorldOfLight;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/** 
 * Init class - used to initialize dimensions.
 */
public class DimensionInit
{
	public static final DimensionType WORLD_OF_LIGHT = DimensionType.register("World Of Light", "_world_of_light", 64, DimensionWorldOfLight.class, false);
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(64, WORLD_OF_LIGHT);
	}
	
	public static void createFiles() throws IOException
	{
		MoveFiles.copyToWorld("wol", "region/r.0.0.mca", 64);
		MoveFiles.copyToWorld("wol", "region/r.0.-1.mca", 64);
		MoveFiles.copyToWorld("wol", "region/r.-1.0.mca", 64);
		MoveFiles.copyToWorld("wol", "region/r.-1.-1.mca", 64);
	}
}
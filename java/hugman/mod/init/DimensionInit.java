package hugman.mod.init;

import java.io.IOException;

import hugman.mod.util.MoveFiles;
import hugman.mod.world.dimension.DimensionUltimatum;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/** 
 * Init class - used to initialize dimensions.
 */
public class DimensionInit
{
	public static final DimensionType ULTIMATUM = DimensionType.register("ultimatum", "_ultimatum", 64, DimensionUltimatum.class, false);
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(64, ULTIMATUM);
	}
	
	public static void createFiles()
	{
		String a = "ultimatum";
		MoveFiles.copyToWorld(a, "region/r.0.0.mca", 64);
		MoveFiles.copyToWorld(a, "region/r.0.-1.mca", 64);
		MoveFiles.copyToWorld(a, "region/r.-1.0.mca", 64);
		MoveFiles.copyToWorld(a, "region/r.-1.-1.mca", 64);
	}
}
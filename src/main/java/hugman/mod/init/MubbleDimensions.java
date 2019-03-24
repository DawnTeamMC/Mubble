package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.world.dimension.type.DimensionTypeUltimatum;
import hugman.mod.util.FileDisplacer;
import net.minecraftforge.common.ModDimension;

public class MubbleDimensions
{
    public static final List<ModDimension> DIMENSIONS = new ArrayList<ModDimension>();
    
	public static final ModDimension ULTIMATUM = new DimensionTypeUltimatum();
	
	public static void register(ModDimension dimension)
	{
		DIMENSIONS.add(dimension);
	}
	
	public static void createFiles()
	{
		String a = "ultimatum";
		FileDisplacer.copyToDimension(a, "test.txt", 64);
		/*FileDisplacer.copyToDimension(a, "region/r.0.0.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.0.-1.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.1.0.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.-1.0.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.-1.-1.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.2.0.mca", 64);
		FileDisplacer.copyToDimension(a, "region/r.-3.-1.mca", 64);*/
	}
}
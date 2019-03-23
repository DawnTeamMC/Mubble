package hugman.mod.init;

import hugman.mod.util.FileDisplacer;

public class MubbleDimensions
{
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
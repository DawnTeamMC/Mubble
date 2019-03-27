package hugman.mod.init;

import java.util.function.Function;

import hugman.mod.Mubble;
import hugman.mod.objects.world.dimension.DimensionUltimatum;
import hugman.mod.util.FileDisplacer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

public class MubbleDimensions
{    
	public static final ModDimension ULTIMATUM = new ModDimension()
	{	
		@Override
		public Function<DimensionType, ? extends Dimension> getFactory()
		{
			return DimensionUltimatum::new;
		}
	}.setRegistryName(Mubble.MOD_ID, "ultimatum");
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "ultimatum"), MubbleDimensions.ULTIMATUM, null);
	}
	
	public static void createFiles()
	{
		String a = "ultimatum";
		FileDisplacer.copyToDimension(a, "test.txt");
		/*FileDisplacer.copyToDimension(a, "region/r.0.0.mca");
		FileDisplacer.copyToDimension(a, "region/r.0.-1.mca");
		FileDisplacer.copyToDimension(a, "region/r.1.0.mca");
		FileDisplacer.copyToDimension(a, "region/r.-1.0.mca");
		FileDisplacer.copyToDimension(a, "region/r.-1.-1.mca");
		FileDisplacer.copyToDimension(a, "region/r.2.0.mca");
		FileDisplacer.copyToDimension(a, "region/r.-3.-1.mca");*/
	}
}
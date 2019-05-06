package hugman.mod.init.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import hugman.mod.Mubble;
import hugman.mod.objects.world.dimension.DimensionPermafrost;
import hugman.mod.objects.world.dimension.DimensionUltimatum;
import hugman.mod.util.FileDisplacer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

public class MubbleDimensions
{
    public static final List<ModDimension> MOD_DIMENSIONS = new ArrayList<ModDimension>();
    
    public static final ModDimension PERMAFROST = new ModDimension()
	{	
		@Override
		public Function<DimensionType, ? extends Dimension> getFactory()
		{
			return DimensionPermafrost::new;
		}
	}.setRegistryName(Mubble.MOD_ID, "permafrost");
	public static final ModDimension ULTIMATUM = new ModDimension()
	{	
		@Override
		public Function<DimensionType, ? extends Dimension> getFactory()
		{
			return DimensionUltimatum::new;
		}
	}.setRegistryName(Mubble.MOD_ID, "ultimatum");

	public static DimensionType ULTIMATUM_TYPE;
	public static DimensionType PERMAFROST_TYPE;
	
	public static void registerDimensions()
	{
		ULTIMATUM_TYPE = DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "permafrost"), MubbleDimensions.PERMAFROST, null);
		PERMAFROST_TYPE = DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "ultimatum"), MubbleDimensions.ULTIMATUM, null);
	}
	
	public static void createFiles(MinecraftServer server) throws IOException
	{
		FileDisplacer.createUltimatumWorldFiles(server);
	}
}
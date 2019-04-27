package hugman.mod.init;

import java.io.IOException;
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
	
	public static void registerDimensions()
	{
		DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "ultimatum"), MubbleDimensions.ULTIMATUM, null);
		DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "permafrost"), MubbleDimensions.PERMAFROST, null);
	}
	
	public static void createFiles(MinecraftServer server) throws IOException
	{
		FileDisplacer.createUltimatumWorldFiles(server);
	}
}
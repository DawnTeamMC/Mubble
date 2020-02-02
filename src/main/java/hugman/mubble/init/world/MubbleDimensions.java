package hugman.mubble.init.world;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import hugman.mubble.util.CustomModDimension;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

public class MubbleDimensions
{
	public static final List<DimensionType> DIMENSIONS_TYPES = new ArrayList<DimensionType>();
	public static final List<ModDimension> MOD_DIMENSIONS = new ArrayList<ModDimension>();

	public static DimensionType PERMAFROST;

	public static final ModDimension PERMAFROST_MOD_DIMENSION = register("permafrost", new CustomModDimension(PermafrostDimension::new));
	
	private static ModDimension register(String name, ModDimension modDimensionIn)
	{
		ModDimension modDimension = modDimensionIn.setRegistryName(Mubble.MOD_ID, name);
		MOD_DIMENSIONS.add(modDimension);
		return modDimension;
	}
	
	private static DimensionType register(ModDimension modDimension, boolean hasSkyLight)
	{
		DimensionType dimensionType = DimensionManager.registerOrGetDimension(modDimension.getRegistryName(), modDimension, new PacketBuffer(Unpooled.buffer()), hasSkyLight);
		DIMENSIONS_TYPES.add(dimensionType);
		return dimensionType;
	}
	
	public static void registerDimensions()
	{
		PERMAFROST = register(PERMAFROST_MOD_DIMENSION, false);
	}
}
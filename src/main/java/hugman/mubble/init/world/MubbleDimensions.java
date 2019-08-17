package hugman.mubble.init.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
		{
			return PermafrostDimension::new;
		}
	}.setRegistryName(Mubble.MOD_ID, "permafrost");

	public static DimensionType PERMAFROST_TYPE;
	
	public static void registerDimensions()
	{
		PERMAFROST_TYPE = DimensionManager.registerDimension(new ResourceLocation(Mubble.MOD_ID, "permafrost"), MubbleDimensions.PERMAFROST, null, true);
	}
}
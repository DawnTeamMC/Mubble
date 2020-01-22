package hugman.mubble.init.world;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.dimension.PermafrostDimension;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleDimensions
{
	public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Mubble.MOD_ID);

	public static DimensionType PERMAFROST;
	
	public static final RegistryObject<ModDimension> PERMAFROST_MOD_DIMENSION = register(PermafrostDimension.getName(), MubbleDimensions::dimFactory);

	private static ModDimension dimFactory()
	{
		return new ModDimension()
		{
			@Override
			public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
			{
				return PermafrostDimension::new;
			}
		};
	}
	
	private static RegistryObject<ModDimension> register(final String name, final Supplier<ModDimension> sup)
	{
		return DIMENSIONS.register(name, sup);
	}
	
	@Mod.EventBusSubscriber(modid = Mubble.MOD_ID)
	public static class EventDimensionType
	{
		@SubscribeEvent
		public static void onModDimensionRegister(final RegisterDimensionsEvent event)
		{
			ResourceLocation id = new ResourceLocation(Mubble.MOD_ID, PermafrostDimension.getName());
			if(DimensionType.byName(id) == null)
			{
				PERMAFROST = DimensionManager.registerDimension(id, PERMAFROST_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
				DimensionManager.keepLoaded(PERMAFROST, false);
			}
			else
			{
				PERMAFROST = DimensionType.byName(id);
			}
		}
	}
}
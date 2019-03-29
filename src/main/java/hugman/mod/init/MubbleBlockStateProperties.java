package hugman.mod.init;

import hugman.mod.objects.state.properties.SlabVerticalType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class MubbleBlockStateProperties
{
	public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");
	public static final BooleanProperty PRINCESS = BooleanProperty.create("princess");
	public static final EnumProperty<SlabVerticalType> VERTICAL_SLAB_TYPE = EnumProperty.create("type", SlabVerticalType.class);
}
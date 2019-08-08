package hugman.mubble.init;

import hugman.mubble.objects.block_state.properties.FluidLog;
import hugman.mubble.objects.block_state.properties.VerticalSlabType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class MubbleBlockStateProperties
{
    public static final BooleanProperty PRINCESS = BooleanProperty.create("princess");
    public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.create("fluidlog", FluidLog.class);
    public static final EnumProperty<VerticalSlabType> VERTICAL_SLAB_TYPE = EnumProperty.create("type", VerticalSlabType.class);
}
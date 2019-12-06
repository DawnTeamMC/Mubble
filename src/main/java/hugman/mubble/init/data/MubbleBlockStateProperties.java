package hugman.mubble.init.data;

import hugman.mubble.objects.block.block_state_property.FluidLog;
import hugman.mubble.objects.block.block_state_property.Princess;
import hugman.mubble.objects.block.block_state_property.VerticalSlabType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class MubbleBlockStateProperties
{
    public static final BooleanProperty OVER = BooleanProperty.create("over");
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
    public static final BooleanProperty ILLUMINATED = BooleanProperty.create("illuminated");
    public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.create("fluidlog", FluidLog.class);
    public static final EnumProperty<VerticalSlabType> VERTICAL_SLAB_TYPE = EnumProperty.create("type", VerticalSlabType.class);
    public static final EnumProperty<Princess> PRINCESS = EnumProperty.create("princess", Princess.class);
}
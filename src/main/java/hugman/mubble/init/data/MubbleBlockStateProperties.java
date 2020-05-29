package hugman.mubble.init.data;

import hugman.mubble.objects.block.block_state_property.FluidLog;
import hugman.mubble.objects.block.block_state_property.Princess;
import hugman.mubble.objects.block.block_state_property.VerticalSlabType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class MubbleBlockStateProperties
{
	public static final BooleanProperty OVER = BooleanProperty.of("over");
	public static final BooleanProperty EMPTY = BooleanProperty.of("empty");
	public static final BooleanProperty LOCKED = BooleanProperty.of("locked");
	public static final BooleanProperty ILLUMINATED = BooleanProperty.of("illuminated");
	public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.of("fluidlog", FluidLog.class);
	public static final EnumProperty<VerticalSlabType> VERTICAL_SLAB_TYPE = EnumProperty.of("type", VerticalSlabType.class);
	public static final EnumProperty<Princess> PRINCESS = EnumProperty.of("princess", Princess.class);
}
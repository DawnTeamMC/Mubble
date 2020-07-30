package com.hugman.mubble.init.data;

import com.hugman.mubble.object.block.block_state_property.FluidLog;
import com.hugman.mubble.object.block.block_state_property.Princess;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class MubbleBlockStateProperties {
	public static final BooleanProperty OVER = BooleanProperty.of("over");
	public static final BooleanProperty EMPTY = BooleanProperty.of("empty");
	public static final BooleanProperty LOCKED = BooleanProperty.of("locked");
	public static final BooleanProperty ILLUMINATED = BooleanProperty.of("illuminated");
	public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.of("fluidlog", FluidLog.class);
	public static final EnumProperty<Princess> PRINCESS = EnumProperty.of("princess", Princess.class);
}
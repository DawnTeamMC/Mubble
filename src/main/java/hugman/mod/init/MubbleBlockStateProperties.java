package hugman.mod.init;

import hugman.mod.objects.block_state.properties.FluidLog;
import hugman.mod.objects.block_state.properties.GameStyle;
import hugman.mod.objects.block_state.properties.SlabVerticalType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public class MubbleBlockStateProperties
{
    public static final BooleanProperty PRINCESS = BooleanProperty.create("princess");
    public static final EnumProperty<FluidLog> FLUIDLOG = EnumProperty.create("fluidlog", FluidLog.class);
    public static final EnumProperty<SlabVerticalType> VERTICAL_SLAB_TYPE = EnumProperty.create("type", SlabVerticalType.class);
    public static final EnumProperty<GameStyle> GAME_STYLE = EnumProperty.create("game_style", GameStyle.class);
}
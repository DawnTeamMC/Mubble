package hugman.mod.objects.world.dimension.type;

import java.util.function.Function;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleDimensions;
import hugman.mod.objects.world.dimension.DimensionUltimatum;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

public class DimensionTypeUltimatum extends ModDimension
{
	public DimensionTypeUltimatum()
	{
        setRegistryName(Mubble.MOD_ID, "ultimatum");
        MubbleDimensions.DIMENSIONS.add(this);
	}
	
    @Override
    public Function<DimensionType, ? extends Dimension> getFactory()
    {
        return DimensionUltimatum::new;
    }
}
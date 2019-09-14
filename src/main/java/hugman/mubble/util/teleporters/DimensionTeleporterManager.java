package hugman.mubble.util.teleporters;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimensionTeleporterManager
{
	public static void teleport(World worldIn, Entity entity, DimensionType dimensionType)
	{
		if (!worldIn.isRemote)
		{
			if (entity.getRidingEntity() == null && !entity.isBeingRidden())
			{
				entity.setPortal(new BlockPos(entity.posX, entity.posY, entity.posZ));

				if (entity.timeUntilPortal > 0)
				{
					entity.timeUntilPortal = 10;
				}
				else if (entity.dimension != dimensionType)
				{
					entity.timeUntilPortal = 10;
					entity.changeDimension(dimensionType);
				}
				else if (entity.dimension == dimensionType)
				{
					entity.timeUntilPortal = 10;
					entity.changeDimension(DimensionType.OVERWORLD);
				}
			}
		}
	}
}
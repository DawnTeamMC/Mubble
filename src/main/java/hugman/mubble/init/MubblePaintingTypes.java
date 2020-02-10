package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.PaintingType;

public class MubblePaintingTypes
{
	public static final List<PaintingType> PAINTING_TYPES = new ArrayList<PaintingType>();

	public static final PaintingType BOB_OMB_BATTLEFIELD = register("bob_omb_battlefield", 48, 48);
	public static final PaintingType WHOMP_FORTRESS = register("whomp_fortress", 48, 48);
	public static final PaintingType JOLLY_ROGER_BAY = register("jolly_roger_bay", 48, 48);
	public static final PaintingType COOL_COOL_MOUNTAIN = register("cool_cool_mountain", 48, 48);
	public static final PaintingType LETHAL_LAVA_LAND = register("lethal_lava_land", 48, 48);
	public static final PaintingType DIRE_DIRE_DOCKS = register("dire_dire_docks", 48, 48);
	public static final PaintingType WET_DRY_WORLD = register("wet_dry_world", 48, 48);
	public static final PaintingType TALL_TALL_MOUNTAIN = register("tall_tall_mountain", 48, 48);
	public static final PaintingType TINY_HUGE_ISLAND = register("tiny_huge_island", 48, 48);
	
	private static PaintingType register(String name, int width, int height)
	{
		PaintingType type = new PaintingType(width, height).setRegistryName(name);
		PAINTING_TYPES.add(type);
		return type;
	}
}
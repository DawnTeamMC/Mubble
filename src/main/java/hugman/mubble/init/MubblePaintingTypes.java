package hugman.mubble.init;

import hugman.mubble.Mubble;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubblePaintingTypes
{
	public static final PaintingMotive BOB_OMB_BATTLEFIELD = register("bob_omb_battlefield", 48, 48);
	public static final PaintingMotive WHOMP_FORTRESS = register("whomp_fortress", 48, 48);
	public static final PaintingMotive JOLLY_ROGER_BAY = register("jolly_roger_bay", 48, 48);
	public static final PaintingMotive COOL_COOL_MOUNTAIN = register("cool_cool_mountain", 48, 48);
	public static final PaintingMotive LETHAL_LAVA_LAND = register("lethal_lava_land", 48, 48);
	public static final PaintingMotive DIRE_DIRE_DOCKS = register("dire_dire_docks", 48, 48);
	public static final PaintingMotive WET_DRY_WORLD = register("wet_dry_world", 48, 48);
	public static final PaintingMotive TALL_TALL_MOUNTAIN = register("tall_tall_mountain", 48, 48);
	public static final PaintingMotive TINY_HUGE_ISLAND = register("tiny_huge_island", 48, 48);
	
	private static PaintingMotive register(String name, int width, int height)
	{
		return Registry.register(Registry.PAINTING_MOTIVE, new Identifier(Mubble.MOD_ID, name), new PaintingMotive(width, height));
	}
}
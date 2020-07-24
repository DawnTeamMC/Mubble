package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.util.DataWriter;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubblePaintingTypes {
	public static final PaintingMotive THE_GREAT_WAVE_OFF_KANAGAWA = register("the_great_wave_off_kanagawa", 64, 64);
	public static final PaintingMotive THE_SCREAM = register("the_scream", 16, 32);
	public static final PaintingMotive THE_STARRY_NIGHT = register("the_starry_night", 64, 48);

	public static final PaintingMotive BOB_OMB_BATTLEFIELD = register("bob_omb_battlefield", 48, 48);
	public static final PaintingMotive WHOMP_FORTRESS = register("whomp_fortress", 48, 48);
	public static final PaintingMotive JOLLY_ROGER_BAY = register("jolly_roger_bay", 48, 48);
	public static final PaintingMotive COOL_COOL_MOUNTAIN = register("cool_cool_mountain", 48, 48);
	public static final PaintingMotive LETHAL_LAVA_LAND = register("lethal_lava_land", 48, 48);
	public static final PaintingMotive DIRE_DIRE_DOCKS = register("dire_dire_docks", 48, 48);
	public static final PaintingMotive WET_DRY_WORLD = register("wet_dry_world", 48, 48);
	public static final PaintingMotive TALL_TALL_MOUNTAIN = register("tall_tall_mountain", 48, 48);
	public static final PaintingMotive TINY_HUGE_ISLAND = register("tiny_huge_island", 48, 48);

	public static final PaintingMotive SSBU_ROSTER = register("ssbu_roster", 384, 64);

	private static PaintingMotive register(String name, int width, int height) {
		DataWriter.entryNamesData.painting_types.add(Mubble.id(name).toString());
		DataWriter.entryCountsData.painting_types++;
		DataWriter.save();
		return Registry.register(Registry.PAINTING_MOTIVE, Mubble.id(name), new PaintingMotive(width, height));
	}
}
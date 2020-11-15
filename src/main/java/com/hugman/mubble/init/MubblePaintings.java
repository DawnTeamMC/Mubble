package com.hugman.mubble.init;

import com.hugman.dawn.api.creator.PaintingCreator;
import net.minecraft.entity.decoration.painting.PaintingMotive;

public class MubblePaintings extends MubblePack {
	public static final PaintingMotive THE_GREAT_WAVE_OFF_KANAGAWA = register(new PaintingCreator.Builder("the_great_wave_off_kanagawa", 64, 64));
	public static final PaintingMotive THE_SCREAM = register(new PaintingCreator.Builder("the_scream", 16, 32));
	public static final PaintingMotive THE_STARRY_NIGHT = register(new PaintingCreator.Builder("the_starry_night", 64, 48));
	public static final PaintingMotive BOB_OMB_BATTLEFIELD = register(new PaintingCreator.Builder("bob_omb_battlefield", 48, 48));
	public static final PaintingMotive WHOMP_FORTRESS = register(new PaintingCreator.Builder("whomp_fortress", 48, 48));
	public static final PaintingMotive JOLLY_ROGER_BAY = register(new PaintingCreator.Builder("jolly_roger_bay", 48, 48));
	public static final PaintingMotive COOL_COOL_MOUNTAIN = register(new PaintingCreator.Builder("cool_cool_mountain", 48, 48));
	public static final PaintingMotive LETHAL_LAVA_LAND = register(new PaintingCreator.Builder("lethal_lava_land", 48, 48));
	public static final PaintingMotive DIRE_DIRE_DOCKS = register(new PaintingCreator.Builder("dire_dire_docks", 48, 48));
	public static final PaintingMotive WET_DRY_WORLD = register(new PaintingCreator.Builder("wet_dry_world", 48, 48));
	public static final PaintingMotive TALL_TALL_MOUNTAIN = register(new PaintingCreator.Builder("tall_tall_mountain", 48, 48));
	public static final PaintingMotive TINY_HUGE_ISLAND = register(new PaintingCreator.Builder("tiny_huge_island", 48, 48));
	public static final PaintingMotive SSBU_ROSTER = register(new PaintingCreator.Builder("ssbu_roster", 384, 64));

	public static void init() {

	}
}
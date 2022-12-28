package fr.hugman.mubble.util;

import net.minecraft.SharedConstants;

public class SplatConversions {
	public static long tps(int frames) {
		// Splatoon 3 runs at 16 frames per second while Minecraft runs at 20 ticks per second.
		return (long) frames * SharedConstants.TICKS_PER_SECOND / 60;
	}

	public static float damage(float damage) {
		// Splatoon 3 stores damage values as integers (multiplied by 10), so we need to convert them to floats
		return damage / 10.0F;
	}

	public static float unitsToBlocks(float units) {
		// Splatoon 3 stores distances in units, while Minecraft stores them in blocks.
		//TODO: find a good conversion factor
		return units;
	}
}

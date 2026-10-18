package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.LevelReader;

/**
 * Where the sky starts, for a loftwing. Everything the bird does on its own is measured against the
 * cloud level: it spawns above it, it comes back up to it, and it only ever goes below it on an
 * errand.
 * <p>
 * The game already knows where the clouds are, per dimension and per biome, so the height is read
 * from vanilla's {@link EnvironmentAttributes#CLOUD_HEIGHT} rather than from a constant of our own.
 * A data pack that lowers the clouds lowers the loftwings with them.
 */
public final class LoftwingSky {
	private LoftwingSky() {
	}

	public static float cloudLevel(LevelReader level, BlockPos pos) {
		return level.environmentAttributes().getValue(EnvironmentAttributes.CLOUD_HEIGHT, pos);
	}
}

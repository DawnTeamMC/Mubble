package hugman.mubble.object.block;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class OreBlock extends net.minecraft.block.OreBlock {
	public OreBlock(Settings properties) {
		super(properties);
	}

	@Override
	protected int getExperienceWhenMined(Random rand) {
		if(this == MubbleBlocks.VANADIUM_ORE.getBlock()) {
			return MathHelper.nextInt(rand, 4, 8);
		}
		return super.getExperienceWhenMined(rand);
	}
}

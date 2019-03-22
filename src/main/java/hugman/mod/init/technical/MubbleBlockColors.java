package hugman.mod.init.technical;

import hugman.mod.init.elements.MubbleBlocks;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;

public class MubbleBlockColors extends BlockColors
{
	public static BlockColors init()
	{
		BlockColors blockcolors = new BlockColors();
		blockcolors.register((p_210225_0_, p_210225_1_, p_210225_2_, p_210225_3_) ->
		{
			return p_210225_1_ != null && p_210225_2_ != null ? BiomeColors.getGrassColor(p_210225_1_, p_210225_2_) : GrassColors.get(0.5D, 1.0D);
		}, MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		net.minecraftforge.client.ForgeHooksClient.onBlockColorsInit(blockcolors);
		return blockcolors;
	}
}
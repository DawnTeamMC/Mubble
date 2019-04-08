package hugman.mod.init;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class MubbleColorMaps extends BlockColors
{
	public static void registerBlockColors(final ColorHandlerEvent.Block event)
	{
		final BlockColors blockColors = event.getBlockColors();
		
		blockColors.register((p_210225_0_, p_210225_1_, p_210225_2_, p_210225_3_) ->
        {
            return p_210225_1_ != null && p_210225_2_ != null ? BiomeColors.getGrassColor(p_210225_1_, p_210225_2_) : GrassColors.get(0.5D, 1.0D);
        }, MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		blockColors.register((p_210232_0_, p_210232_1_, p_210232_2_, p_210232_3_) ->
		{
			return FoliageColors.getSpruce();
		}, MubbleBlocks.SPRUCE_LEAVES_CARPET);
		blockColors.register((p_210232_0_, p_210232_1_, p_210232_2_, p_210232_3_) ->
		{
			return FoliageColors.getBirch();
		}, MubbleBlocks.BIRCH_LEAVES_CARPET);
		blockColors.register((p_210229_0_, p_210229_1_, p_210229_2_, p_210229_3_) ->
		{
			return p_210229_1_ != null && p_210229_2_ != null ? BiomeColors.getFoliageColor(p_210229_1_, p_210229_2_) : FoliageColors.getDefault();
		}, MubbleBlocks.OAK_LEAVES_CARPET, MubbleBlocks.JUNGLE_LEAVES_CARPET, MubbleBlocks.ACACIA_LEAVES_CARPET, MubbleBlocks.DARK_OAK_LEAVES_CARPET, MubbleBlocks.PALM_LEAVES, MubbleBlocks.PALM_LEAVES_CARPET);
		blockColors.register((p_210229_0_, p_210229_1_, p_210229_2_, p_210229_3_) ->
		{
			return 16104984;
		}, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_LEAVES_CARPET);
		blockColors.register((p_210229_0_, p_210229_1_, p_210229_2_, p_210229_3_) ->
		{
			return 16539096;
		}, MubbleBlocks.CHERRY_OAK_LEAVES, MubbleBlocks.CHERRY_OAK_LEAVES_CARPET);
		blockColors.register((p_210229_0_, p_210229_1_, p_210229_2_, p_210229_3_) ->
		{
			return 10622269;
		}, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_LEAVES_CARPET);
	}
	
	public static void registerItemColors(final ColorHandlerEvent.Item event)
	{
		final ItemColors itemColors = event.getItemColors();

		itemColors.register((p_210235_1_, p_210235_2_) ->
		{
	        return GrassColors.get(0.5D, 1.0D);
	    }, MubbleBlocks.OAK_LEAVES_CARPET, MubbleBlocks.SPRUCE_LEAVES_CARPET, MubbleBlocks.BIRCH_LEAVES_CARPET, MubbleBlocks.JUNGLE_LEAVES_CARPET, MubbleBlocks.ACACIA_LEAVES_CARPET, MubbleBlocks.DARK_OAK_LEAVES_CARPET, MubbleBlocks.PALM_LEAVES, MubbleBlocks.PALM_LEAVES_CARPET, MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		itemColors.register((p_210235_1_, p_210235_2_) ->
		{
	        return 16104984;
	    }, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_LEAVES_CARPET);
		itemColors.register((p_210235_1_, p_210235_2_) ->
		{
	        return 16539096;
	    }, MubbleBlocks.CHERRY_OAK_LEAVES, MubbleBlocks.CHERRY_OAK_LEAVES_CARPET);
		itemColors.register((p_210235_1_, p_210235_2_) ->
		{
	        return 10622269;
	    }, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_LEAVES_CARPET);
	}
}
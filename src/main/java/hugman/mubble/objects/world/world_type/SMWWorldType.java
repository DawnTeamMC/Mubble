package hugman.mubble.objects.world.world_type;

import hugman.mubble.init.world.MubbleBiomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.CheckerboardBiomeProvider;
import net.minecraft.world.biome.provider.CheckerboardBiomeProviderSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;

public class SMWWorldType extends WorldType
{	
	public SMWWorldType() 
	{
		super("smw_world");
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator(World world)
	{
		OverworldGenSettings settings = new OverworldGenSettings();
		CheckerboardBiomeProviderSettings checkerboard = new CheckerboardBiomeProviderSettings();
		
		checkerboard.setBiomes(new Biome[]{MubbleBiomes.SMW_GROUND});
		return new OverworldChunkGenerator(world, new CheckerboardBiomeProvider(checkerboard), settings);
	}
}
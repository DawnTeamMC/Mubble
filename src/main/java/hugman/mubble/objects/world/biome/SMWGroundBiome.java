package hugman.mubble.objects.world.biome;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class SMWGroundBiome extends Biome
{
	public SMWGroundBiome()
	{
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, MubbleSurfaceBuilders.SMW_GROUND_SURFACE).precipitation(Biome.RainType.RAIN).category(Biome.Category.FOREST).depth(0.1F).scale(0.2F).temperature(0.8F).downfall(0.9F).waterColor(155336).waterFogColor(541).parent((String)null));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(MubbleEntities.GOOMBA, 20, 4, 6));
		this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(MubbleEntities.TOAD, 20, 4, 4));
		
        this.setRegistryName(Mubble.MOD_ID, "smw_ground");
        MubbleBiomes.register(this);
	}
}
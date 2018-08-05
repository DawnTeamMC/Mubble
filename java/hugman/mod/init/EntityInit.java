package hugman.mod.init;

import hugman.mod.Main;
import hugman.mod.entity.EntityToad;
import hugman.mod.util.Reference;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit
{	
	public static void registerEntities()
	{
		registerEntity("toad", EntityToad.class, Reference.ENTITY_TOAD, 60, 14671839, 16722728);
	}
	
	private static void registerEntity(String name, Class<? extends EntityLiving> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
		EntityRegistry.addSpawn(EntityToad.class, 25, 4, 6, EnumCreatureType.CREATURE, Biome.getBiome(1), Biome.getBiome(4), Biome.getBiome(14), Biome.getBiome(15), Biome.getBiome(129));
	}
}

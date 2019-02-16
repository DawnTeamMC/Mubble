package hugman.mod.init;

import hugman.mod.Main;
import hugman.mod.entity.EntityChincho;
import hugman.mod.entity.EntityFlyingBlock;
import hugman.mod.entity.EntityToad;
import hugman.mod.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/** 
 * Init class - used to initialize entities.
 */
public class MubbleEntities
{	
	public static void registerEntities()
	{
		registerEntity("toad", EntityToad.class, Reference.ENTITY_TOAD, 60, 14671839, 16722728);
		registerEntity("chincho", EntityChincho.class, Reference.ENTITY_CHINCHO, 60, 7527671, 4903);
	}
	
	private static void registerEntity(String name, Class<? extends EntityLiving> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
		EntityRegistry.addSpawn(EntityToad.class, 25, 4, 6, EnumCreatureType.CREATURE, MubbleBiomes.MUSHROOM_KINGDOM);
		EntityRegistry.addSpawn(EntityChincho.class, 25, 4, 6, EnumCreatureType.MONSTER, Biome.getBiome(2), Biome.getBiome(130));
	}
}
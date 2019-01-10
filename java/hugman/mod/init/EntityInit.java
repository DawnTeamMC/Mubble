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
public class EntityInit
{	
	public static void registerEntities()
	{
		registerEggEntity("toad", EntityToad.class, Reference.ENTITY_TOAD, 60, 14671839, 16722728);
		registerEggEntity("chincho", EntityChincho.class, Reference.ENTITY_CHINCHO, 60, 7527671, 4903);
		registerNonEggEntity("flying_block", EntityFlyingBlock.class, Reference.ENTITY_FLYING_BLOCK, 60);
	}
	
	private static void registerEggEntity(String name, Class<? extends EntityLiving> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
		EntityRegistry.addSpawn(EntityToad.class, 25, 4, 6, EnumCreatureType.CREATURE, BiomeInit.MUSHROOM_KINGDOM);
		EntityRegistry.addSpawn(EntityChincho.class, 25, 4, 6, EnumCreatureType.MONSTER, Biome.getBiome(2), Biome.getBiome(130));
	}
	
	private static void registerNonEggEntity(String name, Class<? extends Entity> entity, int id, int range)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Main.instance, range, 1, true);
	}
}
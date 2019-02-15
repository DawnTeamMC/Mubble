package hugman.mod.init;

import hugman.mod.Mubble;
import hugman.mod.entity.EntityChincho;
import hugman.mod.entity.EntityFlyingBlock;
import hugman.mod.entity.EntityToad;
import hugman.mod.entity.render.RenderChincho;
import hugman.mod.entity.render.RenderFlyingBlock;
import hugman.mod.entity.render.RenderToad;
import hugman.mod.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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
		registerEntity("falling_block", EntityFlyingBlock.class, Reference.ENTITY_FLYING_BLOCK, 256);
	}
	
	private static void registerEntity(String name, Class<? extends EntityLiving> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Mubble.instance, range, 1, true, color1, color2);
		EntityRegistry.addSpawn(EntityToad.class, 25, 4, 6, EnumCreatureType.CREATURE, MubbleBiomes.MUSHROOM_KINGDOM);
		EntityRegistry.addSpawn(EntityChincho.class, 25, 4, 6, EnumCreatureType.MONSTER, Biome.getBiome(2), Biome.getBiome(130));
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), entity, name, id, Mubble.instance, range, 1, true);
	}
	
	public static void registerEntityRenderers() 
    {
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityChincho.class, RenderChincho::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBlock.class, RenderFlyingBlock::new);
    }
}
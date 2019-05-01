package hugman.mod.init;

import hugman.mod.Mubble;
import hugman.mod.objects.entity.EntityChincho;
import hugman.mod.objects.entity.EntityFlyingBlock;
import hugman.mod.objects.entity.EntityToad;
import hugman.mod.objects.entity.EntityZombieCowman;
import hugman.mod.objects.entity.render.RenderChincho;
import hugman.mod.objects.entity.render.RenderFlyingBlock;
import hugman.mod.objects.entity.render.RenderToad;
import hugman.mod.objects.entity.render.RenderZombieCowman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class MubbleEntities
{
	public static final EntityType<EntityChincho> CHINCHO = register("chincho", EntityType.Builder.create(EntityChincho.class, EntityChincho::new));
	public static final EntityType<EntityFlyingBlock> FLYING_BLOCK = register("flying_block", EntityType.Builder.create(EntityFlyingBlock.class, EntityFlyingBlock::new).tracker(128, 1, true));
	public static final EntityType<EntityToad> TOAD = register("toad", EntityType.Builder.create(EntityToad.class, EntityToad::new));
	public static final EntityType<EntityZombieCowman> ZOMBIE_COWMAN = register("zombie_cowman", EntityType.Builder.create(EntityZombieCowman.class, EntityZombieCowman::new));
	
	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder)
	{
	    EntityType<T> entitytype = builder.build(Mubble.MOD_PREFIX + id);
	    entitytype.setRegistryName(Mubble.MOD_ID, id);
		return entitytype;
	}
	
    public static void registerEntity(IForgeRegistry<EntityType<?>> registry)
    {
    	registry.register(CHINCHO);
    	registry.register(FLYING_BLOCK);
    	registry.register(TOAD);
    	registry.register(ZOMBIE_COWMAN);
    }
    
    public static void registerRenders()
    {
    	RenderingRegistry.registerEntityRenderingHandler(EntityChincho.class, RenderChincho::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBlock.class, RenderFlyingBlock::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityZombieCowman.class, RenderZombieCowman::new);
    }
}
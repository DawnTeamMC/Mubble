package hugman.mod.init;

import hugman.mod.Mubble;
import hugman.mod.objects.entity.EntityChincho;
import hugman.mod.objects.entity.EntityCustomTNT;
import hugman.mod.objects.entity.EntityFlyingBlock;
import hugman.mod.objects.entity.EntityGoomba;
import hugman.mod.objects.entity.EntityToad;
import hugman.mod.objects.entity.EntityZombieCowman;
import hugman.mod.objects.entity.render.RenderChincho;
import hugman.mod.objects.entity.render.RenderCustomTNT;
import hugman.mod.objects.entity.render.RenderFlyingBlock;
import hugman.mod.objects.entity.render.RenderGoomba;
import hugman.mod.objects.entity.render.RenderToad;
import hugman.mod.objects.entity.render.RenderZombieCowman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class MubbleEntities
{
	public static final EntityType<EntityChincho> CHINCHO = register("chincho", EntityType.Builder.create(EntityChincho::new, EntityClassification.MONSTER).size(0.6F, 1.2F));
	public static final EntityType<EntityGoomba> GOOMBA = register("goomba", EntityType.Builder.create(EntityGoomba::new, EntityClassification.MONSTER).size(0.9375F, 0.9375F));
	public static final EntityType<EntityToad> TOAD = register("toad", EntityType.Builder.create(EntityToad::new, EntityClassification.CREATURE).size(0.6F, 1.4F));
	public static final EntityType<EntityZombieCowman> ZOMBIE_COWMAN = register("zombie_cowman", EntityType.Builder.create(EntityZombieCowman::new, EntityClassification.MONSTER).size(0.6F, 1.95F));

	public static final EntityType<EntityCustomTNT> CUSTOM_TNT = register("custom_tnt", EntityType.Builder.<EntityCustomTNT>create(EntityCustomTNT::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	public static final EntityType<EntityFlyingBlock> FLYING_BLOCK = register("flying_block", EntityType.Builder.<EntityFlyingBlock>create(EntityFlyingBlock::new, EntityClassification.MISC).size(0.98F, 0.98F).setTrackingRange(128).setUpdateInterval(1));
	
	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder)
	{
	    EntityType<T> entitytype = builder.build(Mubble.MOD_PREFIX + id);
	    entitytype.setRegistryName(Mubble.MOD_ID, id);
		return entitytype;
	}
	
    public static void registerEntities(IForgeRegistry<EntityType<?>> registry)
    {
    	registry.register(CHINCHO);
    	registry.register(TOAD);
    	registry.register(GOOMBA);
    	registry.register(ZOMBIE_COWMAN);

    	registry.register(CUSTOM_TNT);
    	registry.register(FLYING_BLOCK);
    }
    
    public static void registerRenders()
    {
    	RenderingRegistry.registerEntityRenderingHandler(EntityChincho.class, RenderChincho::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityGoomba.class, RenderGoomba::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityZombieCowman.class, RenderZombieCowman::new);

    	RenderingRegistry.registerEntityRenderingHandler(EntityCustomTNT.class, RenderCustomTNT::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBlock.class, RenderFlyingBlock::new);
    }
}
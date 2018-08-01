package hugman.mod.util.handlers;

import hugman.mod.entity.EntityToad;
import hugman.mod.entity.render.RenderToad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler 
{
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, new IRenderFactory<EntityToad>()
		{
			@Override
			public Render<? super EntityToad> createRenderFor(RenderManager manager) 
			{
				return new RenderToad(manager);
			}
		});
	}
}
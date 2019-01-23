package hugman.mod.proxy;

import hugman.mod.entity.EntityChincho;
import hugman.mod.entity.EntityFlyingBlock;
import hugman.mod.entity.EntityToad;
import hugman.mod.entity.render.RenderChincho;
import hugman.mod.entity.render.RenderFlyingBlock;
import hugman.mod.entity.render.RenderToad;
import hugman.mod.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void registerVariantRenderer(Item item, int meta, String filename, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, filename), id));
	}
	
	@Override
	public void registerEntityRenderers() 
    {
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityChincho.class, RenderChincho::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBlock.class, RenderFlyingBlock::new);
    }
}

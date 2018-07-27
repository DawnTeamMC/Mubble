package hugman.mod.entity.render;

import hugman.mod.entity.EntityToad;
import hugman.mod.entity.model.ModelToad;
import hugman.mod.util.Reference;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderToad extends RenderLiving<EntityToad>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/toad.png");
	
	public RenderToad(RenderManager manager) 
	{
		super(manager, new ModelToad(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityToad entity) 
	{
		return TEXTURES;
	}

	@Override
	protected void applyRotations(EntityToad entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}

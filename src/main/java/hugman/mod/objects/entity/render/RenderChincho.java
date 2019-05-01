package hugman.mod.objects.entity.render;

import hugman.mod.Mubble;
import hugman.mod.objects.entity.EntityChincho;
import hugman.mod.objects.entity.render.model.ModelChincho;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderChincho extends RenderLiving<EntityChincho>
{
    private static final ResourceLocation CHINCHO_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/chincho.png");
	
	public RenderChincho(RenderManager manager) 
	{
		super(manager, new ModelChincho(), 0.5F);
	}
	
	protected ResourceLocation getEntityTexture(EntityChincho entity)
    {
		return CHINCHO_TEXTURES;
    }

	@Override
	protected void applyRotations(EntityChincho entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
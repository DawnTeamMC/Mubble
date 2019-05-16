package hugman.mod.objects.entity.render;

import hugman.mod.Mubble;
import hugman.mod.objects.entity.EntityGoomba;
import hugman.mod.objects.entity.render.model.ModelGoomba;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGoomba extends RenderLiving<EntityGoomba>
{
    private static final ResourceLocation NORMAL_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/normal.png");
    //private static final ResourceLocation GOLDEN_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/golden.png");
	
	public RenderGoomba(RenderManager manager) 
	{
		super(manager, new ModelGoomba(), 0.5F);
	}
	
	protected ResourceLocation getEntityTexture(EntityGoomba entity)
    {
		return NORMAL_GOOMBA_TEXTURES;
    }

	@Override
	protected void applyRotations(EntityGoomba entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
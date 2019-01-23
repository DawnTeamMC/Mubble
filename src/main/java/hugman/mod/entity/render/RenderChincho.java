package hugman.mod.entity.render;

import java.util.Map;

import com.google.common.collect.Maps;

import hugman.mod.entity.EntityChincho;
import hugman.mod.entity.model.ModelChincho;
import hugman.mod.util.Reference;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderChincho extends RenderLiving<EntityChincho>
{
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.<String, ResourceLocation>newHashMap();
    private static final ResourceLocation CHINCHO_TEXTURES = new ResourceLocation(Reference.MODID + ":textures/entity/chincho.png");
	
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

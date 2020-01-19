package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ZombieCowmanEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZombieCowmanRenderer extends BipedRenderer<ZombieCowmanEntity, ZombieModel<ZombieCowmanEntity>>
{
	private static final ResourceLocation ZOMBIE_COWMAN_TEXTURE = new ResourceLocation(Mubble.MOD_PREFIX + "textures/entity/zombie_cowman.png");
	
	public ZombieCowmanRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new ZombieModel<>(0.0F, false), 0.5F);
		this.addLayer(new BipedArmorLayer<>(this, new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)));
	}
	
	@Override
	public ResourceLocation getEntityTexture(ZombieCowmanEntity entity)
	{
		return ZOMBIE_COWMAN_TEXTURE;
	}
}
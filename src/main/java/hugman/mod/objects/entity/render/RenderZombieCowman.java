package hugman.mod.objects.entity.render;

import hugman.mod.Mubble;
import hugman.mod.objects.entity.EntityZombieCowman;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.model.ModelZombie;
import net.minecraft.util.ResourceLocation;

public class RenderZombieCowman extends RenderBiped<EntityZombieCowman>
{
	private static final ResourceLocation ZOMBIE_COWMAN_TEXTURE = new ResourceLocation(Mubble.MOD_PREFIX + "textures/entity/zombie_cowman.png");
	
	public RenderZombieCowman(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelZombie(), 0.5F);
		this.addLayer
		(
			new LayerBipedArmor(this)
			{
				protected void initArmor()
				{
					this.modelLeggings = new ModelZombie(0.5F, true);
					this.modelArmor = new ModelZombie(1.0F, true);
				}
			}
		);
   }
   
   protected ResourceLocation getEntityTexture(EntityZombieCowman entity)
   {
      return ZOMBIE_COWMAN_TEXTURE;
   }
}
package hugman.mubble.objects.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import hugman.mubble.objects.entity.CustomTNTEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomTNTRenderer extends EntityRenderer<CustomTNTEntity>
{
	public CustomTNTRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}
	
	@Override
	public void render(CustomTNTEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack matrix, IRenderTypeBuffer buffer, int p_225623_6_)
	{
		BlockState state = entity.getCustomTile();
		matrix.push();
		matrix.translate(0.0D, 0.5D, 0.0D);
		if ((float) entity.getFuse() - p_225623_3_ + 1.0F < 10.0F) {
			float f = 1.0F - ((float) entity.getFuse() - p_225623_3_ + 1.0F) / 10.0F;
			f = MathHelper.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			matrix.scale(f1, f1, f1);
		}

		matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
		matrix.translate(-0.5D, -0.5D, 0.5D);
		matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
		TNTMinecartRenderer.func_229127_a_(state, matrix, buffer, p_225623_6_, entity.getFuse() / 5 % 2 == 0);
		matrix.pop();
		super.render(entity, p_225623_2_, p_225623_3_, matrix, buffer, p_225623_6_);
	}
	
	@Override
	public ResourceLocation getEntityTexture(CustomTNTEntity entity)
	{
		return PlayerContainer.BLOCK_ATLAS_TEXTURE;
	}
}
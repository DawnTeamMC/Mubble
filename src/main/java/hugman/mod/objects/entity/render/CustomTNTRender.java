package hugman.mod.objects.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mod.objects.entity.CustomTNTEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomTNTRender extends EntityRenderer<CustomTNTEntity>
{
	public CustomTNTRender(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}
	
	@Override
	public void doRender(CustomTNTEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		BlockState blockState = entity.getBlockState();
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float)x, (float)y + 0.5F, (float)z);
		if ((float)entity.getFuse() - partialTicks + 1.0F < 10.0F)
		{
			float f = 1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 10.0F;
			f = MathHelper.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			GlStateManager.scalef(f1, f1, f1);
		}

		float f2 = (1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 100.0F) * 0.8F;
		this.bindEntityTexture(entity);
		GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness(blockState, entity.getBrightness());
		GlStateManager.translatef(0.0F, 0.0F, 1.0F);
		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
			blockrendererdispatcher.renderBlockBrightness(blockState, 1.0F);
			GlStateManager.tearDownSolidRenderingTextureCombine();
			GlStateManager.disableColorMaterial();
		}
		else if (entity.getFuse() / 5 % 2 == 0)
		{
			GlStateManager.disableTexture();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, f2);
			GlStateManager.polygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderBlockBrightness(blockState, 1.0F);
			GlStateManager.polygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(CustomTNTEntity entity)
	{
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
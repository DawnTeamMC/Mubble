package hugman.mubble.objects.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.objects.entity.CustomTNTEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CustomTNTRenderer extends EntityRenderer<CustomTNTEntity>
{
	public CustomTNTRenderer(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
		this.shadowSize = 0.5F;
	}
	
	@Override
	public void render(CustomTNTEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light)
	{
		BlockState blockState = entity.getBlockState();
		BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) entity.getX(), (float) entity.getY() + 0.5F, (float) entity.getZ());
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
		this.getTexture(entity);
		GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
		blockrendererdispatcher.renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, light, (int) entity.getBrightnessAtEyes());
		GlStateManager.translatef(0.0F, 0.0F, 1.0F);
		if (this.getRenderManager().shouldRenderHitboxes())
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.setupOverlayColor(entity.getTeamColorValue(), 1);
			blockrendererdispatcher.renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, light, 1);
			GlStateManager.teardownOverlayColor();
			GlStateManager.disableColorMaterial();
		}
		else if (entity.getFuse() / 5 % 2 == 0)
		{
			GlStateManager.disableTexture();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.DST_ALPHA.value);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, f2);
			GlStateManager.polygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, light, 1);
			GlStateManager.polygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture();
		}

		GlStateManager.popMatrix();
		super.render(entity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(CustomTNTEntity entity)
	{
		return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
	}
}
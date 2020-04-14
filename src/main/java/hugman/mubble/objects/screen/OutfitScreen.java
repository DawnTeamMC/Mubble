package hugman.mubble.objects.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import hugman.mubble.objects.inventory.container.OutfitContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OutfitScreen extends ContainerScreen<OutfitContainer>
{
	private float oldMouseX;
	private float oldMouseY;
	
	public OutfitScreen(PlayerEntity playerIn)
	{
		super(new OutfitContainer(playerIn), playerIn.inventory, new TranslationTextComponent("container.outfit"));
		this.passEvents = true;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		this.font.drawString(this.title.getFormattedText(), 97.0F, 8.0F, 4210752);
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		this.renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
		this.oldMouseX = (float) p_render_1_;
		this.oldMouseY = (float) p_render_2_;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		drawEntity(i + 51, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player);
	}
	
	public static void drawEntity(int p_228187_0_, int p_228187_1_, int p_228187_2_, float p_228187_3_, float p_228187_4_, LivingEntity entity)
	{
		float f = (float) Math.atan((double) (p_228187_3_ / 40.0F));
		float f1 = (float) Math.atan((double) (p_228187_4_ / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) p_228187_0_, (float) p_228187_1_, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) p_228187_2_, (float) p_228187_2_, (float) p_228187_2_);
		Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
		Quaternion quaternion1 = Vector3f.POSITIVE_X.getDegreesQuaternion(f1 * 20.0F);
		quaternion.multiply(quaternion1);
		matrixstack.multiply(quaternion);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		entity.renderYawOffset = 180.0F + f * 20.0F;
		entity.rotationYaw = 180.0F + f * 40.0F;
		entity.rotationPitch = -f1 * 20.0F;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setRotation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getBufferBuilders().getEntityVertexConsumers();
		entityrenderermanager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
		irendertypebuffer$impl.draw();
		entityrenderermanager.setRenderShadow(true);
		entity.renderYawOffset = f2;
		entity.rotationYaw = f3;
		entity.rotationPitch = f4;
		entity.prevRotationYawHead = f5;
		entity.rotationYawHead = f6;
		RenderSystem.popMatrix();
	}
}
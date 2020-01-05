package hugman.mubble.objects.entity.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelUtil;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GoombaModel<T extends Entity> extends EntityModel<T>
{
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart top;
	private final ModelPart bottom;
	private final ModelPart left_foot;
	private final ModelPart right_foot;

	public GoombaModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, 21.0F, 0.0F);
		head = new ModelPart(this);
		head.setPivot(0.0F, -2.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 11).addCuboid(-2.5F, 1.0F, -2.5F, 5, 3, 5, 0.0F, false);
		top = new ModelPart(this);
		top.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(top);
		top.setTextureOffset(32, 0).addCuboid(-3.5F, -5.0F, -3.5F, 7, 4, 7, 0.0F, false);
		bottom = new ModelPart(this);
		bottom.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(bottom);
		bottom.setTextureOffset(0, 0).addCuboid(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F, false);
		left_foot = new ModelPart(this);
		left_foot.setPivot(0.0F, 23.0F, 0.0F);
		ModelUtil.interpolateAngle(0.0F, -0.1745F, 0.0F);
		left_foot.setTextureOffset(0, 19).addCuboid(1.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false);
		right_foot = new ModelPart(this);
		right_foot.setPivot(0.0F, 23.0F, 0.0F);
		ModelUtil.interpolateAngle(0.0F, 0.1745F, 0.0F);
		right_foot.setTextureOffset(20, 11).addCuboid(-4.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha)
	{
        matrices.push();
        matrices.scale(1.5F, 1.5F, 1.5F);
        matrices.translate(0.0F, -7.75F, 0.0F);
		this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.left_foot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.right_foot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        matrices.pop();
	}
	
    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch)
    {
    	left_foot.pivotX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	right_foot.pivotX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    	body.pivotY = netHeadYaw * 0.017453292F;
    	body.pivotX = headPitch * 0.017453292F;
    }
}
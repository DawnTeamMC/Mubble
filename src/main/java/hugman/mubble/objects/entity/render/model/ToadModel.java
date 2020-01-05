package hugman.mubble.objects.entity.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ToadModel<T extends Entity> extends EntityModel<T>
{
	public ModelPart head;
    public ModelPart hat;
    public ModelPart front_lamp;
    public ModelPart body;
    public ModelPart bagpack;
    public ModelPart right_arm;
    public ModelPart left_arm;
    public ModelPart right_leg;
    public ModelPart left_leg;

    public ToadModel()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new ModelPart(this, 0, 0);
        this.head.setPivot(0.0F, 8.0F, 0.0F);
        this.head.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.hat = new ModelPart(this, 0, 29);
        this.hat.setPivot(0.0F, 8.0F, 0.0F);
        this.hat.addCuboid(-5.0F, -10.0F, -5.0F, 10, 5, 10, 0.0F);
        this.front_lamp = new ModelPart(this, 30, 33);
        this.front_lamp.setPivot(0.0F, 8.0F, 0.0F);
        this.front_lamp.addCuboid(-2.0F, -10.0F, -7.0F, 4, 4, 2, 0.0F);
        this.body = new ModelPart(this, 16, 16);
        this.body.setPivot(0.0F, 8.0F, 0.0F);
        this.body.addCuboid(-4.0F, 0.0F, -2F, 8, 9, 4, 0.0F);
        this.bagpack = new ModelPart(this, 0, 44);
        this.bagpack.setPivot(0.0F, 8.0F, 0.0F);
        this.bagpack.addCuboid(-3.0F, 0.0F, 2.0F, 6, 8, 4, 0.0F);
        this.right_arm = new ModelPart(this, 40, 16);
        this.right_arm.setPivot(-5.0F, 10.0F, 0.0F);
        this.right_arm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.left_arm = new ModelPart(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setPivot(5.0F, 10.0F, 0.0F);
        this.left_arm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.right_leg = new ModelPart(this, 0, 16);
        this.right_leg.setPivot(-2F, 17.0F, 0F);
        this.right_leg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.left_leg = new ModelPart(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setPivot(2F, 17.0F, 0.0F);
        this.left_leg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha)
    { 
    	if (this.child)
        {
        	matrices.push();
        	matrices.scale(0.75F, 0.75F, 0.75F);
        	matrices.translate(0.0F, 13.0F, 0.0F);
        }
        this.head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.hat.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.front_lamp.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        if (this.child)
        {
        	matrices.pop();
            matrices.push();
            matrices.scale(0.5F, 0.5F, 0.5F);
            matrices.translate(0.0F, 24.0F, 0.0F);
        }
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.bagpack.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        if (this.child) matrices.pop();
    }
    
    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch)
    {
        //this.body.pivotY = MathHelper.sin(MathHelper.sqrt(this.swingProgress) * ((float) Math.PI * 2F)) * 0.2F;
    	this.left_leg.pivotX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	this.right_leg.pivotX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
    	this.left_arm.pivotX = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * 1.4F * limbSwingAmount;
    	this.right_arm.pivotX = MathHelper.cos(limbSwing * 0.8F) * 1.4F * limbSwingAmount;
    	
    	this.head.pivotY = netHeadYaw * 0.017453292F;
    	this.head.pivotX = headPitch * 0.017453292F;
    	this.hat.pivotY = netHeadYaw * 0.017453292F;
    	this.hat.pivotX = headPitch * 0.017453292F;
    	this.front_lamp.pivotY = netHeadYaw * 0.017453292F;
    	this.front_lamp.pivotX = headPitch * 0.017453292F;
    }
}
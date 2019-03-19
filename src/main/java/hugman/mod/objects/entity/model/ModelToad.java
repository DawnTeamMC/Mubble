package hugman.mod.objects.entity.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelToad extends ModelBase
{
	public ModelRenderer head;
    public ModelRenderer hat;
    public ModelRenderer front_lamp;
    public ModelRenderer body;
    public ModelRenderer bagpack;
    public ModelRenderer right_arm;
    public ModelRenderer left_arm;
    public ModelRenderer right_leg;
    public ModelRenderer left_leg;

    public ModelToad()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.hat = new ModelRenderer(this, 0, 29);
        this.hat.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.hat.addBox(-5.0F, -10.0F, -5.0F, 10, 5, 10, 0.0F);
        this.front_lamp = new ModelRenderer(this, 30, 33);
        this.front_lamp.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.front_lamp.addBox(-2.0F, -10.0F, -7.0F, 4, 4, 2, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2F, 8, 9, 4, 0.0F);
        this.bagpack = new ModelRenderer(this, 0, 44);
        this.bagpack.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.bagpack.addBox(-3.0F, 0.0F, 2.0F, 6, 8, 4, 0.0F);
        this.right_arm = new ModelRenderer(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 10.0F, 0.0F);
        this.right_arm.addBox(-2.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.left_arm = new ModelRenderer(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 10.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.right_leg = new ModelRenderer(this, 0, 16);
        this.right_leg.setRotationPoint(-2F, 17.0F, 0F);
        this.right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.left_leg = new ModelRenderer(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(2F, 17.0F, 0.0F);
        this.left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    { 
    	if (this.isChild)
        {
        	GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 8.0F * scale, 0.0F);
        }
        this.head.render(scale);
        this.hat.render(scale);
        this.front_lamp.render(scale);
        if (this.isChild)
        {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
        }
        this.body.render(scale);
        this.bagpack.render(scale);
        this.right_arm.render(scale);
        this.left_arm.render(scale);
        this.right_leg.render(scale);
        this.left_leg.render(scale);
        if (this.isChild) GlStateManager.popMatrix();
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) 
    {
        //this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(this.swingProgress) * ((float)Math.PI * 2F)) * 0.2F;
    	this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    	this.left_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.8F + (float)Math.PI) * 1.4F * limbSwingAmount;
    	this.right_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.8F) * 1.4F * limbSwingAmount;
    	
    	this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.head.rotateAngleX = headPitch * 0.017453292F;
    	this.hat.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.hat.rotateAngleX = headPitch * 0.017453292F;
    	this.front_lamp.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.front_lamp.rotateAngleX = headPitch * 0.017453292F;
    }
}
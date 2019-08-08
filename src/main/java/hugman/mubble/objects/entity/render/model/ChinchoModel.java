package hugman.mubble.objects.entity.render.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ChinchoModel<T extends Entity> extends EntityModel<T>
{
    public RendererModel head;
    public RendererModel body;
    public RendererModel right_arm;
    public RendererModel left_arm;
    public RendererModel right_leg;
    public RendererModel left_leg;
    public RendererModel right_teeth;
    public RendererModel middle_teeth;
    public RendererModel left_teeth;

    public ChinchoModel()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new RendererModel(this, 0, 0);
        this.head.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.body = new RendererModel(this, 16, 16);
        this.body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.body.addBox(-4.0F, 5.0F, -2F, 8, 7, 4, 0.0F);
        this.right_teeth = new RendererModel(this, 0, 24);
        this.right_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.right_teeth.addBox(1.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.middle_teeth = new RendererModel(this, 0, 34);
        this.middle_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.middle_teeth.addBox(-1.5F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.left_teeth = new RendererModel(this, 0, 29);
        this.left_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.left_teeth.addBox(-4.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.right_arm = new RendererModel(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 15.0F, 0.0F);
        this.right_arm.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
        this.left_arm = new RendererModel(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 15.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
        this.right_leg = new RendererModel(this, 0, 16);
        this.right_leg.setRotationPoint(-2F, 17.0F, 0.0F);
        this.right_leg.addBox(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
        this.left_leg = new RendererModel(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(2F, 17.0F, 0.0F);
        this.left_leg.addBox(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    { 
        this.right_arm.render(f5);
        this.left_arm.render(f5);
        this.left_leg.render(f5);
        this.middle_teeth.render(f5);
        this.body.render(f5);
        this.right_leg.render(f5);
        this.right_teeth.render(f5);
        this.head.render(f5);
        this.left_teeth.render(f5);
    }
    
    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        this.right_arm.rotateAngleX = 3.7699115F;
        this.left_arm.rotateAngleX = 3.7699115F;
        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.right_leg.rotateAngleY = 0.0F;
        this.left_leg.rotateAngleY = 0.0F;
        this.right_leg.rotateAngleZ = 0.0F;
        this.left_leg.rotateAngleZ = 0.0F;
        
    	this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.head.rotateAngleX = headPitch * 0.017453292F;
    	this.right_teeth.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.right_teeth.rotateAngleX = headPitch * 0.017453292F;
    	this.middle_teeth.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.middle_teeth.rotateAngleX = headPitch * 0.017453292F;
    	this.left_teeth.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.left_teeth.rotateAngleX = headPitch * 0.017453292F;
    }
}
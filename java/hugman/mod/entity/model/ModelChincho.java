package hugman.mod.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelChincho extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer right_arm;
    public ModelRenderer left_arm;
    public ModelRenderer right_leg;
    public ModelRenderer left_leg;
    public ModelRenderer right_teeth;
    public ModelRenderer middle_teeth;
    public ModelRenderer left_teeth;

    public ModelChincho() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.body.addBox(-4.0F, 5.0F, -2F, 8, 7, 4, 0.0F);
        this.right_teeth = new ModelRenderer(this, 0, 24);
        this.right_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.right_teeth.addBox(1.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.middle_teeth = new ModelRenderer(this, 0, 34);
        this.middle_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.middle_teeth.addBox(-1.5F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.left_teeth = new ModelRenderer(this, 0, 29);
        this.left_teeth.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.left_teeth.addBox(-4.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
        this.right_arm = new ModelRenderer(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 15.0F, 0.0F);
        this.right_arm.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
        this.left_arm = new ModelRenderer(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 15.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
        this.right_leg = new ModelRenderer(this, 0, 16);
        this.right_leg.setRotationPoint(-2F, 17.0F, 0.0F);
        this.right_leg.addBox(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
        this.left_leg = new ModelRenderer(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(2F, 17.0F, 0.0F);
        this.left_leg.addBox(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
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

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) 
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

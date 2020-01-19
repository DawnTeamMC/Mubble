package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToadModel<T extends LivingEntity> extends BipedModel<T>
{
    public ModelRenderer hat;
    public ModelRenderer frontLamp;
    public ModelRenderer bagpack;

    public ToadModel()
    {
        super(0.0F, 8.0F, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.hat = new ModelRenderer(this, 0, 29);
        this.hat.addCuboid(-5.0F, -10.0F, -5.0F, 10, 5, 10, 0.0F);
        this.hat.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.frontLamp = new ModelRenderer(this, 30, 33);
        this.frontLamp.addCuboid(-2.0F, -10.0F, -7.0F, 4, 4, 2, 0.0F);
        this.frontLamp.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addCuboid(-4.0F, 0.0F, -2F, 8, 9, 4, 0.0F);
        this.bipedBody.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.bagpack = new ModelRenderer(this, 0, 44);
        this.bagpack.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.bagpack.addCuboid(-3.0F, 0.0F, 2.0F, 6, 8, 4, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.bipedRightArm.setRotationPoint(-5.0F, 10.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
        this.bipedLeftArm.setRotationPoint(5.0F, 10.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.bipedRightLeg.setRotationPoint(-2F, 17.0F, 0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.bipedLeftLeg.setRotationPoint(2F, 17.0F, 0.0F);
    }
    
    @Override
    protected Iterable<ModelRenderer> getHeadParts()
    {
    	return ImmutableList.of(this.bipedHead, this.hat, this.frontLamp);
    }
    
    @Override
    protected Iterable<ModelRenderer> getBodyParts()
    {
    	return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg, this.bipedHeadwear, this.bagpack);
    }
    
    @Override
    public void setAngles(T entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) 
    {
    	super.setAngles(entity, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
		boolean flag = entity.getTicksElytraFlying() > 4;
		boolean flag1 = entity.isActualySwimming();
		this.hat.rotateAngleY = p_225597_5_ * ((float) Math.PI / 180F);
		this.frontLamp.rotateAngleY = p_225597_5_ * ((float) Math.PI / 180F);
		if(flag)
		{
			this.hat.rotateAngleX = (-(float) Math.PI / 4F);
			this.frontLamp.rotateAngleX = (-(float) Math.PI / 4F);
		}
		else if(this.swimAnimation > 0.0F)
		{
			if(flag1)
			{
				this.hat.rotateAngleX = this.func_205060_a(this.hat.rotateAngleX, (-(float) Math.PI / 4F), this.swimAnimation);
				this.frontLamp.rotateAngleX = this.func_205060_a(this.hat.rotateAngleX, (-(float) Math.PI / 4F), this.swimAnimation);
			}
			else
			{
				this.hat.rotateAngleX = this.func_205060_a(this.hat.rotateAngleX, p_225597_6_ * ((float) Math.PI / 180F), this.swimAnimation);
				this.frontLamp.rotateAngleX = this.func_205060_a(this.hat.rotateAngleX, p_225597_6_ * ((float) Math.PI / 180F), this.swimAnimation);
			}
		}
		else
		{
			this.hat.rotateAngleX = p_225597_6_ * ((float) Math.PI / 180F);
			this.frontLamp.rotateAngleX = p_225597_6_ * ((float) Math.PI / 180F);
		}
    }
}
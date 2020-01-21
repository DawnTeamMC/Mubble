package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChinchoModel<T extends LivingEntity> extends BipedModel<T>
{
    public ModelRenderer rightTeeth;
    public ModelRenderer middleTeeth;
    public ModelRenderer leftTeeth;

    public ChinchoModel()
    {
        super(0.0F, 8.0F, 64, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addCuboid(-4.0F, 5.0F, -2.0F, 8.0F, 7.0F, 4.0F, 0.0F);
        this.rightTeeth = new ModelRenderer(this, 0, 24);
        this.rightTeeth.addCuboid(1.8F, -2.0F, -5.0F, 3.0F, 3.0F, 2.0F, 0.0F);
        this.middleTeeth = new ModelRenderer(this, 0, 34);
        this.middleTeeth.addCuboid(-1.5F, -2.0F, -5.0F, 3.0F, 3.0F, 2.0F, 0.0F);
        this.leftTeeth = new ModelRenderer(this, 0, 29);
        this.leftTeeth.addCuboid(-4.8F, -2.0F, -5.0F, 3.0F, 3.0F, 2.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addCuboid(-2.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addCuboid(-2.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F);
    }
    
    @Override
    protected Iterable<ModelRenderer> getHeadParts()
    {
        return ImmutableList.of(this.bipedHead, this.rightTeeth, this.middleTeeth, this.leftTeeth);
    }
    
    @Override
    protected Iterable<ModelRenderer> getBodyParts()
    {
        return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg, this.bipedHeadwear);
    }
    
    @Override
    public void setAngles(T entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
    {
    	super.setAngles(entity, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
        this.bipedHead.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.bipedBody.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.rightTeeth.copyModelAngles(this.bipedHead);
        this.middleTeeth.copyModelAngles(this.bipedHead);
        this.leftTeeth.copyModelAngles(this.bipedHead);
        this.bipedRightArm.setRotationPoint(-5.0F, 15.0F, 0.0F);
        this.bipedLeftArm.setRotationPoint(5.0F, 15.0F, 0.0F);
        this.bipedRightLeg.setRotationPoint(-2F, 17.0F, 0.0F);
        this.bipedLeftLeg.setRotationPoint(2F, 17.0F, 0.0F);
    	
    }
}
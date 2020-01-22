package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GoombaModel<T extends Entity> extends AgeableModel<T>
{
	private final ModelRenderer headTop;
	private final ModelRenderer headBottom;
	private final ModelRenderer body;
	private final ModelRenderer leftFoot;
	private final ModelRenderer rightFoot;

	public GoombaModel()
	{
		super(false, 24.0F, 0.0F);
		textureWidth = 64;
		textureHeight = 64;
		
		headTop = new ModelRenderer(this,32, 0);
		headTop.addCuboid(-3.5F, -10.0F, -3.5F, 7.0F, 4.0F, 7.0F, 0.0F);
		headBottom = new ModelRenderer(this, 0, 0);
		headBottom.addCuboid(-4.0F, -6.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F);
		body = new ModelRenderer(this, 0, 11);
		body.addCuboid(-2.5F, -4.0F, -2.5F, 5.0F, 3.0F, 5.0F, 0.0F);
		rightFoot = new ModelRenderer(this, 20, 11);
		rightFoot.rotateAngleY = 0.1745F;
		rightFoot.addCuboid(-1.0F, -1.0F, -4.0F, 3.0F, 2.0F, 5.0F, 0.0F);
		leftFoot = new ModelRenderer(this, 0, 19);
		leftFoot.rotateAngleY = -0.1745F;
		leftFoot.addCuboid(-1.0F, -1.0F, -4.0F, 3.0F, 2.0F, 5.0F, 0.0F);
	}
	
	@Override
	protected Iterable<ModelRenderer> getHeadParts()
	{
    	return ImmutableList.of(headTop, headBottom, body);
	}
	
	@Override
	protected Iterable<ModelRenderer> getBodyParts()
	{
    	return ImmutableList.of(leftFoot, rightFoot);
	}
	
	@Override
	public void setAngles(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
	{
		getHeadParts().forEach((part) ->
		{
			part.setRotationPoint(0.0F, 24.0F, 0.0F);
			part.rotateAngleY = p_225597_5_ * ((float)Math.PI / 180F);
	        part.rotateAngleX = p_225597_6_ * ((float)Math.PI / 180F);
		});
		rightFoot.setRotationPoint(-2.5F, 23.0F, 0.5F);
		leftFoot.setRotationPoint(2.5F, 23.0F, 0.5F);
		rightFoot.rotateAngleX = MathHelper.cos(p_225597_2_ * 0.6662F) * 1.4F * p_225597_3_;
		leftFoot.rotateAngleX = MathHelper.cos(p_225597_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_225597_3_;
	}
}
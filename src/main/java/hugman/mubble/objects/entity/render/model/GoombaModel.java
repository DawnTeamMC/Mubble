package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
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
		this.textureWidth = 64;
		this.textureHeight = 64;
		
		this.headTop = new ModelRenderer(this, 0, 11);
		this.headTop.addCuboid(-2.5F, 1.0F, -2.5F, 5, 3, 5, 0.0F);
		this.headBottom = new ModelRenderer(this, 32, 0);
		this.headBottom.addCuboid(-3.5F, -5.0F, -3.5F, 7, 4, 7, 0.0F);
		this.body = new ModelRenderer(this, 0, 0);
		this.body.addCuboid(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F);
		this.leftFoot = new ModelRenderer(this, 0, 19);
		this.leftFoot.addCuboid(1.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F);
		this.rightFoot = new ModelRenderer(this, 20, 11);
		this.rightFoot.addCuboid(-4.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F);
	}
	
	@Override
	protected Iterable<ModelRenderer> getHeadParts()
	{
    	return ImmutableList.of(this.headTop, this.headBottom);
	}
	
	@Override
	protected Iterable<ModelRenderer> getBodyParts()
	{
    	return ImmutableList.of(this.body, this.leftFoot, this.rightFoot);
	}
	
	@Override
	public void setAngles(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
	{
		this.body.setRotationPoint(0.0F, 21.0F, 0.0F);
		this.headTop.copyModelAngles(body);
		this.headBottom.copyModelAngles(body);
		this.leftFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.rightFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.leftFoot.rotateAngleY = -0.1745F;
		this.rightFoot.rotateAngleY = 0.1745F;
	}
}
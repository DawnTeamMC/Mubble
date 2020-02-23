package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DuckModel<T extends LivingEntity> extends AgeableModel<T>
{
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightWing;
	private final ModelRenderer leftWing;
	private final ModelRenderer beak;
	   
    public DuckModel()
    {
		head = new ModelRenderer(this, 0, 0);
		head.addCuboid(-2.0F, -10.0F, -2.0F, 4.0F, 10.0F, 3.0F, 0.0F);
		head.setRotationPoint(0.0F, 15.0F, -4.0F);
		beak = new ModelRenderer(this, 14, 0);
		beak.addCuboid(-2.0F, -8.0F, -5.0F, 4.0F, 2.0F, 3.0F, 0.0F);
		body = new ModelRenderer(this, 0, 13);
		body.addCuboid(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F);
		body.setRotationPoint(0.0F, 16.0F, 0.0F);
		rightLeg = new ModelRenderer(this, 28, 0);
		rightLeg.addCuboid(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
		rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
		leftLeg = new ModelRenderer(this, 28, 0);
		leftLeg.addCuboid(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
		leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
		rightWing = new ModelRenderer(this, 24, 17);
		rightWing.addCuboid(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F);
		rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
		leftWing = new ModelRenderer(this, 24, 17);
		leftWing.addCuboid(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F);
		leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
	}

	protected Iterable<ModelRenderer> getHeadParts()
	{
		return ImmutableList.of(head, beak);
	}

	protected Iterable<ModelRenderer> getBodyParts()
	{
		return ImmutableList.of(body, rightLeg, leftLeg, rightWing, leftWing);
	}

	public void setAngles(T entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
	{
		head.rotateAngleX = p_225597_6_ * ((float) Math.PI / 180F);
		head.rotateAngleY = p_225597_5_ * ((float) Math.PI / 180F);
		beak.copyModelAngles(head);
		body.rotateAngleX = ((float) Math.PI / 2F);
		rightLeg.rotateAngleX = MathHelper.cos(p_225597_2_ * 0.6662F) * 1.4F * p_225597_3_;
		leftLeg.rotateAngleX = MathHelper.cos(p_225597_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_225597_3_;
		rightWing.rotateAngleZ = p_225597_4_;
		leftWing.rotateAngleZ = -p_225597_4_;
	}
}
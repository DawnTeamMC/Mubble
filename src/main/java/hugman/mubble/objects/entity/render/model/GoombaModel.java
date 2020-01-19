package hugman.mubble.objects.entity.render.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelRenderer.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GoombaModel<T extends Entity> extends EntityModel<T>
{
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer top;
	private final ModelRenderer bottom;
	private final ModelRenderer left_foot;
	private final ModelRenderer right_foot;

	public GoombaModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 21.0F, 0.0F);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -2.0F, 0.0F);
		body.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 11, -2.5F, 1.0F, -2.5F, 5, 3, 5, 0.0F, false));

		top = new ModelRenderer(this);
		top.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(top);
		top.cubeList.add(new ModelBox(top, 32, 0, -3.5F, -5.0F, -3.5F, 7, 4, 7, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(bottom);
		bottom.cubeList.add(new ModelBox(bottom, 0, 0, -4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F, false));

		left_foot = new ModelRenderer(this);
		left_foot.setRotationPoint(0.0F, 23.0F, 0.0F);
		setRotationAngle(left_foot, 0.0F, -0.1745F, 0.0F);
		left_foot.cubeList.add(new ModelBox(left_foot, 0, 19, 1.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false));

		right_foot = new ModelRenderer(this);
		right_foot.setRotationPoint(0.0F, 23.0F, 0.0F);
		setRotationAngle(right_foot, 0.0F, 0.1745F, 0.0F);
		right_foot.cubeList.add(new ModelBox(right_foot, 20, 11, -4.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		body.render(f5);
		left_foot.render(f5);
		right_foot.render(f5);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) 
    {
    	left_foot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    	right_foot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    	body.rotateAngleY = netHeadYaw * 0.017453292F;
    	body.rotateAngleX = headPitch * 0.017453292F;
    }
}
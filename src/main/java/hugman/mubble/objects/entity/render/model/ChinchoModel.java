package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ChinchoModel<T extends Entity> extends CompositeEntityModel<T>
{
	public ModelPart head;
	public ModelPart body;
	public ModelPart right_arm;
	public ModelPart left_arm;
	public ModelPart right_leg;
	public ModelPart left_leg;
	public ModelPart right_teeth;
	public ModelPart middle_teeth;
	public ModelPart left_teeth;

	public ChinchoModel()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.head = new ModelPart(this, 0, 0);
		this.head.setPivot(0.0F, 13.0F, 0.0F);
		this.head.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.body = new ModelPart(this, 16, 16);
		this.body.setPivot(0.0F, 8.0F, 0.0F);
		this.body.addCuboid(-4.0F, 5.0F, -2F, 8, 7, 4, 0.0F);
		this.right_teeth = new ModelPart(this, 0, 24);
		this.right_teeth.setPivot(0.0F, 13.0F, 0.0F);
		this.right_teeth.addCuboid(1.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.middle_teeth = new ModelPart(this, 0, 34);
		this.middle_teeth.setPivot(0.0F, 13.0F, 0.0F);
		this.middle_teeth.addCuboid(-1.5F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.left_teeth = new ModelPart(this, 0, 29);
		this.left_teeth.setPivot(0.0F, 13.0F, 0.0F);
		this.left_teeth.addCuboid(-4.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.right_arm = new ModelPart(this, 40, 16);
		this.right_arm.setPivot(-5.0F, 15.0F, 0.0F);
		this.right_arm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
		this.left_arm = new ModelPart(this, 40, 16);
		this.left_arm.mirror = true;
		this.left_arm.setPivot(5.0F, 15.0F, 0.0F);
		this.left_arm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
		this.right_leg = new ModelPart(this, 0, 16);
		this.right_leg.setPivot(-2F, 17.0F, 0.0F);
		this.right_leg.addCuboid(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
		this.left_leg = new ModelPart(this, 0, 16);
		this.left_leg.mirror = true;
		this.left_leg.setPivot(2F, 17.0F, 0.0F);
		this.left_leg.addCuboid(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
	}

	@Override
	public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch)
	{
		this.right_arm.pitch = 3.7699115F;
		this.left_arm.pitch = 3.7699115F;
		this.right_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.right_leg.yaw = 0.0F;
		this.left_leg.yaw = 0.0F;
		this.head.yaw = netHeadYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.right_teeth.yaw = netHeadYaw * 0.017453292F;
		this.right_teeth.pitch = headPitch * 0.017453292F;
		this.middle_teeth.yaw = netHeadYaw * 0.017453292F;
		this.middle_teeth.pitch = headPitch * 0.017453292F;
		this.left_teeth.yaw = netHeadYaw * 0.017453292F;
		this.left_teeth.pitch = headPitch * 0.017453292F;
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(this.body, this.head, this.right_arm, this.left_arm, this.right_leg, this.left_leg, this.right_teeth, this.middle_teeth, this.left_teeth);
	}
}
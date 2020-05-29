package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ToadEntityModel<T extends Entity> extends AnimalModel<T>
{
	public ModelPart head;
	public ModelPart hat;
	public ModelPart front_lamp;
	public ModelPart body;
	public ModelPart bagpack;
	public ModelPart right_arm;
	public ModelPart left_arm;
	public ModelPart right_leg;
	public ModelPart left_leg;

	public ToadEntityModel()
	{
		super(true, 13.3F, 0.0F);
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.head = new ModelPart(this, 0, 0);
		this.head.setPivot(0.0F, 8.0F, 0.0F);
		this.head.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.hat = new ModelPart(this, 0, 29);
		this.hat.setPivot(0.0F, 8.0F, 0.0F);
		this.hat.addCuboid(-5.0F, -10.0F, -5.0F, 10, 5, 10, 0.0F);
		this.front_lamp = new ModelPart(this, 30, 33);
		this.front_lamp.setPivot(0.0F, 8.0F, 0.0F);
		this.front_lamp.addCuboid(-2.0F, -10.0F, -7.0F, 4, 4, 2, 0.0F);
		this.body = new ModelPart(this, 16, 16);
		this.body.setPivot(0.0F, 8.0F, 0.0F);
		this.body.addCuboid(-4.0F, 0.0F, -2F, 8, 9, 4, 0.0F);
		this.bagpack = new ModelPart(this, 0, 44);
		this.bagpack.setPivot(0.0F, 8.0F, 0.0F);
		this.bagpack.addCuboid(-3.0F, 0.0F, 2.0F, 6, 8, 4, 0.0F);
		this.right_arm = new ModelPart(this, 40, 16);
		this.right_arm.setPivot(-5.0F, 10.0F, 0.0F);
		this.right_arm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
		this.left_arm = new ModelPart(this, 40, 16);
		this.left_arm.mirror = true;
		this.left_arm.setPivot(5.0F, 10.0F, 0.0F);
		this.left_arm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
		this.right_leg = new ModelPart(this, 0, 16);
		this.right_leg.setPivot(-2F, 17.0F, 0F);
		this.right_leg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
		this.left_leg = new ModelPart(this, 0, 16);
		this.left_leg.mirror = true;
		this.left_leg.setPivot(2F, 17.0F, 0.0F);
		this.left_leg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
	}

	@Override
	public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch)
	{
		//this.body.yaw = MathHelper.sin(MathHelper.sqrt(this.swingProgress) * ((float) Math.PI * 2F)) * 0.2F;
		this.left_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.right_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.left_arm.pitch = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.right_arm.pitch = MathHelper.cos(limbSwing * 0.8F) * 1.4F * limbSwingAmount;
		this.head.yaw = netHeadYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.hat.yaw = netHeadYaw * 0.017453292F;
		this.hat.pitch = headPitch * 0.017453292F;
		this.front_lamp.yaw = netHeadYaw * 0.017453292F;
		this.front_lamp.pitch = headPitch * 0.017453292F;
	}

	@Override
	protected Iterable<ModelPart> getHeadParts()
	{
		return ImmutableList.of(this.head, this.hat, this.front_lamp);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts()
	{
		return ImmutableList.of(this.body, this.bagpack, this.right_arm, this.left_arm, this.right_leg, this.left_leg);
	}
}
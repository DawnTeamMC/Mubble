package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ChinchoEntityModel<T extends Entity> extends CompositeEntityModel<T> {
	public ModelPart head;
	public ModelPart torso;
	public ModelPart rightArm;
	public ModelPart leftArm;
	public ModelPart rightLeg;
	public ModelPart leftLeg;
	public ModelPart rightTooth;
	public ModelPart middleTooth;
	public ModelPart leftTooth;

	public ChinchoEntityModel() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.head = new ModelPart(this, 0, 0);
		this.head.setPivot(0.0F, 13.0F, 0.0F);
		this.head.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.torso = new ModelPart(this, 16, 16);
		this.torso.setPivot(0.0F, 8.0F, 0.0F);
		this.torso.addCuboid(-4.0F, 5.0F, -2F, 8, 7, 4, 0.0F);
		this.rightTooth = new ModelPart(this, 0, 24);
		this.rightTooth.setPivot(0.0F, 13.0F, 0.0F);
		this.rightTooth.addCuboid(1.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.middleTooth = new ModelPart(this, 0, 34);
		this.middleTooth.setPivot(0.0F, 13.0F, 0.0F);
		this.middleTooth.addCuboid(-1.5F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.leftTooth = new ModelPart(this, 0, 29);
		this.leftTooth.setPivot(0.0F, 13.0F, 0.0F);
		this.leftTooth.addCuboid(-4.8F, -2.0F, -5.0F, 3, 3, 2, 0.0F);
		this.rightArm = new ModelPart(this, 40, 16);
		this.rightArm.setPivot(-5.0F, 15.0F, 0.0F);
		this.rightArm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
		this.leftArm = new ModelPart(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setPivot(5.0F, 15.0F, 0.0F);
		this.leftArm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 6, 4, 0.0F);
		this.rightLeg = new ModelPart(this, 0, 16);
		this.rightLeg.setPivot(-2F, 17.0F, 0.0F);
		this.rightLeg.addCuboid(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
		this.leftLeg = new ModelPart(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setPivot(2F, 17.0F, 0.0F);
		this.leftLeg.addCuboid(-2.0F, 3.0F, -2.0F, 4, 4, 4, 0.0F);
	}

	@Override
	public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch) {
		this.rightArm.pitch = 3.7699115F;
		this.leftArm.pitch = 3.7699115F;
		this.rightLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yaw = 0.0F;
		this.leftLeg.yaw = 0.0F;
		this.head.yaw = netHeadYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.rightTooth.yaw = netHeadYaw * 0.017453292F;
		this.rightTooth.pitch = headPitch * 0.017453292F;
		this.middleTooth.yaw = netHeadYaw * 0.017453292F;
		this.middleTooth.pitch = headPitch * 0.017453292F;
		this.leftTooth.yaw = netHeadYaw * 0.017453292F;
		this.leftTooth.pitch = headPitch * 0.017453292F;
	}

	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(this.torso, this.head, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.rightTooth, this.middleTooth, this.leftTooth);
	}
}
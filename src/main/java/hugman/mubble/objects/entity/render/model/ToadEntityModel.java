package hugman.mubble.objects.entity.render.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ToadEntityModel<T extends Entity> extends AnimalModel<T> {
	public ModelPart head;
	public ModelPart mushroom;
	public ModelPart frontLamp;
	public ModelPart torso;
	public ModelPart backpack;
	public ModelPart rightArm;
	public ModelPart leftArm;
	public ModelPart rightLeg;
	public ModelPart leftLeg;

	public ToadEntityModel() {
		super(true, 13.3F, 0.0F);
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.head = new ModelPart(this, 0, 0);
		this.head.setPivot(0.0F, 8.0F, 0.0F);
		this.head.addCuboid(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.mushroom = new ModelPart(this, 0, 29);
		this.mushroom.setPivot(0.0F, 8.0F, 0.0F);
		this.mushroom.addCuboid(-5.0F, -10.0F, -5.0F, 10, 5, 10, 0.0F);
		this.frontLamp = new ModelPart(this, 30, 33);
		this.frontLamp.setPivot(0.0F, 8.0F, 0.0F);
		this.frontLamp.addCuboid(-2.0F, -10.0F, -7.0F, 4, 4, 2, 0.0F);
		this.torso = new ModelPart(this, 16, 16);
		this.torso.setPivot(0.0F, 8.0F, 0.0F);
		this.torso.addCuboid(-4.0F, 0.0F, -2F, 8, 9, 4, 0.0F);
		this.backpack = new ModelPart(this, 0, 44);
		this.backpack.setPivot(0.0F, 8.0F, 0.0F);
		this.backpack.addCuboid(-3.0F, 0.0F, 2.0F, 6, 8, 4, 0.0F);
		this.rightArm = new ModelPart(this, 40, 16);
		this.rightArm.setPivot(-5.0F, 10.0F, 0.0F);
		this.rightArm.addCuboid(-2.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
		this.leftArm = new ModelPart(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setPivot(5.0F, 10.0F, 0.0F);
		this.leftArm.addCuboid(-1.0F, -2.0F, -2.0F, 3, 7, 4, 0.0F);
		this.rightLeg = new ModelPart(this, 0, 16);
		this.rightLeg.setPivot(-2F, 17.0F, 0F);
		this.rightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
		this.leftLeg = new ModelPart(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setPivot(2F, 17.0F, 0.0F);
		this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
	}

	@Override
	public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch) {
		//this.body.yaw = MathHelper.sin(MathHelper.sqrt(this.swingProgress) * ((float) Math.PI * 2F)) * 0.2F;
		this.leftLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leftArm.pitch = MathHelper.cos(limbSwing * 0.8F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightArm.pitch = MathHelper.cos(limbSwing * 0.8F) * 1.4F * limbSwingAmount;
		this.head.yaw = netHeadYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.mushroom.yaw = netHeadYaw * 0.017453292F;
		this.mushroom.pitch = headPitch * 0.017453292F;
		this.frontLamp.yaw = netHeadYaw * 0.017453292F;
		this.frontLamp.pitch = headPitch * 0.017453292F;
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head, this.mushroom, this.frontLamp);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.torso, this.backpack, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}
}
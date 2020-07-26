package com.hugman.mubble.object.entity.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GoombaEntityModel<T extends Entity> extends CompositeEntityModel<T> {
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart top;
	private final ModelPart bottom;
	private final ModelPart leftFoot;
	private final ModelPart rightFoot;

	public GoombaEntityModel() {
		textureWidth = 64;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, 21.0F, 0.0F);
		head = new ModelPart(this);
		head.setPivot(0.0F, -2.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 11).addCuboid(-2.5F, 1.0F, -2.5F, 5, 3, 5, 0.0F, false);
		top = new ModelPart(this);
		top.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(top);
		top.setTextureOffset(32, 0).addCuboid(-3.5F, -5.0F, -3.5F, 7, 4, 7, 0.0F, false);
		bottom = new ModelPart(this);
		bottom.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(bottom);
		bottom.setTextureOffset(0, 0).addCuboid(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F, false);
		leftFoot = new ModelPart(this);
		leftFoot.setPivot(0.0F, 23.0F, 0.0F);
		setRotationAngle(leftFoot, 0.0F, -0.1745F, 0.0F);
		leftFoot.setTextureOffset(0, 19).addCuboid(1.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false);
		rightFoot = new ModelPart(this);
		rightFoot.setPivot(0.0F, 23.0F, 0.0F);
		setRotationAngle(rightFoot, 0.0F, 0.1745F, 0.0F);
		rightFoot.setTextureOffset(20, 11).addCuboid(-4.0F, -1.0F, -4.0F, 3, 2, 5, 0.0F, false);
	}

	private void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.pitch = x;
		modelPart.yaw = y;
		modelPart.roll = z;
	}

	@Override
	public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch) {
		leftFoot.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		rightFoot.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		body.yaw = netHeadYaw * 0.017453292F;
		body.pitch = headPitch * 0.017453292F;
	}

	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(this.body, this.leftFoot, this.rightFoot);
	}
}
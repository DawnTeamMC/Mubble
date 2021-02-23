package com.hugman.mubble.object.entity.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GoombaEntityModel<T extends Entity> extends CompositeEntityModel<T> {
	private final ModelPart body = new ModelPart(this);
	private final ModelPart head = new ModelPart(this, 0, 11);
	private final ModelPart top = new ModelPart(this, 32, 0);
	private final ModelPart bottom = new ModelPart(this, 0, 0);
	private final ModelPart leftFoot = new ModelPart(this, 0, 19);
	private final ModelPart rightFoot = new ModelPart(this, 20, 11);

	public GoombaEntityModel() {
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.body.setPivot(0.0F, 21.0F, 0.0F);
		this.body.addChild(head);
		this.head.addCuboid(-2.5F, 1.0F, -2.5F, 5, 3, 5);
		this.head.setPivot(0.0F, -2.0F, 0.0F);
		this.head.addChild(top);
		this.head.addChild(bottom);
		this.top.addCuboid(-3.5F, -5.0F, -3.5F, 7, 4, 7);
		this.top.setPivot(0.0F, 0.0F, 0.0F);
		this.bottom.addCuboid(-4.0F, -1.0F, -4.0F, 8, 2, 8);
		this.bottom.setPivot(0.0F, 0.0F, 0.0F);
		this.leftFoot.addCuboid(1.0F, -1.0F, -4.0F, 3, 2, 5);
		this.leftFoot.setPivot(0.0F, 23.0F, 0.0F);
		this.setRotationAngle(leftFoot, 0.0F, -0.1745F, 0.0F);
		this.rightFoot.addCuboid(-4.0F, -1.0F, -4.0F, 3, 2, 5);
		this.rightFoot.setPivot(0.0F, 23.0F, 0.0F);
		this.setRotationAngle(rightFoot, 0.0F, 0.1745F, 0.0F);
	}

	private void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.pitch = x;
		modelPart.yaw = y;
		modelPart.roll = z;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.scale(1.5F, 1.5F, 1.5F);
		matrices.translate(0.0D, -0.5D, 0.0D);
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
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
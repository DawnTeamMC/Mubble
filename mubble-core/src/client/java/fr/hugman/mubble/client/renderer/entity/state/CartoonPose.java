package fr.hugman.mubble.client.renderer.entity.state;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * A cartoon deformation of a living entity, laid over whatever its renderer already does.
 *
 * @see fr.hugman.mubble.client.renderer.entity.CartoonPoseCallback
 */
@Environment(EnvType.CLIENT)
public final class CartoonPose {
    public final Vector3f scale = new Vector3f(1.0F);
    /** Degrees around the vertical axis. */
    public float spin;
    /** Degrees, forwards when positive. */
    public float lean;
    /** Degrees around the axis the entity faces along. */
    public float roll;
    /** Height the lean and the roll turn around, in blocks of an unscaled entity. */
    public float pivot;

    public CartoonPose reset() {
        this.scale.set(1.0F);
        this.spin = 0.0F;
        this.lean = 0.0F;
        this.roll = 0.0F;
        this.pivot = 0.0F;
        return this;
    }

    public CartoonPose copy() {
        var copy = new CartoonPose();
        copy.scale.set(this.scale);
        copy.spin = this.spin;
        copy.lean = this.lean;
        copy.roll = this.roll;
        copy.pivot = this.pivot;
        return copy;
    }

    public CartoonPose scale(Vector3fc factor) {
        this.scale.mul(factor);
        return this;
    }

    public boolean isIdentity() {
        return this.spin == 0.0F && this.lean == 0.0F && this.roll == 0.0F
                && this.scale.x == 1.0F && this.scale.y == 1.0F && this.scale.z == 1.0F;
    }

    public void apply(PoseStack poseStack) {
        if (this.spin != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(this.spin));
        }
        if (this.lean != 0.0F || this.roll != 0.0F) {
            poseStack.translate(0.0F, this.pivot, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-this.lean));
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.roll));
            poseStack.translate(0.0F, -this.pivot, 0.0F);
        }
        poseStack.scale(this.scale.x, this.scale.y, this.scale.z);
    }
}

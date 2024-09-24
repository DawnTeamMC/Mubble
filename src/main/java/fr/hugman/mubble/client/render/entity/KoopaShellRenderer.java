package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.entity.model.KoopaShellModel;
import fr.hugman.mubble.entity.KoopaShellEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KoopaShellRenderer extends EntityRenderer<KoopaShellEntity> {
    protected KoopaShellModel model;

    public KoopaShellRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new KoopaShellModel(context.getPart(MubbleRenderLayers.KOOPA_SHELL));
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(KoopaShellEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0F, -1.501F, 0.0F);

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean showBody = !entity.isInvisible();
        boolean translucent = !showBody && !entity.isInvisibleTo(minecraftClient.player);
        boolean outline = minecraftClient.hasOutline(entity);
        RenderLayer renderLayer = this.getRenderLayer(entity, showBody, translucent, outline);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            int q = OverlayTexture.packUv(OverlayTexture.getU(0.0f), OverlayTexture.getV(false));
            this.model.render(matrices, vertexConsumer, light, q, translucent ? 654311423 : -1);
        }


        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Nullable
    protected RenderLayer getRenderLayer(KoopaShellEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        } else if (showBody) {
            return this.model.getLayer(identifier);
        } else {
            return showOutline ? RenderLayer.getOutline(identifier) : null;
        }
    }

    @Override
    public Identifier getTexture(KoopaShellEntity entity) {
        return Mubble.id("textures/entity/koopa_shell/green.png");
    }
}

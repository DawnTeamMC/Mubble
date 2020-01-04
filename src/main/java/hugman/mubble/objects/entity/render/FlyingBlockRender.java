package hugman.mubble.objects.entity.render;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.objects.entity.FlyingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class FlyingBlockRender extends EntityRenderer<FlyingBlockEntity>
{
   public FlyingBlockRender(EntityRenderDispatcher dispatcher)
   {
      super(dispatcher);
      this.shadowSize = 0.5F;
   }

   @Override
   public void render(FlyingBlockEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light)
   {
      BlockState blockState = entity.getBlockState();
      if (blockState.getRenderType() == BlockRenderType.MODEL)
      {
         World world = entity.getEntityWorld();
         if (blockState != world.getBlockState(new BlockPos(entity)) && blockState.getRenderType() != BlockRenderType.INVISIBLE)
         {
            this.getRenderManager().textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            if (this.getRenderManager().shouldRenderHitboxes())
            {
               GlStateManager.enableColorMaterial();
               GlStateManager.setupOverlayColor(entity.getTeamColorValue(), 1);
            }
            bufferbuilder.begin(7, VertexFormats.POSITION);
            BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().getMax(Axis.Z), entity.getZ());
            GlStateManager.translatef((float)(entity.getX() - (double)blockpos.getX() - 0.5D), (float)(entity.getY() - (double)blockpos.getY()), (float)(entity.getZ() - (double)blockpos.getZ() - 0.5D));
            BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
            blockrendererdispatcher.getModelRenderer().render(world, blockrendererdispatcher.getModel(blockState), blockState, blockpos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped()), false, new Random(), world.random.nextLong(), light);
            tessellator.draw();
            if (this.getRenderManager().shouldRenderHitboxes())
            {
               GlStateManager.teardownOverlayColor();
               GlStateManager.disableColorMaterial();
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            super.render(entity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
         }
      }
   }
   
   @Override
   public Identifier getTexture(FlyingBlockEntity entity)
   {
      return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
   }
}
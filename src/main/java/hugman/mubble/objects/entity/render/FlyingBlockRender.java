package hugman.mubble.objects.entity.render;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.objects.entity.FlyingBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class FlyingBlockRender extends EntityRenderer<FlyingBlockEntity>
{
   public FlyingBlockRender(EntityRendererManager renderManagerIn)
   {
      super(renderManagerIn);
      this.shadowSize = 0.5F;
   }

   @Override
   public void doRender(FlyingBlockEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
   {
      BlockState blockState = entity.getBlockState();
      if (blockState.getRenderType() == BlockRenderType.MODEL)
      {
         World world = entity.getWorldObj();
         if (blockState != world.getBlockState(new BlockPos(entity)) && blockState.getRenderType() != BlockRenderType.INVISIBLE)
         {
            this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            if (this.renderOutlines)
            {
               GlStateManager.enableColorMaterial();
               GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
            }
            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            BlockPos blockpos = new BlockPos(entity.posX, entity.getBoundingBox().maxY, entity.posZ);
            GlStateManager.translatef((float)(x - (double)blockpos.getX() - 0.5D), (float)(y - (double)blockpos.getY()), (float)(z - (double)blockpos.getZ() - 0.5D));
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockState), blockState, blockpos, bufferbuilder, false, new Random(), blockState.getPositionRandom(entity.getOrigin()), EmptyModelData.INSTANCE);
            tessellator.draw();
            if (this.renderOutlines)
            {
               GlStateManager.tearDownSolidRenderingTextureCombine();
               GlStateManager.disableColorMaterial();
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
         }
      }
   }
   
   @Override
   protected ResourceLocation getEntityTexture(FlyingBlockEntity entity)
   {
      return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
   }
}
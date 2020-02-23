package hugman.mubble.objects.entity.render;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import hugman.mubble.objects.entity.FlyingBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class FlyingBlockRenderer extends EntityRenderer<FlyingBlockEntity>
{
	public FlyingBlockRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}
	
	@Override
	public void render(FlyingBlockEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack matrix, IRenderTypeBuffer buffer, int p_225623_6_)
	{
		BlockState blockstate = entity.getBlockState();
		if (blockstate.getRenderType() == BlockRenderType.MODEL)
		{
			World world = entity.getWorldObj();
			if (blockstate != world.getBlockState(new BlockPos(entity)) && blockstate.getRenderType() != BlockRenderType.INVISIBLE)
			{
				matrix.push();
				BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				matrix.translate(-0.5D, 0.0D, -0.5D);
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
				for(RenderType type : RenderType.getBlockLayers())
				{
					if (RenderTypeLookup.canRenderInLayer(blockstate, type))
					{
						ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, matrix, buffer.getBuffer(type), false, new Random(), blockstate.getPositionRandom(entity.getOrigin()), OverlayTexture.DEFAULT_UV, EmptyModelData.INSTANCE);
					}
				}
				ForgeHooksClient.setRenderLayer(null);
				matrix.pop();
				super.render(entity, p_225623_2_, p_225623_3_, matrix, buffer, p_225623_6_);
			}
		}
	}
	
	@Override
	public ResourceLocation getEntityTexture(FlyingBlockEntity entity)
	{
		return PlayerContainer.BLOCK_ATLAS_TEXTURE;
	}
}
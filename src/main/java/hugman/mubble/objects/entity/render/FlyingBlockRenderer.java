package hugman.mubble.objects.entity.render;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import hugman.mubble.objects.entity.FlyingBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
	public void render(FlyingBlockEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_)
	{
		BlockState blockstate = p_225623_1_.getBlockState();
		if (blockstate.getRenderType() == BlockRenderType.MODEL)
		{
			World world = p_225623_1_.getWorldObj();
			if (blockstate != world.getBlockState(new BlockPos(p_225623_1_)) && blockstate.getRenderType() != BlockRenderType.INVISIBLE)
			{
				p_225623_4_.push();
				BlockPos blockpos = new BlockPos(p_225623_1_.getX(), p_225623_1_.getBoundingBox().maxY, p_225623_1_.getZ());
				p_225623_4_.translate(-0.5D, 0.0D, -0.5D);
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
				for(net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.getBlockLayers())
				{
					if (RenderTypeLookup.canRenderInLayer(blockstate, type))
					{
						net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, p_225623_4_, p_225623_5_.getBuffer(type), false, new Random(), blockstate.getPositionRandom(p_225623_1_.getOrigin()), OverlayTexture.DEFAULT_UV, EmptyModelData.INSTANCE);
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
				p_225623_4_.pop();
				super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
			}
		}
	}
	
	@Override
	public ResourceLocation getEntityTexture(FlyingBlockEntity entity)
	{
		return PlayerContainer.BLOCK_ATLAS_TEXTURE;
	}
}
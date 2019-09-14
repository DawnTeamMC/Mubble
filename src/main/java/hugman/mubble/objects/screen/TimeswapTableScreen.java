package hugman.mubble.objects.screen;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.Mubble;
import hugman.mubble.objects.container.TimeswapTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TimeswapTableScreen extends ContainerScreen<TimeswapTableContainer>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Mubble.MOD_ID, "textures/gui/container/timeswap_table.png");
	private float sliderProgress;
	private boolean clickedOnSroll;
	private int recipeIndexOffset;
	private boolean hasItemsInInputSlot;

	public TimeswapTableScreen(TimeswapTableContainer containerIn, PlayerInventory playerInv, ITextComponent titleIn)
	{
		super(containerIn, playerInv, titleIn);
		containerIn.setInventoryUpdateListener(this::onInventoryUpdate);
	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 4.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 94), 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		int k = (int) (41.0F * this.sliderProgress);
		this.blit(i + 119, j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
		int l = this.guiLeft + 52;
		int i1 = this.guiTop + 14;
		int j1 = this.recipeIndexOffset + 12;
		this.drawRecipesBackground(mouseX, mouseY, l, i1, j1);
		this.drawRecipesItems(l, i1, j1);
	}

	private void drawRecipesBackground(int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
		for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getOutputItemsListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = left + j % 4 * 16;
			int l = j / 4;
			int i1 = top + l * 18 + 2;
			int j1 = this.ySize;
			if (i == this.container.getSelectedOutputItem()) {
				j1 += 18;
			} else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
				j1 += 36;
			}

			this.blit(k, i1 - 1, 0, j1, 16, 18);
		}

	}

	private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
		RenderHelper.enableGUIStandardItemLighting();
		List<Item> list = this.container.getOutputItemsList();

		for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getOutputItemsListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = left + j % 4 * 16;
			int l = j / 4;
			int i1 = top + l * 18 + 2;
			this.minecraft.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(list.get(i)), k, i1);
		}

		RenderHelper.disableStandardItemLighting();
	}

	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		this.clickedOnSroll = false;
		if (this.hasItemsInInputSlot) {
			int i = this.guiLeft + 52;
			int j = this.guiTop + 14;
			int k = this.recipeIndexOffset + 12;

			for (int l = this.recipeIndexOffset; l < k; ++l) {
				int i1 = l - this.recipeIndexOffset;
				double d0 = p_mouseClicked_1_ - (double) (i + i1 % 4 * 16);
				double d1 = p_mouseClicked_3_ - (double) (j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D
						&& this.container.enchantItem(this.minecraft.player, l)) {
					Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.playerController.sendEnchantPacket((this.container).windowId, l);
					return true;
				}
			}

			i = this.guiLeft + 119;
			j = this.guiTop + 9;
			if (p_mouseClicked_1_ >= (double) i && p_mouseClicked_1_ < (double) (i + 12)
					&& p_mouseClicked_3_ >= (double) j && p_mouseClicked_3_ < (double) (j + 54)) {
				this.clickedOnSroll = true;
			}
		}

		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (this.clickedOnSroll && this.canScroll()) {
			int i = this.guiTop + 14;
			int j = i + 54;
			this.sliderProgress = ((float) p_mouseDragged_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
			return true;
		} else {
			return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
	}

	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
		if (this.canScroll()) {
			int i = this.getHiddenRows();
			this.sliderProgress = (float) ((double) this.sliderProgress - p_mouseScrolled_5_ / (double) i);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
		}

		return true;
	}

	private boolean canScroll() {
		return this.hasItemsInInputSlot && this.container.getOutputItemsListSize() > 12;
	}

	protected int getHiddenRows() {
		return (this.container.getOutputItemsListSize() + 4 - 1) / 4 - 3;
	}

	/**
	 * Called every time this screen's container is changed (is marked as dirty).
	 */
	private void onInventoryUpdate() {
		this.hasItemsInInputSlot = this.container.hasItemsinInputSlot();
		if (!this.hasItemsInInputSlot) {
			this.sliderProgress = 0.0F;
			this.recipeIndexOffset = 0;
		}

	}
}
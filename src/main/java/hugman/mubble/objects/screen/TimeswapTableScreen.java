package hugman.mubble.objects.screen;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import hugman.mubble.Mubble;
import hugman.mubble.objects.container.TimeswapTableContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class TimeswapTableScreen extends AbstractContainerScreen<TimeswapTableContainer>
{
	private static final TranslatableText CONTAINER_NAME = new TranslatableText("container." + Mubble.MOD_ID + ".timeswap_table");
	private static final Identifier BACKGROUND_TEXTURE = new Identifier(Mubble.MOD_ID, "textures/gui/container/timeswap_table.png");
	private float sliderProgress;
	private boolean clickedOnSroll;
	private int recipeIndexOffset;
	private boolean hasItemsInInputSlot;

	public TimeswapTableScreen(TimeswapTableContainer containerIn, PlayerInventory playerInv, Text titleIn)
	{
		super(containerIn, playerInv, titleIn);
		containerIn.setInventoryUpdateListener(this::onInventoryUpdate);
	}

	public void render(int mouseX, int mouseY, float delta) {
		super.render(mouseX, mouseY, delta);
		this.drawMouseoverTooltip(mouseX, mouseY);
	}
	
	protected void drawForeground(int mouseX, int mouseY) {
		this.font.draw(CONTAINER_NAME.asFormattedString(), 8.0F, 4.0F, 4210752);
		this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float) (this.containerHeight - 94), 4210752);
	}

	protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
		this.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int i = this.x;
		int j = this.y;
		this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);
		int k = (int) (41.0F * this.sliderProgress);
		this.blit(i + 119, j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
		int l = this.x + 52;
		int i1 = this.y + 14;
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
			int j1 = this.containerHeight;
			if (i == this.container.getSelectedOutputItem()) {
				j1 += 18;
			} else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
				j1 += 36;
			}

			this.blit(k, i1 - 1, 0, j1, 16, 18);
		}

	}

	private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
		List<Item> list = this.container.getOutputItemsList();

		for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getOutputItemsListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = left + j % 4 * 16;
			int l = j / 4;
			int i1 = top + l * 18 + 2;
			this.minecraft.getItemRenderer().renderGuiItem(new ItemStack(list.get(i)), k, i1);
		}
	}

	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		this.clickedOnSroll = false;
		if (this.hasItemsInInputSlot) {
			int i = this.x + 52;
			int j = this.y + 14;
			int k = this.recipeIndexOffset + 12;

			for (int l = this.recipeIndexOffset; l < k; ++l) {
				int i1 = l - this.recipeIndexOffset;
				double d0 = p_mouseClicked_1_ - (double) (i + i1 % 4 * 16);
				double d1 = p_mouseClicked_3_ - (double) (j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D
						&& this.container.onButtonClick(this.minecraft.player, l)) {
					MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.interactionManager.clickButton((this.container).syncId, l);
					return true;
				}
			}

			i = this.x + 119;
			j = this.y + 9;
			if (p_mouseClicked_1_ >= (double) i && p_mouseClicked_1_ < (double) (i + 12)
					&& p_mouseClicked_3_ >= (double) j && p_mouseClicked_3_ < (double) (j + 54)) {
				this.clickedOnSroll = true;
			}
		}

		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (this.clickedOnSroll && this.canScroll()) {
			int i = this.y + 14;
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
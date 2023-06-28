package fr.hugman.mubble.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.block.BumpableDropMode;
import fr.hugman.mubble.screen.BumpableScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BumpableScreen extends HandledScreen<BumpableScreenHandler> {
	private static final Identifier TEXTURE = Mubble.id("textures/gui/container/bumpable.png");

	private ButtonWidget button;

	public BumpableScreen(BumpableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);

		this.passEvents = false;
		this.backgroundHeight = 134;
		this.playerInventoryTitleY = this.backgroundHeight - 94;

		handler.addListener(new ScreenHandlerListener(){
			@Override
			public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {}

			@Override
			public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
				if (property == 0) {
					BumpableScreen.this.updateMode();
				}
			}
		});
	}

	@Override
	protected void init() {
		super.init();
		int backgroundX = (this.width - this.backgroundWidth) / 2;
		int backgroundY = (this.height - this.backgroundHeight) / 2;
		BumpableDropMode mode = this.handler.getDropMode();
		this.button = ButtonWidget.builder(mode.getName(), btn -> this.sendButtonPressPacket(0))
				.dimensions(backgroundX + 51, backgroundY + 16, 100, 20)
				.tooltip(Tooltip.of(mode.getDescription()))
				.build();
		this.addDrawableChild(this.button);
	}

	private void sendButtonPressPacket(int id) {
		this.client.interactionManager.clickButton(this.handler.syncId, id);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}

	@Override
	public void close() {
		this.finishEditing();
	}

	void updateMode() {
		BumpableDropMode mode = this.handler.getDropMode();
		this.button.setMessage(mode.getName());
		this.button.setTooltip(Tooltip.of(mode.getDescription()));
	}

	private void finishEditing() {
		this.handler.sendContentUpdates();
		if(this.client != null) {
			this.client.setScreen(null);
		}
	}
}
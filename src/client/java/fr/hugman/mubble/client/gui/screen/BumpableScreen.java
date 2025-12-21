package fr.hugman.mubble.client.gui.screen;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.inventory.BumpableScreenHandler;
import fr.hugman.mubble.world.level.block.BumpableDropMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class BumpableScreen extends AbstractContainerScreen<BumpableScreenHandler> {
    private static final Identifier TEXTURE = Mubble.id("textures/gui/container/bumpable.png");

    private Button button;

    public BumpableScreen(BumpableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 176, 134);

        this.inventoryLabelY = this.imageHeight - 94;

        handler.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu handler, int slotId, ItemStack stack) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu handler, int property, int value) {
                switch (property) {
                    case 0 -> BumpableScreen.this.updateMode();
                    case 1 -> BumpableScreen.this.updateModeLock();
                }
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        int backgroundX = (this.width - this.imageWidth) / 2;
        int backgroundY = (this.height - this.imageHeight) / 2;
        BumpableDropMode mode = this.menu.getDropMode();
        this.button = Button.builder(mode.getName(), btn -> this.sendButtonPressPacket(0))
                .bounds(backgroundX + 51, backgroundY + 16, 100, 20)
                .tooltip(Tooltip.create(mode.getDescription()))
                .build();
        this.button.active = !this.menu.isDropModeLocked();
        this.addRenderableWidget(this.button);
    }

    private void sendButtonPressPacket(int id) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void onClose() {
        this.finishEditing();
    }

    void updateMode() {
        BumpableDropMode mode = this.menu.getDropMode();
        this.button.setMessage(mode.getName());
        this.button.setTooltip(Tooltip.create(mode.getDescription()));
    }

    void updateModeLock() {
        this.button.active = !this.menu.isDropModeLocked();
    }

    private void finishEditing() {
        this.menu.broadcastChanges();
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }
}

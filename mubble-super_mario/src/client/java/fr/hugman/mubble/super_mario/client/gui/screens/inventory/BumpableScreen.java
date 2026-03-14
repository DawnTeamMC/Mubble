package fr.hugman.mubble.super_mario.client.gui.screens.inventory;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.inventory.BumpableScreenHandler;
import fr.hugman.mubble.super_mario.world.level.block.BumpableDropMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    private static final Identifier TEXTURE = SuperMario.id("textures/gui/container/bumpable.png");

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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
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

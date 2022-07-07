package io.github.mortuusars.wares.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.util.Cursor;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.GuiUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final List<ScreenElement<?>> elements = new ArrayList<>();
    private final List<ScreenElement<?>> elementsReversed = new ArrayList<>();
    protected boolean drawDebugInfo = true;

    public BaseContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void addElement(ScreenElement<?> element){
        elements.add(element);
        elementsReversed.add(0, element);
    }

    public ScreenElement<?> getElement(int index){
        return elements.get(index);
    }

    public void removeElement(ScreenElement<?> element){
        elements.remove(element);
        elementsReversed.remove(element);
    }

    public NonNullList<ScreenElement<?>> getElements(){
        NonNullList<ScreenElement<?>> screenElements = NonNullList.create();
        screenElements.addAll(elements);
        return screenElements;
    }

    public ItemRenderer getItemRenderer(){
        return this.itemRenderer;
    }

    public Font getFontRenderer(){
        return this.font;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);

        boolean isOverAnyElement = false;
        for (ScreenElement<?> element : elements){
            element.render(poseStack, mouseX, mouseY, partialTick);
            if (element.isMouseOver(mouseX, mouseY)){
                Cursor.set(element.getCursor());
                element.onMouseOver(mouseX, mouseY);
                isOverAnyElement = true;
            }
        }

        if (!isOverAnyElement)
            Cursor.set(Cursor.ARROW);

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@NotNull PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);
        for (ScreenElement<?> element : elementsReversed){
            if (element.isMouseOver(x, y)){
                element.renderTooltip(poseStack, x ,y);
                break;
            }
        }

        if (this.drawDebugInfo && minecraft.options.renderDebug)
            drawDebugInfo(poseStack, x, y, 0);
    }

    public void renderItemStackTooltip(PoseStack poseStack, ItemStack stack, int mouseX, int mouseY){
        this.renderTooltip(poseStack, stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY){
        for (ScreenElement<?> element : elements)
            element.renderBg(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        for (ScreenElement<?> element : elementsReversed){ // catch mouse click on last added elements first.
            if (element.isMouseOver(mouseX, mouseY)){
                element.onClick(mouseX, mouseY);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    public void drawDebugInfo(PoseStack poseStack, int mouseX, int mouseY, float partialTick){
        int xOffset = 5;
        int yOffset = 5;

        this.font.draw(poseStack, "Width: " + this.width, xOffset, yOffset += 10, 0x50eeee);
        this.font.draw(poseStack, "Height: " + this.height, xOffset, yOffset += 10, 0x50eeee);

        this.font.draw(poseStack, "Image Width: " + this.imageWidth, xOffset, yOffset += 10, 0x50eeee);
        this.font.draw(poseStack, "Image Height: " + this.imageHeight, xOffset, yOffset += 10, 0x50eeee);

        this.font.draw(poseStack, "Mouse X: " + mouseX, xOffset, yOffset += 10, 0x50eeee);
        this.font.draw(poseStack, "Mouse Y: " + mouseY, xOffset, yOffset += 10, 0x50eeee);

        yOffset += 10;

        for (ScreenElement<?> element : elementsReversed){
            if (element.isMouseOver(mouseX, mouseY)){
                this.font.draw(poseStack, element.getClass().getSimpleName() + ", ID: " + element.id, xOffset, yOffset += 10, 0x50eeee);

                int borderColor = 0xFFAA2244;

                int x = element.posX -1;
                int y = element.posY -1;
                int width = element.width;
                int height = element.height;

                // Mouseover element border
                hLine(poseStack, x, x + width, y, borderColor);
                hLine(poseStack, x, x + width, y + height, borderColor);
                vLine(poseStack, x, y, y + height, borderColor);
                vLine(poseStack, x + width, y, y + height, borderColor);
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        Cursor.set(Cursor.ARROW);
    }
}

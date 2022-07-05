package io.github.mortuusars.wares.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final List<ScreenElement<?>> elements = new ArrayList<>();
    private final List<ScreenElement<?>> elementsReversed = new ArrayList<>();

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
        for (ScreenElement<?> element : elements){
            element.render(poseStack, mouseX, mouseY, partialTick);
            if (element.isMouseOver(mouseX, mouseY))
                element.onMouseOver(mouseX, mouseY);
        }
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@NotNull PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);
        for (ScreenElement<?> element : elements){
            if (element.isMouseOver(x, y))
                element.renderTooltip(poseStack, x ,y);
        }
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

        if (super.mouseClicked(mouseX, mouseY, pButton))
            return true;

        return false;
    }

    public void drawDebugInfo(PoseStack poseStack, int mouseX, int mouseY, float partialTick){
        int xOffset = 5;

        this.font.draw(poseStack, "Mouse X: " + mouseX, xOffset, 5, 0x50eeee);
        this.font.draw(poseStack, "Mouse Y: " + mouseY, xOffset, 15, 0x50eeee);

        this.font.draw(poseStack, "Width: " + this.width, xOffset, 25, 0x50eeee);
        this.font.draw(poseStack, "Height: " + this.height, xOffset, 35, 0x50eeee);

        this.font.draw(poseStack, "Image Width: " + this.imageWidth, xOffset, 45, 0x50eeee);
        this.font.draw(poseStack, "Image Height: " + this.imageHeight, xOffset, 55, 0x50eeee);

        for (ScreenElement<?> element : elements){
            if (element.isMouseOver(mouseX, mouseY)){
                this.font.draw(poseStack, "MouseOver ID: " + element.id, xOffset, 70, 0x50eeee);
                //TODO: DEBUL LINES
                fill(poseStack, element.posX, element.posY, element.posX + element.width, element.posY + element.height, 0xff3333);
            }
        }
    }
}

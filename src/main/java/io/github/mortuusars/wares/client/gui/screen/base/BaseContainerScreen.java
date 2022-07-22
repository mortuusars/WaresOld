package io.github.mortuusars.wares.client.gui.screen.base;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.mortuusars.wares.client.gui.screen.util.Cursor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final List<ScreenElement<?>> elements = new ArrayList<>();
    private final List<ScreenElement<?>> elementsReversed = new ArrayList<>();
    protected boolean drawDebugInfo = true;

    public long gameTick;

    public BaseContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }


    // <Elements>

    public Point addElement(ScreenElement<?> element){
        elements.add(element);
        elementsReversed.add(0, element);
        return new Point(element.posX + element.width, element.posY + element.height);
    }

    public ScreenElement<?> getElement(int index){
        return elements.get(index);
    }

    @Nullable
    public ScreenElement<?> getElementByID(String id){
        for (var element : elements){
            if (element.id.equals(id))
                return element;
        }
        return null;
    }

    public NonNullList<ScreenElement<?>> getElements(){
        NonNullList<ScreenElement<?>> screenElements = NonNullList.create();
        screenElements.addAll(elements);
        return screenElements;
    }

    public void removeElement(ScreenElement<?> element){
        elements.remove(element);
        elementsReversed.remove(element);
    }

    public void clearElements(){
        elements.clear();
        elementsReversed.clear();
    }


    // <Utils>

    public int getGuiCenterX(){
        return getGuiLeft() + imageWidth / 2;
    }

    public int getGuiCenterY(){
        return getGuiTop() + imageHeight / 2;
    }

    public int fromLeft(int x){
        return getGuiLeft() + x;
    }

    public int fromTop(int y){
        return getGuiTop() + y;
    }

    public int fromRight(int x, int width){
        return getGuiLeft() + imageWidth - (x + width);
    }

    public int fromBottom(int y, int height){
        return getGuiTop() + imageHeight - (y + height);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public int drawCenteredTextBlock(PoseStack poseStack, Component text, int posX, int posY, int maxWidth, int verticalLineSpacing, int maxLines, int color){
        Preconditions.checkArgument(maxLines > 0, "Text cannot have 0 lines.");
        List<FormattedCharSequence> sequenceList = font.split(text, maxWidth);

        for (int i = 0; i < Math.min(sequenceList.size(), maxLines); i++) {
            FormattedCharSequence textPart = sequenceList.get(i);
            font.draw(poseStack, textPart, posX - font.width(textPart) / 2, posY, color);
            posY += font.lineHeight + verticalLineSpacing;
        }

        return posY - verticalLineSpacing;
    }

    private int gridLayoutIndex = 0;
    protected void gridLayout(List<ScreenElement<?>> elements, int rows, int columns, Rectangle bounds, HorizontalAlignment rowAlignment){
        Preconditions.checkArgument(rows > 0, "Cannot have 0 (or negative) rows. Value: " + rows);
        Preconditions.checkArgument(columns > 0, "Cannot have 0 (or negative) columns. Value: " + columns);
        Preconditions.checkArgument(bounds.width > 0 && bounds.height > 0,
                "Size of the area cannot be 0 or negative. Values: width - " + bounds.width + ", height - " + bounds.height);

        gridLayoutIndex++;
        addElement(new ScreenElement<>(this, "grid_layout_" + gridLayoutIndex, bounds.x, bounds.y, bounds.width, bounds.height));

        List<List<ScreenElement<?>>> rowChunks = Lists.partition(elements, columns);

        int y = bounds.y;

        for (int row = 0; row < Math.min(rowChunks.size(), rows); row++) {
            List<ScreenElement<?>> rowElements = rowChunks.get(row);

            int rowWidth = rowElements.stream().mapToInt(e -> e.width).sum();
            int x = switch (rowAlignment){
                case LEFT -> bounds.x;
                case CENTER -> bounds.x + (bounds.width / 2) - rowWidth / 2;
                case RIGHT -> bounds.x + bounds.width - rowWidth;
            };

            int largestHeight = 0;

            for (var element : rowElements){
                element.posX = x;
                element.posY = y;
                addElement(element);
                x += element.width;
                largestHeight = Math.max(largestHeight, element.height);

            }

            y += largestHeight;
        }
    }

    // <Base>

    public abstract ResourceLocation getBackgroundTexture();

    @Override
    protected void init() {
        this.inventoryLabelY = this.imageHeight - 94;
        clearElements(); // Fix doubles when resizing window;
        super.init();
    }

    @Override
    public void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1,1, 1);
        RenderSystem.setShaderTexture(0, getBackgroundTexture());
        blit(poseStack, getGuiLeft(), getGuiTop(), 0,0, imageWidth, imageHeight); // Gui Texture

        ClientLevel level = Minecraft.getInstance().level;
        if (level != null)
            gameTick = level.getGameTime();

        renderBgForElements(poseStack, partialTick, mouseX, mouseY);
    }

    public void renderBgForElements(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY){
        for (ScreenElement<?> element : elements)
            element.renderBg(poseStack, mouseX, mouseY, partialTick);
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
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
        for (ScreenElement<?> element : elements)
            element.renderLabels(poseStack, mouseX, mouseY);
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

        if (this.drawDebugInfo && minecraft != null && minecraft.options.renderDebug)
            drawDebugInfo(poseStack, x, y, 0);
    }

    public void renderItemStackTooltip(PoseStack poseStack, ItemStack stack, int mouseX, int mouseY){
        this.renderTooltip(poseStack, stack, mouseX, mouseY);
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
                int width = element.width + 1;
                int height = element.height + 1;

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


    // <Getters>

    public ItemRenderer getItemRenderer(){
        return this.itemRenderer;
    }

    public Font getFontRenderer(){
        return this.font;
    }
}

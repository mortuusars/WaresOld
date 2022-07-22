package io.github.mortuusars.wares.client.gui.screen.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class UIElement<T extends BaseContainerScreen<?>> {
    T screen;
    String id;
    Rectangle maxBounds;
    int x;
    int y;
    public int desiredWidth;
    public int desiredHeight;
    int actualWidth;
    int actualHeight;
    int minWidth = 5;
    int minHeight = 5;
    VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;

    //TODO: finish
    public UIElement(T screen, String id, Rectangle maxBounds) {
        this.screen = screen;
        this.id = id;
        this.maxBounds = maxBounds;
    }

    public boolean isMouseOver(int mouseX, int mouseY){
        int mX = mouseX - screen.getGuiLeft();
        int mY = mouseY - screen.getGuiTop();
        return (mX >= x && mX <= x + actualWidth) && (mY >= y && mY <= y + actualHeight);
    }

    public void layout(){
        actualHeight = Math.max(minHeight, Math.min(desiredHeight, maxBounds.height));
        switch (verticalAlignment){
            case TOP -> y = maxBounds.y;
            case CENTER -> y = (maxBounds.height - actualHeight) / 2;
            case BOTTOM -> y = maxBounds.height - actualHeight;
        }

        actualWidth = Math.max(minWidth, Math.min(desiredWidth, maxBounds.width));
        switch (horizontalAlignment) {
            case LEFT -> x = maxBounds.x;
            case CENTER -> x = (maxBounds.width - actualWidth) / 2;
            case RIGHT -> x = maxBounds.width - actualWidth;
        }
    }
}

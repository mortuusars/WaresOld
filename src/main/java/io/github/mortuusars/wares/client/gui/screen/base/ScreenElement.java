package io.github.mortuusars.wares.client.gui.screen.base;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.util.Cursor;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class ScreenElement<T extends Screen> {
    protected final T screen;
    public String id;
    public int posX;
    public int posY;
    public int width;
    public int height;

    public ScreenElement(T parentScreen, String id, int posX, int posY, int width, int height) {
        this.screen = parentScreen;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public boolean isMouseOver(double mouseX, double mouseY){
        return (mouseX > posX && mouseX <= posX + width) && (mouseY > posY && mouseY <= posY + height);
    }

    public Cursor getCursor(){
        return Cursor.ARROW;
    }

    public void renderBg(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {}
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {}
    public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) { }
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {}
    public void onMouseOver(int mouseX, int mouseY){}
    public void onClick(double mouseX, double mouseY){}
}

package io.github.mortuusars.wares.client.gui.screen.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public enum Cursor {
    ARROW(GLFW.GLFW_ARROW_CURSOR),
    HAND(GLFW.GLFW_HAND_CURSOR),
    I_BEAM(GLFW.GLFW_IBEAM_CURSOR),
    CROSSHAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
    DISABLED(GLFW.GLFW_CURSOR_DISABLED),
    HIDDEN(GLFW.GLFW_CURSOR_HIDDEN);

    private static Cursor current = ARROW;

    private final long cursor;

    Cursor(long cursor) {
        this.cursor = cursor;
    }

    public void set(){
        Cursor.set(this);
    }

    public static void set(Cursor cursor){
        if (cursor != current){
            long cursorID = GLFW.glfwCreateStandardCursor((int) cursor.cursor);
            GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursorID);
            current = cursor;
        }
    }
}
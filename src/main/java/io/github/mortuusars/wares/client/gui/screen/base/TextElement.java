package io.github.mortuusars.wares.client.gui.screen.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TextElement<T extends BaseContainerScreen<?>> extends ScreenElement<T>{

    public static final int DEFAULT_LINE_SPACING = 1;
    public static final int DEFAULT_FONT_COLOR = 0xFF000000;

    protected Supplier<Component> textSupplier;

    public HorizontalAlignment horizontalAlignment;
    public int fontColor = DEFAULT_FONT_COLOR;
    public int lineSpacing = DEFAULT_LINE_SPACING;
    public boolean tooltipLeftoverOnly = true;
    public int tooltipWidth = 240;

    public List<FormattedCharSequence> leftoverLines;

    private boolean hasLeftoverText = false;

    public TextElement(T parentScreen, String id, int posX, int posY, int width, int height, Supplier<Component> textComponentSupplier) {
        super(parentScreen, id, posX, posY, width, height);
        this.textSupplier = textComponentSupplier;
    }

    public TextElement(T parentScreen, String id, int posX, int posY, int width, int height, Component textComponent) {
        super(parentScreen, id, posX, posY, width, height);
        this.textSupplier = () -> textComponent;
    }

    public TextElement(T parentScreen, String id, int posX, int posY, int width, int height, String text) {
        super(parentScreen, id, posX, posY, width, height);
        this.textSupplier = () -> new TextComponent(text);
    }

    public TextElement<T> horizontalAlignment(HorizontalAlignment alignment){
        horizontalAlignment = alignment;
        return this;
    }

    public TextElement<T> tooltipLeftoverOnly(boolean value){
        tooltipLeftoverOnly = value;
        return this;
    }

    public TextElement<T> lineSpacing(int spacing){
        lineSpacing = spacing;
        return this;
    }

    public TextElement<T> color(int color){
        fontColor = color;
        return this;
    }


    @Override
    public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        Component component = textSupplier.get();
        List<FormattedCharSequence> lines = screen.getFontRenderer().split(component, width);

        int maxLines = height / (screen.getFontRenderer().lineHeight + lineSpacing);

        hasLeftoverText = maxLines < lines.size();
        leftoverLines = new ArrayList<>(lines.stream().skip(maxLines).toList());

        int y = posY - screen.getGuiTop();

        for (int index = 0; index < maxLines; index++) {
            FormattedCharSequence line = lines.get(index);
            int x = posX - screen.getGuiLeft();
            switch (horizontalAlignment) {
                case CENTER -> x += width / 2 - screen.getFontRenderer().width(line) / 2;
                case RIGHT -> x += width - screen.getFontRenderer().width(line);
            }

            if (hasLeftoverText && index == maxLines -1){ // draw 3 dots at the end.
                FormattedCharSequence lastLine = FormattedCharSequence.composite(line, FormattedCharSequence.forward("...", Style.EMPTY));
                screen.getFontRenderer().draw(poseStack, lastLine, x, y, fontColor);
            }
            else
                screen.getFontRenderer().draw(poseStack, line, x, y, fontColor);

            y += screen.getFontRenderer().lineHeight + lineSpacing;
        }
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        if (hasLeftoverText){
            if (tooltipLeftoverOnly && leftoverLines.size() > 0){
                leftoverLines.set(0, FormattedCharSequence.composite(FormattedCharSequence.forward("...", Style.EMPTY), leftoverLines.get(0)));
                screen.renderTooltip(poseStack, leftoverLines, mouseX, mouseY);
            }
            else{
                List<FormattedCharSequence> lines = screen.getFontRenderer().split(textSupplier.get(), tooltipWidth);
                screen.renderTooltip(poseStack, lines, mouseX, mouseY);
            }
        }
    }

    /**
     * Gets the desired height of the text.
     * @return Height in pixels - how many pixels is needed to draw text fully.
     */
    public static int measure(Font font, FormattedText text, int width, int lineSpacing){
        List<FormattedCharSequence> sequences = font.split(text, width);
        return sequences.size() * (font.lineHeight + lineSpacing);
    }
}

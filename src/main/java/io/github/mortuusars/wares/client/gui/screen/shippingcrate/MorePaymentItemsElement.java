package io.github.mortuusars.wares.client.gui.screen.shippingcrate;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.ScreenElement;
import io.github.mortuusars.wares.client.gui.screen.ShippingCrateScreen;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MorePaymentItemsElement extends ScreenElement<ShippingCrateScreen> {
    public final List<ItemStack> excessStacks;

    private static final int TEXTURE_X = 0;
    private static final int TEXTURE_Y = 213;

    private List<Component> tooltipComponents = new ArrayList<>();

    public MorePaymentItemsElement(ShippingCrateScreen parentScreen, int id, int posX, int posY, List<ItemStack> excessStacks) {
        super(parentScreen, id, posX, posY, 18, 18);
        this.excessStacks = excessStacks;

        for (ItemStack stack : excessStacks){
            tooltipComponents.add(((BaseComponent)stack.getHoverName()).append(new TextComponent(" x" + stack.getCount())));
        }
    }

    @Override
    public void renderBg(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (isMouseOver(mouseX, mouseY))
            screen.blit(poseStack, posX, posY,  TEXTURE_X + 18, TEXTURE_Y, width, height);
        else
            screen.blit(poseStack, posX, posY,  TEXTURE_X, TEXTURE_Y, width, height);
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        screen.renderTooltip(poseStack, tooltipComponents, Optional.empty(), mouseX, mouseY, screen.getFontRenderer());
    }
}

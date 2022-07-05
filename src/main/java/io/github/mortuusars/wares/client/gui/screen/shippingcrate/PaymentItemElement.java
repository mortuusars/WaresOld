package io.github.mortuusars.wares.client.gui.screen.shippingcrate;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.ScreenElement;
import io.github.mortuusars.wares.client.gui.screen.ShippingCrateScreen;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaymentItemElement extends ScreenElement<ShippingCrateScreen> {
    public ItemStack paymentItemStack;

    public PaymentItemElement(ShippingCrateScreen parentScreen, int id, int posX, int posY, ItemStack paymentItemStack) {
        super(parentScreen, id, posX, posY, 18, 18);
        this.paymentItemStack = paymentItemStack;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        screen.getItemRenderer().renderGuiItem(paymentItemStack, posX, posY);
        screen.getItemRenderer().renderGuiItemDecorations(screen.getFontRenderer(), paymentItemStack, posX, posY);
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        screen.renderItemStackTooltip(poseStack, paymentItemStack, mouseX, mouseY);
    }
}

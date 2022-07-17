package io.github.mortuusars.wares.client.gui.screen.shippingcrate;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.base.ScreenElement;
import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PaymentRequestElement extends ScreenElement<ShippingCrateScreen> {

    private final ItemStack request;
//    private final NonNullList<Component> requestTooltip;

    public PaymentRequestElement(ShippingCrateScreen parentScreen, int id, int posX, int posY, Ware ware) {
        super(parentScreen, id, posX, posY, 18, 18);

        this.request = new ItemStack(ModItems.PURCHASE_REQUEST.get());
//        this.requestTooltip = WareUtils.getWareTooltipInfo(ware);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        screen.getItemRenderer().renderGuiItem(request, posX, posY);
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
//        screen.renderTooltip(poseStack, requestTooltip, Optional.empty(), mouseX, mouseY, screen.getFontRenderer());
    }
}

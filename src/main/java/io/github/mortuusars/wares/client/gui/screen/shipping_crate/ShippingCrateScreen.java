package io.github.mortuusars.wares.client.gui.screen.shipping_crate;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.client.gui.screen.base.BaseContainerScreen;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShippingCrateScreen extends BaseContainerScreen<ShippingCrateMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Wares.MOD_ID, "textures/gui/shipping_crate.png");

    public static final int SHIP_WARE_BUTTON_ID = 0;

    public static final String PROGRESS_ARROW_ID = "progress_arrow";
    public static final String PURCHASE_REQUEST_ID = "purchase_request";
    public static final String PAYMENT_ITEM_ID = "payment_item";

    private static final int PAYMENT_ITEMS_START_X = 150;
    private static final int PAYMENT_ITEMS_START_Y = 17;
    private static final int PAYMENT_ITEMS_ROWS = 5;
    private static final int PAYMENT_ITEMS_COLUMNS = 3;
    private static final int MAX_PAYMENT_ITEMS = 15;

    private static final int PROGRESS_ARROW_X = 121;
    private static final int PROGRESS_ARROW_Y = 52;

    public ShippingCrateScreen(ShippingCrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        imageWidth = 211;
        imageHeight = 212;
        inventoryLabelX = 25;
        inventoryLabelY = 118;

        super.init();

        // Arrow
        this.addElement(new ShipmentProgressArrowElement(this, PROGRESS_ARROW_ID,
                getGuiLeft() + PROGRESS_ARROW_X, getGuiTop() + PROGRESS_ARROW_Y, menu.ware));

        // Payment Items
        int paymentX = this.getGuiLeft() + PAYMENT_ITEMS_START_X;
        int paymentY = this.getGuiTop() + PAYMENT_ITEMS_START_Y;

        if (menu.paymentItems.size() <= 3)
            paymentY += 36;

        int index = 0;
        for (int row = 0; row < PAYMENT_ITEMS_ROWS; row++) {
            for (int column = 0; column < PAYMENT_ITEMS_COLUMNS; column++) {
                if (index >= menu.paymentItems.size())
                    break;
                else if (index == MAX_PAYMENT_ITEMS -1 && menu.paymentItems.size() > MAX_PAYMENT_ITEMS)
                    addElement(new MorePaymentItemsElement(this, PAYMENT_ITEM_ID + index, paymentX + column * 18, paymentY + row * 18,
                            menu.paymentItems.stream().skip(MAX_PAYMENT_ITEMS).toList()));
                else {
                    ItemStack paymentItem = menu.paymentItems.get(index);
                    addElement(new PaymentItemElement(this, PAYMENT_ITEM_ID + index, paymentX + column * 18, paymentY + row * 18, paymentItem));
                }
                index++;
            }
        }

        this.addElement(new PaymentRequestElement(this, PURCHASE_REQUEST_ID, this.getGuiLeft() + 124, this.getGuiTop() + 36, getMenu().ware));
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float partialTick) {
        super.render(pPoseStack, mouseX, mouseY, partialTick);

    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}

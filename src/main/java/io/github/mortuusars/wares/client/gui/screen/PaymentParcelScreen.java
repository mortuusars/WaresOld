package io.github.mortuusars.wares.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.client.gui.screen.base.BaseContainerScreen;
import io.github.mortuusars.wares.common.parcel.ParcelMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PaymentParcelScreen extends BaseContainerScreen<ParcelMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Wares.MOD_ID, "textures/gui/payment_parcel.png");

    public PaymentParcelScreen(ParcelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 166;
        super.init();
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}

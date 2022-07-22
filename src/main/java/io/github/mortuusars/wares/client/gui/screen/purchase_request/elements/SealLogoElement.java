package io.github.mortuusars.wares.client.gui.screen.purchase_request.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.base.ScreenElement;
import io.github.mortuusars.wares.client.gui.screen.purchase_request.PurchaseRequestScreen;
import io.github.mortuusars.wares.lib.enums.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SealLogoElement extends ScreenElement<PurchaseRequestScreen> {

    private final Component tooltip;
    private final int U_OFFSET = 214;
    private final int vOffset;

    public  SealLogoElement(PurchaseRequestScreen parentScreen, String id, int posX, int posY, Rarity rarity) {
        super(parentScreen, id, posX, posY, 42, 42);
        int rarityIndex = Arrays.stream(Rarity.values()).toList().indexOf(rarity);
        vOffset = 42 * rarityIndex;
        tooltip = new TranslatableComponent(rarity.getKey());
    }

    @Override
    public void renderBg(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        screen.blit(poseStack, posX, posY, U_OFFSET, vOffset, width, height);
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        screen.renderTooltip(poseStack, tooltip, mouseX, mouseY);
    }
}

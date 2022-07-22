package io.github.mortuusars.wares.client.gui.screen.purchase_request.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.client.gui.screen.base.ScreenElement;
import io.github.mortuusars.wares.client.gui.screen.purchase_request.PurchaseRequestScreen;
import io.github.mortuusars.wares.setup.ModLangKeys;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class ExperienceElement extends ScreenElement<PurchaseRequestScreen> {

    private static final int U_OFFSET = 186;
    private static final int V_OFFSET = 11;

    private final TranslatableComponent TOOLTIP = new TranslatableComponent(ModLangKeys.GUI_PURCHASE_REQUEST_EXPERIENCE_TOOLTIP);
    private final String xpString;

    public ExperienceElement(PurchaseRequestScreen parentScreen, String id, int posX, int posY, int width, int height, int experience) {
        super(parentScreen, id, posX, posY, width, height);
        xpString = Integer.toString(experience);
    }

    @Override
    public void renderBg(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        screen.blit(poseStack, posX, posY, U_OFFSET, V_OFFSET, 9, 9);
    }

    @Override
    public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        screen.getFontRenderer().draw(poseStack, xpString, posX + 9 + 2 - screen.getGuiLeft(), posY + 1 - screen.getGuiTop(), 0xFF58B845 /*green*/);
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        screen.renderTooltip(poseStack, TOOLTIP, mouseX, mouseY);
    }
}

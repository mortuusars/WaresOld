package io.github.mortuusars.wares.client.gui.screen.mailbox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.client.gui.screen.base.BaseContainerScreen;
import io.github.mortuusars.wares.common.mailbox.MailboxMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MailboxScreen extends BaseContainerScreen<MailboxMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Wares.MOD_ID, "textures/gui/mailbox.png");

    private static final String TRASH_CAN_ID = "trash_can";

    public MailboxScreen(MailboxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 202;
        inventoryLabelY += 40;
        super.init();

        addElement(new TrashCanElement(this, TRASH_CAN_ID, getGuiLeft() + 140, getGuiTop() + 90));
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}

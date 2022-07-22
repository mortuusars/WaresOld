package io.github.mortuusars.wares.client.gui.screen.purchase_request;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.client.gui.screen.base.*;
import io.github.mortuusars.wares.client.gui.screen.purchase_request.elements.ExperienceElement;
import io.github.mortuusars.wares.client.gui.screen.purchase_request.elements.RequestedItemElement;
import io.github.mortuusars.wares.client.gui.screen.purchase_request.elements.SealLogoElement;
import io.github.mortuusars.wares.common.PurchaseRequestMenu;
import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.core.ware.data.WareItem;
import io.github.mortuusars.wares.setup.ModLangKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.GuiUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PurchaseRequestScreen extends BaseContainerScreen<PurchaseRequestMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Wares.MOD_ID, "textures/gui/purchase_request.png");

    private static final int FONT_COLOR = 0xff854920;

    private static final String TITLE_ID = "title";
    private static final String BUYER_ID = "buyer";
    private static final String MESSAGE_ID = "message";
    private static final String SEAL_LOGO_ID = "seal_logo";

    private static final int TITLE_SIDE_MARGIN = 22;
    private static final int MESSAGE_SIDE_MARGIN = 12;

    private static final int TITLE_Y = 14;
    private static final int MESSAGE_Y_MARGIN = 10;

    private static final int ITEM_COLUMNS = 3;

    private final Ware ware;

    private boolean debugMode = false;

    public PurchaseRequestScreen(PurchaseRequestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.ware = menu.ware;
    }

    @Override
    protected void init() {
        this.imageWidth = 186;
        this.imageHeight = 248;
        super.init();
        inventoryLabelY = -1000;
        titleLabelY = -1000;

        int maxItemRows = (int) Math.ceil(Math.max(ware.requestedItems().size(), ware.paymentItems().size()) / (float)ITEM_COLUMNS);
        int additionalLines = Math.max(0, 4 - maxItemRows); // Adding more lines to the description if number of items is low, and therefore there's more space.

        int textLineHeight = font.lineHeight + TextElement.DEFAULT_LINE_SPACING;

        Point lastElementEndPos;

        // TITLE
        final Component titleComponent = debugMode ? new TextComponent(new LoremIpsumGenerator().words(30)) :
                ware.description().title().isBlank() ? new TranslatableComponent(ModLangKeys.GUI_PURCHASE_REQUEST_TITLE) : new TextComponent(ware.description().title());

        int maxTitleLines = Math.min(3, 1 + additionalLines);
        int titleWidth = imageWidth - TITLE_SIDE_MARGIN * 2;
        int titleDesiredHeight = TextElement.measure(font, titleComponent, titleWidth, TextElement.DEFAULT_LINE_SPACING);
        int titleHeight = Math.min(titleDesiredHeight, maxTitleLines * textLineHeight);
        lastElementEndPos = addElement(
                new TextElement<>(this, TITLE_ID, fromLeft(TITLE_SIDE_MARGIN), fromTop(TITLE_Y), titleWidth, titleHeight, titleComponent)
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .color(FONT_COLOR));


        // BUYER
        if (!ware.description().buyer().isBlank()){
            Component buyerComponent = new TranslatableComponent(ModLangKeys.GUI_PURCHASE_REQUEST_FROM).withStyle(ChatFormatting.ITALIC)
                    .append(new TextComponent(": " + ware.description().buyer()));

            lastElementEndPos = addElement(
                    new TextElement<>(this, BUYER_ID, fromLeft(10), lastElementEndPos.y + MESSAGE_Y_MARGIN,
                            imageWidth - 10 * 2, textLineHeight, buyerComponent)
                            .horizontalAlignment(HorizontalAlignment.CENTER)
                            .color(FONT_COLOR));
        }


        // MESSAGE
        final Component messageComponent = debugMode ? new TextComponent(new LoremIpsumGenerator().words(10)).withStyle(ChatFormatting.ITALIC) :
                new TextComponent(ware.description().message()).withStyle(ChatFormatting.ITALIC);

        int titleLines = Math.min(titleDesiredHeight / textLineHeight, maxTitleLines);
        int maxMessageLines = 5 + additionalLines + (maxTitleLines - titleLines);
        int messageWidth = imageWidth - MESSAGE_SIDE_MARGIN * 2;
        int messageStartY = lastElementEndPos.y + MESSAGE_Y_MARGIN;
        int messageDesiredHeight = TextElement.measure(font, messageComponent, messageWidth, TextElement.DEFAULT_LINE_SPACING);
        int messageHeight = Math.min(messageDesiredHeight, maxMessageLines * textLineHeight);
        lastElementEndPos = addElement(
                new TextElement<>(this, MESSAGE_ID, fromLeft(MESSAGE_SIDE_MARGIN), messageStartY, messageWidth, messageHeight, messageComponent)
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .color(FONT_COLOR));

        int itemsStartY = lastElementEndPos.y + MESSAGE_Y_MARGIN;
        int itemsMaxHeight = getGuiTop() + imageHeight - itemsStartY - 25;

        addRequestedItems(ware.requestedItems(), new Rectangle(fromLeft(18), itemsStartY, ITEM_COLUMNS * 18, itemsMaxHeight));
        addPaymentItems(ware.paymentItems(), new Rectangle(fromRight(18, ITEM_COLUMNS * 18), itemsStartY, ITEM_COLUMNS * 18, itemsMaxHeight));

        // ARROW
        Point arrowEndPos = addElement(new ScreenElement<>(this, "arrow",
                getGuiCenterX() - 18 / 2, itemsStartY + 5, 19, 11) {
            @Override
            public void renderBg(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                blit(poseStack, posX, posY, imageWidth, 0, width, height);
            }
        });

        // XP
        if (ware.experience() > 0){
            int width = 12 + 2 + font.width(Integer.toString(ware.experience()));
            addElement(new ExperienceElement(this, "experience_reward", getGuiCenterX() + 1 - width / 2,
                    arrowEndPos.y + 4,  width, 10, ware.experience()));
        }

        // SEAL
        addElement(new SealLogoElement(this, SEAL_LOGO_ID, fromLeft(69), fromTop(222), ware.rarity()));
    }

    private void addRequestedItems(List<WareItem> items, Rectangle bounds) {

        int rows = Mth.clamp(Mth.ceil(items.size() / (float) ITEM_COLUMNS), 1, Math.round(bounds.height / 18f));
        int actualHeight = rows * 18;
        int startY = bounds.y;
//        int startY = (bounds.y + bounds.height / 2) - (actualHeight / 2);

        List<ScreenElement<?>> elements = new ArrayList<>();
        IntStream.range(0, Math.min(rows * ITEM_COLUMNS, items.size()))
                .forEach(index -> elements.add(new RequestedItemElement(this, "requested_item_" + index, 0,0, items.get(index))));

        gridLayout(elements, rows, ITEM_COLUMNS, new Rectangle(bounds.x, startY, ITEM_COLUMNS * 18, actualHeight),
                rows == 1 ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER);

        int maxDisplayedItems = ITEM_COLUMNS * rows;

        if (items.size() > maxDisplayedItems){
            List<Component> leftoverItems = items.stream()
                    .skip(maxDisplayedItems)
                    .map(wItem -> (Component)wItem.getDescriptiveName())
                    .toList();

            int elementWidth = 26;
            addElement(new TextElement<>(this, "more_requested_items",
                    (bounds.x + bounds.width / 2) - elementWidth / 2,
                    startY + actualHeight + 2,
                    elementWidth, 12, () -> new TextComponent("-+-").withStyle(ChatFormatting.BOLD)){
                @Override
                public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
                    PurchaseRequestScreen.this.renderTooltip(poseStack, leftoverItems, Optional.empty(), mouseX, mouseY);
                }
            }
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .color(FONT_COLOR));
        }
    }

    private void addPaymentItems(List<ItemStack> items, Rectangle bounds){
        int rows = Mth.clamp(Mth.ceil(items.size() / (float) ITEM_COLUMNS), 1, Math.round(bounds.height / 18f));
        int actualHeight = rows * 18;
        int startY = bounds.y;
//        int startY = (bounds.y + bounds.height / 2) - (actualHeight / 2);

        List<ScreenElement<?>> elements = new ArrayList<>();
        IntStream.range(0, Math.min(rows * ITEM_COLUMNS, items.size()))
                .forEach(index -> {
                    ItemStack stack = items.get(index);
                    elements.add(new ScreenElement<>(this, "requested_item_" + index, 0, 0, 18, 18) {
                        @Override
                        public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                            screen.getItemRenderer().renderGuiItem(stack, posX, posY);
                            screen.getItemRenderer().renderGuiItemDecorations(screen.getFontRenderer(), stack, posX, posY);
                        }

                        @Override
                        public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
                            PurchaseRequestScreen.this.renderTooltip(poseStack, stack, mouseX, mouseY);
                        }
                    });
                });

        gridLayout(elements, rows, ITEM_COLUMNS, new Rectangle(bounds.x, startY, ITEM_COLUMNS * 18, actualHeight),
                rows == 1 ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER);

        int maxDisplayedItems = ITEM_COLUMNS * rows;

        if (items.size() > maxDisplayedItems){
            List<Component> leftoverItems = items.stream()
                    .skip(maxDisplayedItems)
                    .map(s -> (Component)((BaseComponent) s.getHoverName()).append(new TextComponent(" x" + s.getCount())))
                    .toList();

            int elementWidth = 26;
            addElement(new TextElement<>(this, "more_payment_items",
                    (bounds.x + bounds.width / 2) - elementWidth / 2,
                    startY + actualHeight + 2,
                    elementWidth, 12, () -> new TextComponent("-+-").withStyle(ChatFormatting.BOLD)){
                @Override
                public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
                    PurchaseRequestScreen.this.renderTooltip(poseStack, leftoverItems, Optional.empty(), mouseX, mouseY);
                }
            }
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .color(FONT_COLOR));
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}

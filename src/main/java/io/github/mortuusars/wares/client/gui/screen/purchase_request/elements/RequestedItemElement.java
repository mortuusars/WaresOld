package io.github.mortuusars.wares.client.gui.screen.purchase_request.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.client.gui.screen.base.BaseContainerScreen;
import io.github.mortuusars.wares.client.gui.screen.base.ScreenElement;
import io.github.mortuusars.wares.core.ware.data.WareItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequestedItemElement extends ScreenElement<BaseContainerScreen<?>> {

    private final WareItem item;
    private final boolean isTag;

    private ItemStack renderedItemStack;

    private final Component tooltip;

    private long lastChangedTick;

    public RequestedItemElement(Screen parentScreen, String id, int posX, int posY, WareItem item) {
        super((BaseContainerScreen<?>) parentScreen, id, posX, posY, 18, 18);

        this.item = item;
        this.isTag = item.isTag();

        renderedItemStack = new ItemStack(item.getItemOrFirstItemFromTag());
        renderedItemStack.setCount(item.count().left().orElse(1));
        if (item.nbt().isPresent())
            renderedItemStack.setTag(item.nbt().get());

        tooltip = isTag ? new TextComponent("#" + item.tag().location()) : renderedItemStack.getHoverName();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        screen.getItemRenderer().renderGuiItem(renderedItemStack, posX, posY);
        screen.getItemRenderer().renderGuiItemDecorations(screen.getFontRenderer(), renderedItemStack, posX, posY);

        // Cycle item from tag every second:
        if (isTag && screen.gameTick != lastChangedTick && screen.gameTick % 20 == 0){
            lastChangedTick = screen.gameTick;
            cycleTagItemStack();
        }
    }

    @Override
    public void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        screen.renderTooltip(poseStack, tooltip, mouseX, mouseY);
    }

    private void cycleTagItemStack() {
        List<Item> tagItems = ForgeRegistries.ITEMS.tags().getTag(item.tag()).stream().toList();
        int currentIndex = tagItems.indexOf(renderedItemStack.getItem());
        if (tagItems.size() == 0 || currentIndex == -1)
            return;

        int nextIndex = currentIndex == tagItems.size() -1 ? 0 : currentIndex + 1;
        Item item = tagItems.get(nextIndex);
        ItemStack newRenderedItemStack = new ItemStack(item);
        newRenderedItemStack.setCount(renderedItemStack.getCount());
        renderedItemStack = newRenderedItemStack;
    }
}

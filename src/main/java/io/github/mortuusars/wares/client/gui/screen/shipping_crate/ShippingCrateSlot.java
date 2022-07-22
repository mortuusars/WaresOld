package io.github.mortuusars.wares.client.gui.screen.shipping_crate;

import io.github.mortuusars.wares.client.gui.BillSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ShippingCrateSlot extends SlotItemHandler {

    public BillSlot billSlot;

    public ShippingCrateSlot(BillSlot billSlot, int index, int xPosition, int yPosition) {
        super(billSlot.getItemHandler(), index, xPosition, yPosition);
        this.billSlot = billSlot;
    }

    @Override
    public IItemHandler getItemHandler() {
        return billSlot.getItemHandler();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isActive() {
        return billSlot.hasItem();
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return billSlot.hasItem();
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return billSlot.hasItem();
    }
}

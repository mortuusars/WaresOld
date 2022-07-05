package io.github.mortuusars.wares.inventory.menu;

import com.mojang.datafixers.util.Pair;
import io.github.mortuusars.wares.client.gui.BillSlot;
import io.github.mortuusars.wares.client.gui.screen.ShippingCrateScreen;
import io.github.mortuusars.wares.common.ShippingCrate;
import io.github.mortuusars.wares.common.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModContainers;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class ShippingCrateMenu extends WaresAbstractContainerMenu {
    private final ShippingCrateBlockEntity blockEntity;
    private final Level level;
    private final Inventory playerInventory;

    public NonNullList<ItemStack> paymentItems;

    public Ware ware;

    public ShippingCrateMenu(int pContainerId, Inventory playerInventory, BlockPos pos) {
        super(ModContainers.SHIPPING_CRATE.get(), pContainerId);
        level = playerInventory.player.getLevel();
        blockEntity = (ShippingCrateBlockEntity) level.getBlockEntity(pos);
        this.playerInventory = playerInventory;


        if (blockEntity == null)
            return;

        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
            handler -> {
                addContainerSlots(handler, 0,18);
                addSlot(new SlotItemHandler(handler, ShippingCrate.BILL_SLOT_INDEX, 124, 36){
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player playerIn) {
                        return false;
                    }
                });
                addPlayerInventory(this.playerInventory, 9, 26, 131);
                addPlayerHotbar(this.playerInventory, 0, 26, 189);
            });

        WareUtils.readWareFromStackNBT(this.slots.get(ShippingCrate.BILL_SLOT_INDEX).getItem()).ifPresent(w -> ware = w);
        paymentItems = blockEntity.getPaymentItems();

        blockEntity.startOpen(playerInventory.player);
    }

    public ShippingCrateBlockEntity getBlockEntity(){
        return blockEntity;
    }

    private void addContainerSlots(IItemHandler handler, int startIndex, int yPos) {
        int index = startIndex;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 6; column++){
                addSlot(new SlotItemHandler(handler, index++, 8 + column * 18, yPos + row * 18));
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(),
                blockEntity.getBlockPos()), pPlayer, ModBlocks.SHIPPING_CRATE.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int elementID) {
        if (elementID == ShippingCrateScreen.PROGRESS_ARROW_ID){
            blockEntity.shipWare(player);
        }

        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            itemstack = slotItemStack.copy();
            if (index < ShippingCrate.ITEM_SLOTS){
                if (!this.moveItemStackTo(slotItemStack, ShippingCrate.ITEM_SLOTS, this.slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(slotItemStack, 0, ShippingCrate.ITEM_SLOTS, false))
                return ItemStack.EMPTY;


            if (slotItemStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return itemstack;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }

    public Pair<Integer, Integer> getProgress(){
        Slot slot = slots.get(ShippingCrate.BILL_SLOT_INDEX);
        ItemStack billStack = slot.getItem();
        Optional<Ware> wareOptional = WareUtils.readWareFromStackNBT(billStack);
        if(wareOptional.isPresent()){
            Ware ware = wareOptional.get();

            Map<FixedWareItemInfo, Integer> requested = new HashMap<>();
            int requestedCount = ware.requestedItems.stream().mapToInt(i -> i.count).sum();
            for (int i = 0; i < ShippingCrate.ITEM_SLOTS; i++) {
                ItemStack stack = this.slots.get(i).getItem();
                ware.getMatchingRequestedItem(stack).ifPresent(item -> {
                    if (requested.containsKey(item)){
                        int newCount = requested.get(item) + stack.getCount();
                        requested.put(item, newCount);
                    }
                    else {
                        requested.put(item, stack.getCount());
                    }
                });
            }

            int currentCount = 0;
            for (var reqItem : requested.entrySet())
                currentCount += Math.min(reqItem.getKey().count, reqItem.getValue());

            return Pair.of(requestedCount, currentCount);
        }

        return Pair.of(0, 0);
    }
}

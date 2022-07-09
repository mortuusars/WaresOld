package io.github.mortuusars.wares.common.payment_parcel;

import io.github.mortuusars.wares.common.base.WaresAbstractContainerMenu;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PaymentParcelMenu extends WaresAbstractContainerMenu {

    private final Inventory playerInventory;
    private final Level level;
    private final PaymentParcelBlockEntity blockEntity;

    public PaymentParcelMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(ModContainers.PAYMENT_PARCEL.get(), containerId);
        this.playerInventory = playerInventory;
        this.level = playerInventory.player.getLevel();
        this.blockEntity = (PaymentParcelBlockEntity) level.getBlockEntity(pos);

        assert blockEntity != null;
        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int index = 0;
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 7; column++) {
                    addSlot(new SlotItemHandler(handler, index, 26 + column * 18, 18 + row * 18){
                        @Override
                        public boolean mayPlace(@NotNull ItemStack stack) {
                            return false;
                        }
                    });
                    index++;
                }
            }
        });

        this.addPlayerInventory(playerInventory, 84);
        this.addPlayerHotbar(playerInventory, 142);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            itemstack = slotItemStack.copy();
            if (index < PaymentParcel.SLOTS){
                if (!this.moveItemStackTo(slotItemStack, PaymentParcel.SLOTS, this.slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(slotItemStack, 0, PaymentParcel.SLOTS, false))
                return ItemStack.EMPTY;


            if (slotItemStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.getLevel(),
                blockEntity.getBlockPos()), player, ModBlocks.PAYMENT_PARCEL.get());
    }
}

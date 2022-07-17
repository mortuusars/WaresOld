package io.github.mortuusars.wares.common.mailbox;

import io.github.mortuusars.wares.common.base.WaresAbstractContainerMenu;
import io.github.mortuusars.wares.common.mailbox.Mailbox;
import io.github.mortuusars.wares.common.mailbox.MailboxBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrate;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModContainers;
import io.github.mortuusars.wares.setup.ModItems;
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

public class MailboxMenu extends WaresAbstractContainerMenu {

    private final Level level;
    private final MailboxBlockEntity blockEntity;
    private final Inventory playerInventory;

    public MailboxMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(ModContainers.MAILBOX.get(), containerId);
        this.level = playerInventory.player.getLevel();
        this.blockEntity = (MailboxBlockEntity) level.getBlockEntity(pos);
        this.playerInventory = playerInventory;

        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int index = 0;
            for (int row = 0; row < 5; row++) {
                for (int column = 0; column < 4; column++) {
                    addSlot(new SlotItemHandler(handler, index, 53 + column * 18, 18 + row * 18));
                    index++;
                }
            }

            addSlot(new SlotItemHandler(handler, Mailbox.TRASH_SLOT_ID, 140, 91){
                @Override
                public boolean mayPickup(Player playerIn) {
                    return false;
                }

                @Override
                public boolean isActive() {
                    return getCarried().is(ModItems.PURCHASE_REQUEST.get());
                }
            });
        });

        this.addPlayerInventory(playerInventory, 120);
        this.addPlayerHotbar(playerInventory, 178);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(),
                blockEntity.getBlockPos()), pPlayer, ModBlocks.MAILBOX.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            itemstack = slotItemStack.copy();
            if (index < Mailbox.MAIL_SLOTS){
                if (!this.moveItemStackTo(slotItemStack, Mailbox.MAIL_SLOTS, this.slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(slotItemStack, 0, Mailbox.MAIL_SLOTS, false))
                return ItemStack.EMPTY;


            if (slotItemStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return itemstack;
    }
}

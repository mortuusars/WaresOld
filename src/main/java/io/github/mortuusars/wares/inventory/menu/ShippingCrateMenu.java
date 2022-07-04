package io.github.mortuusars.wares.inventory.menu;

import io.github.mortuusars.wares.common.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ShippingCrateMenu extends WaresAbstractContainerMenu {

    private final ShippingCrateBlockEntity _blockEntity;
    private final Level _level;
    private final Inventory _playerInventory;

    public ShippingCrateMenu(int pContainerId, Inventory playerInventory, BlockPos pos) {
        super(ModContainers.SHIPPING_CRATE.get(), pContainerId);
        _level = playerInventory.player.getLevel();
        _blockEntity = (ShippingCrateBlockEntity)_level.getBlockEntity(pos);
        _playerInventory = playerInventory;

        if (_blockEntity == null)
            return;

        _blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
            handler -> {
                addContentSlots(handler, 0,44);
                addPlayerInventory(_playerInventory, 114);
                addPlayerHotbar(_playerInventory, 172);
            });

        _blockEntity.startOpen(playerInventory.player);
    }

    public ShippingCrateBlockEntity getBlockEntity(){
        return _blockEntity;
    }

    private void addContentSlots(IItemHandler handler, int startIndex, int yPos) {

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++){
                addSlot(new SlotItemHandler(handler,column + row * 9 + startIndex, 8 + column * 18, yPos + row * 18));
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(),
                _blockEntity.getBlockPos()), pPlayer, ModBlocks.SHIPPING_CRATE.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(index);
//        if (slot.hasItem()) {
//            ItemStack stack = slot.getItem();
//            itemstack = stack.copy();
//            if (index == 0) {
//                if (!this.moveItemStackTo(stack, 1, 37, true)) {
//                    return ItemStack.EMPTY;
//                }
//                slot.onQuickCraft(stack, itemstack);
//            } else {
//                if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0) {
//                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                } else if (index < 28) {
//                    if (!this.moveItemStackTo(stack, 28, 37, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                } else if (index < 37 && !this.moveItemStackTo(stack, 1, 28, false)) {
//                    return ItemStack.EMPTY;
//                }
//            }
//
//            if (stack.isEmpty()) {
//                slot.set(ItemStack.EMPTY);
//            } else {
//                slot.setChanged();
//            }
//
//            if (stack.getCount() == itemstack.getCount()) {
//                return ItemStack.EMPTY;
//            }
//
//            slot.onTake(playerIn, stack);
//        }

        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this._blockEntity.stopOpen(player);
    }
}

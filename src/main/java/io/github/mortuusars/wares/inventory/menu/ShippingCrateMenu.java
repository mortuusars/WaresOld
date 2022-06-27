package io.github.mortuusars.wares.inventory.menu;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.client.gui.BillSlot;
import io.github.mortuusars.wares.client.gui.ShippingCrateSlot;
import io.github.mortuusars.wares.content.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class ShippingCrateMenu extends WaresAbstractContainerMenu {

    private final ShippingCrateBlockEntity _blockEntity;
    private final Level _level;
    private final Inventory _playerInventory;

    public BillSlot billSlot;

    public ShippingCrateMenu(int pContainerId, Inventory playerInventory, BlockPos pos) {
        super(ModContainers.SHIPPING_CRATE.get(), pContainerId);
        _level = playerInventory.player.getLevel();
        _blockEntity = (ShippingCrateBlockEntity)_level.getBlockEntity(pos);
        _playerInventory = playerInventory;

        if (_blockEntity == null){
            LogUtils.getLogger().error("Something wrong happened - blockEntity is null. Shipping Crate will not function as expected.");
            return;
        }

        _blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
            handler -> {
                this.billSlot = new BillSlot(handler, 0, 80, 18);
                addSlot(billSlot);
                addContentSlots(handler, 1,44);
                addPlayerInventory(_playerInventory, 114);
                addPlayerHotbar(_playerInventory, 172);
            });
    }

    private void addContentSlots(IItemHandler handler, int startIndex, int yPos) {

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++){
                addSlot(new ShippingCrateSlot(billSlot,
                        column + row * 9 + startIndex, 8 + column * 18, yPos + row * 18));
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(),
                _blockEntity.getBlockPos()), pPlayer, ModBlocks.SHIPPING_CRATE.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;

        LogUtils.getLogger().error("Not implemented!");

        return itemStack;
    }
}

package io.github.mortuusars.wares.common.payment_parcel;

import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaymentParcelBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public PaymentParcelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PAYMENT_PARCEL.get(), pos, blockState, PaymentParcel.SLOTS);
    }

    @Override
    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(slots){

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        };
    }

    // Without this method hoppers can still insert items:
    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("container.payment_parcel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new PaymentParcelMenu(containerID, playerInventory, this.getBlockPos());
    }
}

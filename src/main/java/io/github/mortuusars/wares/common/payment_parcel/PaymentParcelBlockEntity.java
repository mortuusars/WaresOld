package io.github.mortuusars.wares.common.payment_parcel;

import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class PaymentParcelBlockEntity extends InventoryBlockEntity {
    public PaymentParcelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int slots) {
        super(type, pos, blockState, slots);
    }

    @Override
    public int getContainerSize() {
        return PaymentParcel.SLOTS;
    }

    @Override
    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(PaymentParcel.SLOTS){

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}

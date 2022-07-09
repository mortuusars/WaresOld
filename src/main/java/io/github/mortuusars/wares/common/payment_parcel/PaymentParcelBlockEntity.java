package io.github.mortuusars.wares.common.payment_parcel;

import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateMenu;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaymentParcelBlockEntity extends InventoryBlockEntity implements MenuProvider {

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            PaymentParcelBlockEntity.this.playSound(blockState, SoundEvents.BARREL_OPEN);
            PaymentParcelBlockEntity.this.updateBlockState(blockState, true);
        }

        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            PaymentParcelBlockEntity.this.playSound(blockState, SoundEvents.BARREL_CLOSE);
            PaymentParcelBlockEntity.this.updateBlockState(blockState, false);
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof PaymentParcelMenu parcelMenu && parcelMenu.getBlockEntity() == PaymentParcelBlockEntity.this;
        }
    };

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

    public static <T extends BlockEntity> void tickServer(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof PaymentParcelBlockEntity parcelEntity){
            if (parcelEntity.isEmpty() && parcelEntity.openersCounter.getOpenerCount() == 0){
                level.removeBlock(pos, false);
            }
        }
    }


    // <OpenersCounter>

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator())
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator())
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void recheckOpen() {
        if (!this.remove)
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    private void updateBlockState(@NotNull BlockState blockState, boolean isOpen) {
//        this.level.setBlock(this.getBlockPos(), blockState.setValue(BarrelBlock.OPEN, isOpen), Block.UPDATE_ALL);
    }

    private void playSound(@NotNull BlockState pState, SoundEvent pSound) {
//        Vec3i vec3i = pState.getValue(BlockStateProperties.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() /*+ 0.5D + (double)vec3i.getX() / 2.0D*/;
        double d1 = (double)this.worldPosition.getY() /*+ 0.5D + (double)vec3i.getY() / 2.0D*/;
        double d2 = (double)this.worldPosition.getZ() /*+ 0.5D + (double)vec3i.getZ() / 2.0D*/;
        this.level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}

package io.github.mortuusars.wares.common.parcel;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import io.github.mortuusars.wares.common.extensions.IOpenersCounter;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParcelBlockEntity extends InventoryBlockEntity implements MenuProvider, IOpenersCounter {

    private final ContainerOpenersCounter openersCounter = createOpenersCounter();

    public ParcelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PAYMENT_PARCEL.get(), pos, blockState, Parcel.SLOTS);
    }


    public static <T extends BlockEntity> void tickServer(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof ParcelBlockEntity parcelEntity && parcelEntity.isEmpty() && parcelEntity.openersCounter.getOpenerCount() == 0){
            if (blockState.getBlock() instanceof ParcelBlock parcelBlock)
                parcelBlock.removeBlock(level, pos);
            else
                LogUtils.getLogger().error("Cannot remove Parcel block on tick: wrong block. Expected: {}, Got: {}",
                        ParcelBlock.class.getSimpleName(), blockState.getBlock().getClass().getSimpleName());
        }
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


    // <Container>

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("container.payment_parcel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ParcelMenu(containerID, playerInventory, this.getBlockPos());
    }


    // <OpenersCounter>

    @Override
    public ContainerOpenersCounter getOpenersCounter() {
        return openersCounter;
    }

    @Override
    public boolean isValidContainer(Player player) {
        return player.containerMenu instanceof ParcelMenu menu && menu.getBlockEntity() == this;
    }

    private void playSound(@NotNull BlockState pState, SoundEvent pSound) {
        double d0 = (double)this.worldPosition.getX() + 0.5D;
        double d1 = (double)this.worldPosition.getY() + 0.5D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D;
        this.level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}

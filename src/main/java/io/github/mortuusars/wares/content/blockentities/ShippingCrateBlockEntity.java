package io.github.mortuusars.wares.content.blockentities;

import io.github.mortuusars.wares.inventory.menu.ShippingCrateMenu;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ShippingCrateBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler _itemStackHandler = createItemStackHandler();
    private final LazyOptional<IItemHandler> _itemHandler = LazyOptional.of(() -> _itemStackHandler);

    public ShippingCrateBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SHIPPING_CRATE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TextComponent("container.shipping_crate");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShippingCrateMenu(pContainerId, pPlayerInventory, this.getBlockPos());
    }

    private @NotNull ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(28) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0)
                    return stack.getItem() == ModItems.BILL_OF_LADING.get();
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 0 && stack.getItem() != ModItems.BILL_OF_LADING.get())
                    return stack;

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        _itemHandler.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return _itemHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", _itemStackHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("Inventory"))
            _itemStackHandler.deserializeNBT(tag.getCompound("Inventory"));
        super.load(tag);
    }
}

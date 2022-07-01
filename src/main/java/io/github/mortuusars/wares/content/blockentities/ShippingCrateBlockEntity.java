package io.github.mortuusars.wares.content.blockentities;

import io.github.mortuusars.wares.content.blocks.ShippingCrateBlock;
import io.github.mortuusars.wares.inventory.menu.ShippingCrateMenu;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
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

    private final int SLOT_COUNT = 27;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private final ItemStackHandler _itemStackHandler = createItemStackHandler();
    private final LazyOptional<IItemHandler> _itemHandler = LazyOptional.of(() -> _itemStackHandler);

    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155062_, BlockPos p_155063_, BlockState blockState) {
            ShippingCrateBlockEntity.this.playSound(blockState, SoundEvents.BARREL_OPEN);
            ShippingCrateBlockEntity.this.updateBlockState(blockState, true);
        }

        protected void onClose(Level p_155072_, BlockPos p_155073_, BlockState blockState) {
            ShippingCrateBlockEntity.this.playSound(blockState, SoundEvents.BARREL_CLOSE);
            ShippingCrateBlockEntity.this.updateBlockState(blockState, false);
        }

        protected void openerCountChanged(Level p_155066_, BlockPos p_155067_, BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof ShippingCrateMenu shippingCrateMenu && shippingCrateMenu.getBlockEntity() == ShippingCrateBlockEntity.this;
        }
    };


    public ShippingCrateBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SHIPPING_CRATE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("container.shipping_crate");
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

    public void startOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator())
            this.openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void stopOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator())
            this.openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void recheckOpen() {
        if (!this.remove)
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    void updateBlockState(BlockState pState, boolean pOpen) {
        this.level.setBlock(this.getBlockPos(), pState.setValue(BarrelBlock.OPEN, Boolean.valueOf(pOpen)), 3);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = pState.getValue(BarrelBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound((Player)null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}

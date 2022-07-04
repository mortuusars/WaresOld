package io.github.mortuusars.wares.common.blockentities;

import io.github.mortuusars.wares.inventory.menu.ShippingCrateMenu;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
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

public class ShippingCrateBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public static final int SLOTS = 28;
    public static final int BILL_SLOT_INDEX = 27;

    private ContainerOpenersCounter _openersCounter = new ContainerOpenersCounter() {
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


    public ShippingCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SHIPPING_CRATE_BLOCK_ENTITY.get(), pos, blockState, SLOTS);
    }

    public ItemStack getBillStack(){
        return this.getItem(BILL_SLOT_INDEX);
    }

    public void setBillStack(ItemStack billOfLadingStack){
        if (!billOfLadingStack.is(ModItems.BILL_OF_LADING.get()))
            throw new IllegalArgumentException("Only 'wares:bill_of_lading' is allowed.");

        this.insertItem(BILL_SLOT_INDEX, billOfLadingStack);
    }

    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
//        if (!(blockEntity instanceof ShippingCrateBlockEntity shippingCrateEntity))
//            return;
//
//        Ware ware = shippingCrateEntity.getWare();
//        if (ware == null)
//            return;
//
//        Map<FixedWareItemInfo, AtomicInteger> items = new HashMap<>();
//
//        for (var item : ware.requestedItems)
//            items.put(item, new AtomicInteger(0));
//
//        for (int i = 0; i < shippingCrateEntity._itemStackHandler.getSlots(); i++) {
//            for (var reqItem : ware.requestedItems){
//                ItemStack stack = shippingCrateEntity._itemStackHandler.getStackInSlot(i);
//                if (reqItem.matches(stack)){
//                    items.get(reqItem).getAndUpdate(count -> count + stack.getCount());
//                }
//            }
//        }
//
//        for (var reqItem2 : ware.requestedItems){
//            if (reqItem2.getCount() > items.get(reqItem2).get())
//                return;
//        }
//
//
////        for (int i = 0; i < shippingCrateEntity._itemStackHandler.getSlots(); i++) {
////            shippingCrateEntity._itemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
////        }
//        level.removeBlockEntity(blockPos);
//        level.removeBlock(blockPos, false);
//
//        double x = blockPos.getX() + 0.5;
//        double y = blockPos.getY() + 0.5;
//        double z = blockPos.getZ() + 0.5;
//
//        for (var paymentItem : ware.paymentItems){
//            Optional<ItemStack> itemStack = paymentItem.toItemStack();
//            itemStack.ifPresent(stack -> {
//                level.addFreshEntity(new ItemEntity(level, x, y, z, stack));
//            });
//        }

    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("container.shipping_crate");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ShippingCrateMenu(containerId, playerInventory, this.getBlockPos());
    }

    @Override
    protected @NotNull ItemStackHandler createInventory() {
        return new ItemStackHandler(SLOTS) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot != 28 || stack.is(ModItems.BILL_OF_LADING.get());
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator())
            this._openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator())
            this._openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public void recheckOpen() {
        if (!this.remove)
            this._openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    void updateBlockState(BlockState blockState, boolean isOpen) {
        this.level.setBlock(this.getBlockPos(), blockState.setValue(BarrelBlock.OPEN, isOpen), Block.UPDATE_ALL);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = pState.getValue(BlockStateProperties.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound((Player)null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}

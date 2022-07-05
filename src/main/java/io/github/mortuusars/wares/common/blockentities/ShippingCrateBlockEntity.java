package io.github.mortuusars.wares.common.blockentities;

import com.mojang.logging.LogUtils;
import com.mojang.math.Vector3f;
import io.github.mortuusars.wares.common.ShippingCrate;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.inventory.menu.ShippingCrateMenu;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.utils.PosUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class ShippingCrateBlockEntity extends InventoryBlockEntity implements MenuProvider {

    private Ware ware;
    private float progress;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            ShippingCrateBlockEntity.this.playSound(blockState, SoundEvents.BARREL_OPEN);
            ShippingCrateBlockEntity.this.updateBlockState(blockState, true);
        }

        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
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
        super(ModBlockEntities.SHIPPING_CRATE_BLOCK_ENTITY.get(), pos, blockState, ShippingCrate.SLOTS);
    }

    public ItemStack getBillStack(){
        return this.getItem(ShippingCrate.BILL_SLOT_INDEX);
    }

    public void setBillStack(ItemStack billOfLadingStack){
        if (!billOfLadingStack.is(ModItems.BILL_OF_LADING.get()))
            throw new IllegalArgumentException("Only 'wares:bill_of_lading' is allowed.");

        Optional<Ware> ware = WareUtils.readWareFromStackNBT(billOfLadingStack);
        ware.ifPresent(w -> {
            this.ware = w;
//            requestedCount = w.requestedItems.stream().mapToInt(wItem -> wItem.count).sum();
            updateShippingProgress(0);
        });

        this.insertItem(ShippingCrate.BILL_SLOT_INDEX, billOfLadingStack);
    }

    public NonNullList<ItemStack> getPaymentItems() {
        Optional<Ware> wareOptional = WareUtils.readWareFromStackNBT(this.getBillStack());
        if (wareOptional.isPresent()){
            Ware ware = wareOptional.get();
            return ware.getPaymentStacks();
        }
        return NonNullList.of(new ItemStack(Items.BARRIER));
    }

    public float getProgress(){
        return progress;
    }

    @Override
    public int getContainerSize() {
        return ShippingCrate.SLOTS;
    }

    @Override
    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(slots){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                updateShippingProgress(slot);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot != ShippingCrate.BILL_SLOT_INDEX || stack.is(ModItems.BILL_OF_LADING.get());
            }
        };
    }

    private void updateShippingProgress(int slot) {
        if (ware == null)
            return;

//        if (!isInventoryKnown){
//            for (int i = 0; i < ShippingCrate.ITEM_SLOTS; i++) {
//                ItemStack itemStack = this.getItem(i);
//                itemStacks.set(i, itemStack);
//                ware.getMatchingRequestedItem(itemStack).ifPresent(wItem -> {
//
//                    matchedRequestedItems.put(wItem, )
//
//                });
//            }
//            isInventoryKnown = true;
//        }
//
//        itemStacks.set(slot, this.getItem(slot));

        HashMap<FixedWareItemInfo, Integer> matchedItems = new HashMap<>();

        for (int i = 0; i < ShippingCrate.ITEM_SLOTS; i++) {
            ItemStack itemStack = this.getItem(i);
            ware.getMatchingRequestedItem(itemStack).ifPresent(requestedItem -> matchedItems.put(requestedItem, itemStack.getCount()));
        }

        int requestedCount = 0;
        int currentCount = 0;

        for (var m : matchedItems.entrySet()){
            requestedCount += m.getKey().count;
            currentCount += m.getValue();
        }

        if (currentCount == 0)
            progress = 0f;
        else
            progress = Math.min(1f, requestedCount / (float)currentCount);
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
        this.level.setBlock(this.getBlockPos(), blockState.setValue(BarrelBlock.OPEN, isOpen), Block.UPDATE_ALL);
    }

    private void playSound(@NotNull BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = pState.getValue(BlockStateProperties.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }


    // <Save/Load>

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        String wareString = tag.getString("Ware");
        WareUtils.deserialize(wareString).ifPresentOrElse(w -> this.ware = w,
                () -> LogUtils.getLogger().error("Ware was not deserialized and would not be loaded. Value: " + wareString));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        WareUtils.serialize(ware).ifPresentOrElse(w -> tag.putString("Ware", w),
                () -> LogUtils.getLogger().error("Ware would not be saved in a block entity. Value: " + ware));
    }

    public void shipWare(Player player) {
        Level level = player.getLevel();
        ShippingCrate.Shipping shippingResult = ShippingCrate.getShippingResult(ware, this.getItems());
        if (shippingResult.isFulfilled()){
            this.clearContent();
            Container paymentItemsContainer = new SimpleContainer(shippingResult.paymentItems.toArray(new ItemStack[0]));
            Containers.dropContents(level, worldPosition, paymentItemsContainer);
            level.removeBlock(this.worldPosition, false);

            Vector3f pos = PosUtils.blockCenter(worldPosition);
            level.playSound(player, pos.x(), pos.y(), pos.z(), SoundEvents.SCAFFOLDING_BREAK, SoundSource.BLOCKS, 0.5f, 1f);
        }
    }
}

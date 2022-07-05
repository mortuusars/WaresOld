package io.github.mortuusars.wares.common.blockentities;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public abstract class InventoryBlockEntity extends BlockEntity implements Container {

    public final int slots;
    protected int tickCounter;
    protected boolean requiresUpdate;

    public final ItemStackHandler inventory;
    protected LazyOptional<ItemStackHandler> inventoryHandlerLazy;

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, int slots) {
        super(type, worldPosition, blockState);

        Preconditions.checkArgument(slots > 0, "slot count should be larger than 0. Value: {}", slots);
        this.slots = slots;

        inventory = createInventory(slots);
        inventoryHandlerLazy = LazyOptional.of(() -> this.inventory);
    }

    // <Inventory Management>

    public ItemStack insertItem(int slot, ItemStack stack) {
        final ItemStack copy = stack.copy();
        stack.shrink(copy.getCount());
        this.requiresUpdate = true;
        return this.inventoryHandlerLazy.map(inv -> inv.insertItem(slot, copy, false)).orElse(ItemStack.EMPTY);
    }

    public NonNullList<ItemStack> getItems(){
        return getItems(0, getContainerSize());
    }

    public NonNullList<ItemStack> getItems(int startIndexInclusive, int endIndexExclusive) {
        Preconditions.checkArgument(startIndexInclusive < endIndexExclusive, "startIndex should not be larger than endIndex");

        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = startIndexInclusive; i < endIndexExclusive; i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty())
                items.add(stack);
        }
        return items;
    }

    public LazyOptional<ItemStackHandler> getHandler() {
        return this.inventoryHandlerLazy;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return !this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.inventoryHandlerLazy.cast()
                : super.getCapability(cap, side);
    }


    // <Container>

    @Override
    public boolean isEmpty() {
        return IntStream.range(0, this.getContainerSize()).allMatch(i -> this.getItem(i).isEmpty());
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.inventoryHandlerLazy.map(inv -> inv.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        this.requiresUpdate = true;
        return this.inventoryHandlerLazy.map(inv -> inv.extractItem(slot, amount, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        final int stackSize = getItem(slot).getCount();
        return removeItem(slot, stackSize);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        else
            return !(player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) > 64.0D);
    }

    @Override
    public void clearContent() {
        IntStream.range(0, this.getContainerSize()).forEach(i -> setItem(i, ItemStack.EMPTY));
    }


    // <Load/Save>

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.inventory.serializeNBT());
    }


    // <Updating>

    public void tick() {
        this.tickCounter++;
        if (this.requiresUpdate && this.level != null) {
            update();
            this.requiresUpdate = false;
        }
    }

    public void update() {
        requestModelDataUpdate();
        setChanged();
        if (this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryHandlerLazy.invalidate();
    }

    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(slots);
    }
}
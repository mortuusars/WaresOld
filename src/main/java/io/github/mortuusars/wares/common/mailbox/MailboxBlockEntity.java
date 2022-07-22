package io.github.mortuusars.wares.common.mailbox;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import io.github.mortuusars.wares.lib.ModConstants;
import io.github.mortuusars.wares.lib.enums.TimeOfDay;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.setup.ModLangKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MailboxBlockEntity extends InventoryBlockEntity implements MenuProvider {

    private static final Component DISPLAY_NAME = new TranslatableComponent(ModLangKeys.CONTAINER_MAILBOX);

    public MailboxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MAILBOX.get(), pos, blockState, Mailbox.SLOTS);
    }

    @Override
    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(Mailbox.SLOTS){
            @Override
            protected void onContentsChanged(int slot) {
                if (slot == Mailbox.TRASH_SLOT_ID && getItem(slot) != ItemStack.EMPTY)
                    setItem(slot, ItemStack.EMPTY);

                setChanged();
                updateFillLevel();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(ModItems.PURCHASE_REQUEST.get());
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return slot == Mailbox.TRASH_SLOT_ID ? ItemStack.EMPTY : super.insertItem(slot, stack, simulate);
            }
        };
    }

    private void updateFillLevel() {
        if (level == null || level.isClientSide)
            return;

        int mailCount = (int)this.getItems().stream().filter(i -> !i.isEmpty()).count();

        int fillLevel;
        if (mailCount == 0) fillLevel = 0;
        else if (mailCount / (float)Mailbox.MAIL_SLOTS < 0.44f) fillLevel = 1;
        else if (mailCount / (float)Mailbox.MAIL_SLOTS < 0.88f) fillLevel = 2;
        else fillLevel = 3;

        BlockState currentState = level.getBlockState(worldPosition);
        if (currentState.getValue(MailboxBlock.FILL_LEVEL) != fillLevel)
            level.setBlock(worldPosition, currentState.setValue(MailboxBlock.FILL_LEVEL, fillLevel), Block.UPDATE_CLIENTS);
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return index != Mailbox.TRASH_SLOT_ID && stack.is(ModItems.PAYMENT_PARCEL.get());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return DISPLAY_NAME;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new MailboxMenu(containerId, playerInventory, getBlockPos());
    }

    public void onBlockRemoved(BlockState state, BlockState newState) {
        if (level != null)
            Containers.dropContents(level, worldPosition, this);
    }


    // <TICK>

    public static <T extends BlockEntity> void tickServer(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
        if ( !(blockEntity instanceof MailboxBlockEntity mailboxEntity)){
            Wares.LOGGER.warn("TickServer expects MailboxBlockEntity. Got: {}", blockEntity.getClass().getSimpleName());
            return;
        }

        mailboxEntity.tick();

        //TODO: delay after placing? how this works after world start?
//        if (mailboxEntity.tickCounter < 2000)
//            return;

        if (mailboxEntity.tickCounter % 100 != 0)
            return;

        LocalTime currentTime = TimeOfDay.getCurrentTime(level);
        if (!Mailbox.isWorkingHours(level, currentTime) || ModConstants.RANDOM.nextDouble() > Mailbox.getChanceForCurrentTime(currentTime))
            return;

        List<ItemStack> existingRequests = new ArrayList<>();
        List<Integer> emptySlots = new ArrayList<>();

        for (int index = 0; index < Mailbox.MAIL_SLOTS; index++) {
            ItemStack stack = mailboxEntity.getItem(index);
            if (stack.isEmpty())
                emptySlots.add(index);
            else if (stack.is(ModItems.PURCHASE_REQUEST.get()))
                existingRequests.add(stack);
        }

        if (emptySlots.size() == 0)
            return;

        ItemStack newRequest = Mailbox.getRandomPurchaseRequest(existingRequests);
        int slotIndex = emptySlots.get(ModConstants.RANDOM.nextInt(0, emptySlots.size()));
        mailboxEntity.insertItem(slotIndex, newRequest);
    }
}

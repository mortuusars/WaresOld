package io.github.mortuusars.wares.common.shipping_crate;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.WareProgression;
import io.github.mortuusars.wares.common.base.InventoryBlockEntity;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("NullableProblems")
public class ShippingCrateBlockEntity extends InventoryBlockEntity implements MenuProvider {

    private Ware ware;

    private int requestedCount = 0;
    private int fulfilledCount = 0;

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
        super(ModBlockEntities.SHIPPING_CRATE.get(), pos, blockState, ShippingCrate.SLOTS);
    }

    public Ware getWare(){
        return ware;
    }

    public void setWare(@NotNull Ware ware){
        this.ware = ware;
        requestedCount = ware.requestedItems.stream().mapToInt(i -> i.count).sum();
    }

    public NonNullList<ItemStack> getPaymentItems() {
        return ware.getPaymentStacks();
    }

    public float getProgressPercentage(){
        if (requestedCount == 0)
            return 0f;

        return Math.min( Math.max(0f, fulfilledCount / (float)requestedCount), 1f);
    }

    public Pair<Integer, Integer> getProgress(){
        return Pair.of(requestedCount, fulfilledCount);
    }

    private void updateShippingProgress(int slot) {
        if (ware == null){
            requestedCount = 0;
            fulfilledCount = 0;
            return;
        }

        Map<FixedWareItemInfo, Integer> requestedAndCounts = new HashMap<>();
        for (int i = 0; i < ShippingCrate.SLOTS; i++) {
            ItemStack stack = this.getItem(i);
            ware.getMatchingRequestedItem(stack).ifPresent(item -> {
                if (requestedAndCounts.containsKey(item)){
                    int newCount = requestedAndCounts.get(item) + stack.getCount();
                    requestedAndCounts.put(item, newCount);
                }
                else {
                    requestedAndCounts.put(item, stack.getCount());
                }
            });
        }

        fulfilledCount = requestedAndCounts.values().stream().mapToInt(i -> i).sum();
    }


    public void shipWare(Player player) {
        Level level = player.getLevel();
        ShippingCrate.Shipping shippingResult = ShippingCrate.getShippingResult(ware, this.getItems());
        if (shippingResult.isFulfilled()){
            this.clearContent();
            level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            WareProgression.shipWare(ware, player, level, this.worldPosition, shippingResult);
        }
    }

    // <Container>

    @Override
    protected @NotNull ItemStackHandler createInventory(int slots) {
        return new ItemStackHandler(slots){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                updateShippingProgress(slot);
            }
        };
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
}

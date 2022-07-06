package io.github.mortuusars.wares.common.blocks;

import io.github.mortuusars.wares.common.blockentities.DeliveryNoteBlockEntity;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteBlock extends Block implements EntityBlock {
    public DeliveryNoteBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DeliveryNoteBlockEntity(pos, state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
//        ItemStack note = new ItemStack(ModItems.DELIVERY_NOTE.get());
//
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide && blockEntityType == ModBlockEntities.DELIVERY_NOTE_BLOCK_ENTITY.get() ? DeliveryNoteBlockEntity::tick : null;
    }
}

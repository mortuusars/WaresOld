package io.github.mortuusars.wares.common.extensions;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("NullableProblems")
public interface IOpenersCounter extends Container {

    ContainerOpenersCounter getOpenersCounter();

    /**
     * Checks whether the opened container is correct for current block entity.<br>
     * "player.menu instanceof 'Menu' && menu.getBlockEntity instance of 'this'".
     */
    boolean isValidContainer(Player player);

    default ContainerOpenersCounter createOpenersCounter(){
        return new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                onContainerOpen(level, pos, state);
            }

            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                onContainerClosed(level, pos, state);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155466_, int openersCount) {
                onOpenersCountChanged(level, pos, state, p_155466_, openersCount);
            }

            @Override
            protected boolean isOwnContainer( Player player) {
                return isValidContainer(player);
            }
        };
    }

    default void updateOpenBlockState(@NotNull BlockState blockState, boolean isOpen) {
        if (blockState.hasProperty(BlockStateProperties.OPEN)){
            BlockEntity blockEntity = (BlockEntity) this;
            blockEntity.getLevel().setBlock(blockEntity.getBlockPos(), blockState.setValue(BlockStateProperties.OPEN, isOpen), Block.UPDATE_ALL);
        }
    }

    default void onContainerOpen(Level level, BlockPos pos, BlockState state){
        updateOpenBlockState(state, true);
    }

    default void onContainerClosed(Level level, BlockPos pos, BlockState state){
        updateOpenBlockState(state, false);
    }

    default void onOpenersCountChanged(Level level_, BlockPos pos, BlockState state, int p_155466_, int openersCount){
        LogUtils.getLogger().info("Openers: " + openersCount + "??: " + p_155466_);
    }


    default void startOpen(Player player) {
        BlockEntity blockEntity = (BlockEntity) this;
        if (!blockEntity.isRemoved() && !player.isSpectator())
            getOpenersCounter().incrementOpeners(player, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState());
    }

    default void stopOpen(Player player) {
        BlockEntity blockEntity = (BlockEntity) this;
        if (!blockEntity.isRemoved() && !player.isSpectator())
            getOpenersCounter().decrementOpeners(player, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState());
    }

    default void recheckOpen() {
        BlockEntity blockEntity = (BlockEntity) this;
        if (!blockEntity.isRemoved())
            getOpenersCounter().recheckOpeners(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState());
    }
}

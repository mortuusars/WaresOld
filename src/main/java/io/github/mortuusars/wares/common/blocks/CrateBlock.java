package io.github.mortuusars.wares.common.blocks;

import io.github.mortuusars.wares.common.blockentities.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BarrelBlock {

    public CrateBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrateBlockEntity(pPos, pState);
    }
}

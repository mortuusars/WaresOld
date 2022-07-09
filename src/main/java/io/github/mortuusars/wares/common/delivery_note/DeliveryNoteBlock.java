package io.github.mortuusars.wares.common.delivery_note;

import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeliveryNoteBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(1.0D, 0D, 1.0D, 15.0D, 1.0D, 15.0D);

    public DeliveryNoteBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DeliveryNoteBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide && blockEntityType == ModBlockEntities.DELIVERY_NOTE.get() ? DeliveryNoteBlockEntity::tick : null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)){
            DeliveryNoteBlockEntity deliveryNoteEntity = (DeliveryNoteBlockEntity) level.getBlockEntity(pos);
            deliveryNoteEntity.dropNote();
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}

package io.github.mortuusars.wares.common.mailbox;

import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MailboxBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 0, 3);

    public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public MailboxBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FILL_LEVEL, 0));

        VoxelShape northShape = Shapes.or(Block.box(2, 0, 0, 14, 15, 4), Block.box(1, 15, 0, 15, 16, 5));
        SHAPES.put(Direction.NORTH, northShape);
        SHAPES.put(Direction.EAST, Utils.VoxelShape.rotateShape(Direction.NORTH, Direction.EAST, northShape));
        SHAPES.put(Direction.SOUTH, Utils.VoxelShape.rotateShape(Direction.NORTH, Direction.SOUTH, northShape));
        SHAPES.put(Direction.WEST, Utils.VoxelShape.rotateShape(Direction.NORTH, Direction.WEST, northShape));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(FILL_LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MailboxBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : MailboxBlockEntity::tickServer;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof MailboxBlockEntity mailboxEntity)
            NetworkHooks.openGui((ServerPlayer) player, mailboxEntity, pos);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !newState.is(ModBlocks.MAILBOX.get()) && level.getBlockEntity(pos) instanceof MailboxBlockEntity mailboxEntity)
            mailboxEntity.onBlockRemoved(state, newState);
        super.onRemove(state, level, pos, newState, isMoving);
    }
}

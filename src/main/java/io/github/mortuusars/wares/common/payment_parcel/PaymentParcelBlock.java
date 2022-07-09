package io.github.mortuusars.wares.common.payment_parcel;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PaymentParcelBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(2.0D, 0D, 3.0D, 14.0D, 8.0D, 13.0D);

    public PaymentParcelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PaymentParcelBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide){
            PaymentParcelBlockEntity blockEntity = (PaymentParcelBlockEntity) level.getBlockEntity(pos);
            NetworkHooks.openGui((ServerPlayer) player, blockEntity, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

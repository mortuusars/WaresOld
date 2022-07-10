package io.github.mortuusars.wares.common.parcel;

import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ParcelBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(2.0D, 0D, 3.0D, 14.0D, 8.0D, 13.0D);

    public ParcelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ParcelBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide){
            ParcelBlockEntity blockEntity = (ParcelBlockEntity) level.getBlockEntity(pos);
            NetworkHooks.openGui((ServerPlayer) player, blockEntity, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide || blockEntityType != ModBlockEntities.PAYMENT_PARCEL.get() ? null : ParcelBlockEntity::tickServer;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ParcelBlockEntity parcelEntity){
            Containers.dropContents(level, pos, parcelEntity);
        }
    }

    public void removeBlock(Level level, BlockPos pos){
        level.blockEvent(pos, this, 0 , 0);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        level.playSound(null, pos, SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.BLOCKS, 0.5f, 1f);

        Random r = level.getRandom();
        for (int i = 0; i < 15; i++) {
            double x = pos.getX() + 0.5d + r.nextFloat(-0.7f, 0.7f);
            double y = pos.getY() + 0.8d + r.nextFloat(-0.7f, 0.7f);
            double z = pos.getZ() + 0.5d + r.nextFloat(-0.7f, 0.7f);
            double velocity = r.nextFloat(-0.05f, 0.05f);
            level.addParticle(ParticleTypes.POOF, x, y, z, velocity, velocity + 0.05, velocity);
        }

        if (!level.isClientSide)
            level.removeBlock(pos, false);
        return true;
    }
}

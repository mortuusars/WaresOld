package io.github.mortuusars.wares.content.blocks;

import io.github.mortuusars.wares.content.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ShippingCrateBlock extends Block implements EntityBlock {
    public ShippingCrateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide)
            NetworkHooks.openGui((ServerPlayer) pPlayer, (MenuProvider) pLevel.getBlockEntity(pPos), pPos);

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new ShippingCrateBlockEntity(pPos, pState);
    }

//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return pLevel.isClientSide ? null : ShippingCrateBlockEntity::tick;
//    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
        if (level.isClientSide)
            return;

        if (state.getBlock() != pNewState.getBlock() && level.getBlockEntity(pos) instanceof ShippingCrateBlockEntity blockEntity){
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                        NonNullList<ItemStack> items = NonNullList.create();
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                                items.add(i, itemHandler.getStackInSlot(i));
                        }
                        Containers.dropContents(level, pos, items);
                    }
                );

        }
        super.onRemove(state, level, pos, pNewState, pIsMoving);
    }
}

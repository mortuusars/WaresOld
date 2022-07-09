package io.github.mortuusars.wares.common.shipping_crate;

import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShippingCrateBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public ShippingCrateBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        return NonNullList.of(ItemStack.EMPTY, new ItemStack(ModItems.CRATE.get()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(OPEN);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ShippingCrateBlockEntity shippingCrateBlockEntity)
            NetworkHooks.openGui((ServerPlayer) player, shippingCrateBlockEntity, pos);

        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ShippingCrateBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState oldBlockState, Level level, @NotNull BlockPos pos, @NotNull BlockState newBlockState, boolean isMoving) {
        if (!level.isClientSide && !oldBlockState.is(newBlockState.getBlock()) && level.getBlockEntity(pos) instanceof ShippingCrateBlockEntity shippingCrateEntity){
            Containers.dropContents(level, pos, shippingCrateEntity);
            Ware ware = shippingCrateEntity.getWare();
            if (ware != null){
                ItemStack request = new ItemStack(ModItems.PURCHASE_REQUEST.get());
                WareUtils.saveToStackNBT(ware, request);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), request);
            }
        }

        super.onRemove(oldBlockState, level, pos, newBlockState, isMoving);
    }
}

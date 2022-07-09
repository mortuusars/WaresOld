package io.github.mortuusars.wares.common.items;

import io.github.mortuusars.wares.common.shipping_crate.ShippingCrate;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.setup.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PurchaseRequestItem extends Item {
    public PurchaseRequestItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
        Optional<Ware> ware = WareUtils.readWareFromStackNBT(stack);
        ware.ifPresent(w -> {
            components.add(new TextComponent(""));
            NonNullList<Component> wareTooltipInfo = WareUtils.getWareTooltipInfo(w);
            components.addAll(wareTooltipInfo);
        });
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!stack.is(this))
            return InteractionResult.PASS;

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);

        if (clickedBlockState.is(ModTags.SHIPMENT_CONVERTIBLE)){
            ShippingCrate.convertToShippingCrate(player, level, blockPos, clickedBlockState, stack);

            if (player != null && !level.isClientSide && !player.isCreative())
                stack.shrink(1);

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide)
            player.sendMessage(new TextComponent("ware info here"), Util.NIL_UUID);

        return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, level.isClientSide);
    }
}

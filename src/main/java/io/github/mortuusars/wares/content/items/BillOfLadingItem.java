package io.github.mortuusars.wares.content.items;

import io.github.mortuusars.wares.content.ShippingCrate;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BillOfLadingItem extends Item {
    public BillOfLadingItem(Properties properties) {
        super(properties);
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
            return InteractionResult.FAIL;

        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);

        if (clickedBlockState.is(ModTags.SHIPMENT_CONVERTIBLE)){
            ShippingCrate.convertToShippingCrate(level, blockPos, clickedBlockState, stack);
            if (!level.isClientSide && !Objects.requireNonNull(context.getPlayer()).isCreative())
                stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}

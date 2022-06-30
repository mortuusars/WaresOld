package io.github.mortuusars.wares.content.items;

import com.google.gson.Gson;
import io.github.mortuusars.wares.content.ShippingCrate;
import io.github.mortuusars.wares.core.Ware;
import io.github.mortuusars.wares.setup.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class BillOfLadingItem extends Item {
    public BillOfLadingItem(Properties properties) {
        super(properties);
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
        else if (stack.hasTag()){
            CompoundTag tag = stack.getTag();
            if (tag.contains("Ware")){
                String serializedWare = tag.get("Ware").getAsString();
                context.getPlayer().sendMessage(new TextComponent(new Gson().fromJson(serializedWare, Ware.class).toString()), Util.NIL_UUID);
                return InteractionResult.SUCCESS;
            }
        }


        return InteractionResult.PASS;
    }


}

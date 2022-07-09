package io.github.mortuusars.wares;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.common.payment_parcel.PaymentParcel;
import io.github.mortuusars.wares.common.payment_parcel.PaymentParcelBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrate;
import io.github.mortuusars.wares.common.delivery_note.DeliveryNoteBlockEntity;
import io.github.mortuusars.wares.core.Delivery;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.setup.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class WareProgression {
    public static void shipWare(Ware ware, Player player, Level level, BlockPos pos, ShippingCrate.Shipping shippingResult) {
        BlockState noteBlockState = ModBlocks.DELIVERY_NOTE.get().defaultBlockState();

        Delivery delivery = new Delivery(ware);
        delivery.excessItems = shippingResult.excessItems;
        delivery.deliveryTime = ware.deliveryTime.getRandom(level.random);
        delivery.shippingDay = (int)(level.getDayTime() % 24000);

        level.setBlock(pos, noteBlockState, Block.UPDATE_ALL);

        if ( !(level.getBlockEntity(pos) instanceof DeliveryNoteBlockEntity deliveryEntity) ){
            level.removeBlock(pos, false);
            LogUtils.getLogger().error("Failed to place delivery note: DeliveryNoteBlockEntity was not found on pos '{}'", pos);
            return;
        }

        deliveryEntity.setDelivery(delivery);
    }

    public static void spawnPaymentParcel(Level level, BlockPos pos, Delivery delivery){
        BlockState parcelBlockState = ModBlocks.PAYMENT_PARCEL.get().defaultBlockState();

        level.setBlock(pos, parcelBlockState, Block.UPDATE_ALL);

        if ( !(level.getBlockEntity(pos) instanceof PaymentParcelBlockEntity parcelEntity) ){
            level.removeBlock(pos, false);
            LogUtils.getLogger().error("Failed to place payment parcel: PaymentParcelBlockEntity was not found on pos '{}'", pos);
            return;
        }

        if (level.isClientSide){
            return;
        }

        int index = 0;
        for(var stack : delivery.ware.getPaymentStacks()){
            if (index >= PaymentParcel.SLOTS)
                break;

            parcelEntity.setItem(index, stack);
            index++;
        }

//        List<ItemStack> items = new ArrayList<>();
//
//        for (ItemStack stack : delivery.ware.getPaymentStacks()) {
//            int maxSize = stack.getMaxStackSize();
//            if (maxSize >= stack.getCount())
//                items.add(stack);
//            else {
//                int count = stack.getCount();
//                while (count > 0){
//                    ItemStack newStack = stack.copy();
//                    int newStackSize = Math.min(count, maxSize);
//                    newStack.setCount(newStackSize);
//                    items.add(newStack);
//                    count -= newStackSize;
//                }
//            }
//        }
//
//        items = items.stream().limit(PaymentParcel.SLOTS).toList();
//
//        for (int i = 0; i < items.size(); i++) {
//            parcelEntity.setItem(i, items.get(i));
//        }


    }
}

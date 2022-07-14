package io.github.mortuusars.wares.core.ware;

import com.mojang.datafixers.util.Either;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.core.ware.data.*;
import io.github.mortuusars.wares.lib.ModConstants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WareFinalizer {

    public Ware finalize(WareData wareData){
        return new Ware(wareData.description(),
                    wareData.rarity(),
                    wareData.requestedItems().stream().map(this::finalizedItem).collect(Collectors.toList()),
                    createPaymentStacks(wareData),
                    finalizeIntNumbers(wareData.experience()),
                    chooseDeliveryTime(wareData));
    }

    private WareItem finalizedItem(WareItem item){
        return new WareItem(item.item(), item.tag(), item.nbt(),
                Either.left(finalizeIntNumbers(item.count())), item.chance(), item.ignoreNBT(), item.ignoreDamage());
    }

    private List<ItemStack> createPaymentStacks(WareData wareData){

        List<ItemStack> stacks = new ArrayList<>();

        for (WareItem potentialPayment : wareData.paymentItems()){

            if (ModConstants.RANDOM.nextDouble() > potentialPayment.chance())
                continue;

            Item item = potentialPayment.getItemOrFirstItemFromTag();
            if (item == Items.AIR){
                Wares.LOGGER.error("[PaymentItems] Cannot get Item from WareItem '{}'. Skipping.", potentialPayment);
                continue;
            }

            int totalCount = finalizeIntNumbers(potentialPayment.count());

            while (totalCount > 0){
                ItemStack newStack = new ItemStack(item);
                int maxSize = newStack.getMaxStackSize();
                int count = Math.min(totalCount, maxSize);
                newStack.setCount(count);
                totalCount -= count;

                if (potentialPayment.nbt().isPresent())
                    newStack.setTag(potentialPayment.nbt().get());

                stacks.add(newStack);
            }
        }

        return stacks;
    }

    private DeliveryTime chooseDeliveryTime(WareData wareData){
        DeliveryTimeData timeData = wareData.deliveryTime();
        return new DeliveryTime(timeData.timeOfDay(), finalizeIntNumbers(timeData.days()));
    }

    private int finalizeIntNumbers(Either<Integer, IntegerRange> numbers) {
        return numbers.map(i -> i, range -> range.get(ModConstants.RANDOM));
    }
}

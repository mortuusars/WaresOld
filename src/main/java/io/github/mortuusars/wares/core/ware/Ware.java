package io.github.mortuusars.wares.core.ware;

import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.types.IntRange;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Ware {
    public String title = "";
    public String description = "";
    public String buyer = "";
    public int experience = 0;
    public IntRange deliveryTime = IntRange.ZERO;
    public int deliveryDays = 0;
    public List<FixedWareItemInfo> requestedItems = Collections.emptyList();
    public List<FixedWareItemInfo> paymentItems = Collections.emptyList();

    public Optional<FixedWareItemInfo> getMatchingRequestedItem(ItemStack stack){
        for (var reqItem : requestedItems){
            if (reqItem.matches(stack))
                return Optional.of(reqItem);
        }

        return Optional.empty();
    }

    public NonNullList<ItemStack> getPaymentStacks(){
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (var item : paymentItems)
            stacks.add(item.toItemStack());
        return stacks;
    }


    public Ware title(String title) {
        this.title = title;
        return this;
    }

    public Ware description(String description) {
        this.description = description;
        return this;
    }

    public Ware buyer(String buyer) {
        this.buyer = buyer;
        return this;
    }

    public Ware experience(int experience) {
        this.experience = experience;
        return this;
    }

    public Ware deliveryTimeRange(int min, int max){
        this.deliveryTime = new io.github.mortuusars.wares.types.IntRange(min, max);
        return this;
    }

    public Ware deliveryDays(int days){
        this.deliveryDays = days;
        return this;
    }

    public Ware addRequested(FixedWareItemInfo requestedItem) {
        requestedItems.add(requestedItem);
        return this;
    }

    public Ware addPayment(FixedWareItemInfo paymentItem) {
        paymentItems.add(paymentItem);
        return this;
    }

    public Ware setRequestedItems(List<FixedWareItemInfo> requestedItems) {
        this.requestedItems = requestedItems;
        return this;
    }

    public Ware setPaymentItems(List<FixedWareItemInfo> paymentItems) {
        this.paymentItems = paymentItems;
        return this;
    }
}

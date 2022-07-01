package io.github.mortuusars.wares.core.ware;

import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class Ware {
    public String title = "";
    public String description = "";
    public String seller = "";
    public int experience = 0;
    public List<FixedWareItemInfo> requestedItems = Collections.emptyList();
    public List<FixedWareItemInfo> paymentItems = Collections.emptyList();


    public NonNullList<ItemStack> getPaymentStacks(){
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (var item : paymentItems)
            item.toItemStack().ifPresent(stacks::add);
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

    public Ware seller(String seller) {
        this.seller = seller;
        return this;
    }

    public Ware experience(int experience) {
        this.experience = experience;
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

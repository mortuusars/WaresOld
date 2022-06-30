package io.github.mortuusars.wares.core;

import com.google.common.base.Preconditions;
import io.netty.util.internal.RecyclableArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Ware {
    public String title = "";
    public String description = "";
    public Rarity rarity = Rarity.COMMON;
    public float weight = 1f;
    public List<WareItem> requestedItems = new ArrayList<>();
    public List<WareItem> paymentItems = new ArrayList<>();
    public int experience = 0;

    public Ware(){}

    public Ware(String title, String description, Rarity rarity, float weight, List<WareItem> requestedItems, List<WareItem> paymentItems, int experience) {
        this.title = title;
        this.description = description;
        this.weight = weight;
        this.experience = experience;
        this.requestedItems = requestedItems;
        this.paymentItems = paymentItems;
    }

    public NonNullList<ItemStack> getPaymentItemStacks(){
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (WareItem item : paymentItems)
            stacks.addAll(item.createItemStacks());

        return stacks;
    }

    @Override
    public String toString() {
        return "Ware{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", rarity=" + rarity +
                ", weight=" + weight +
                ", requestedItems=" + requestedItems +
                ", paymentItems=" + paymentItems +
                ", experience=" + experience +
                '}';
    }

    public static class Builder{

        private final Ware _ware = new Ware();

        public Builder title(String title){
            Preconditions.checkNotNull(title);
            _ware.title = title;
            return this;
        }

        public Builder description(String description){
            Preconditions.checkNotNull(description);
            _ware.description = description;
            return this;
        }

        public Builder rarity(Rarity rarity){
            Preconditions.checkNotNull(rarity);
            _ware.rarity = rarity;
            return this;
        }

        public Builder weight(float weight){
            Preconditions.checkArgument(weight > 0f, "Weight should be larger than 0. Input: {}", weight);
            _ware.weight = weight;
            return this;
        }

        public Builder experience(int experience){
            _ware.experience = experience;
            return this;
        }

        public Builder addRequestedItem(@NotNull WareItem requestedItem){
            _ware.requestedItems.add(requestedItem);
            return this;
        }

        public Builder addPaymentItem(@NotNull WareItem paymentItem){
            _ware.paymentItems.add(paymentItem);
            return this;
        }

        public Ware build(){
            return _ware;
        }
    }
}

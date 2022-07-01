package io.github.mortuusars.wares.core.ware;

import com.google.common.base.Preconditions;
import io.github.mortuusars.wares.core.Rarity;
import io.github.mortuusars.wares.core.ware.item.PotentialWareItemInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents potential Ware: Item ranges, XP ranges, etc.
 */
public class PotentialWare {
    public String title = "";
    public String description = "";
    public String seller = "";
    public Rarity rarity = Rarity.COMMON;
    public float weight = 1f;
    public List<PotentialWareItemInfo> requestedItems = new ArrayList<>();
    public List<PotentialWareItemInfo> paymentItems = new ArrayList<>();
    public int experienceMin = 0;
    public int experienceMax = 0;

    public Ware toFixedWare(){
        return new Ware()
            .title(title)
            .description(description)
            .seller(seller)
            .experience(getExperience())
            .setRequestedItems(requestedItems.stream().map(PotentialWareItemInfo::toFixed).collect(Collectors.toList()))
            .setPaymentItems(paymentItems.stream().map(PotentialWareItemInfo::toFixed).collect(Collectors.toList()));
    }

    public int getExperience(){
        return new Random().nextInt(experienceMin, ++experienceMax);
    }

    public PotentialWare title(String title){
        this.title = title;
        return this;
    }

    public PotentialWare description(String description){
        this.description = description;
        return this;
    }

    public PotentialWare seller(String seller){
        this.seller = seller;
        return this;
    }

    public PotentialWare rarity(Rarity rarity){
        Preconditions.checkNotNull(rarity);
        this.rarity = rarity;
        return this;
    }

    public PotentialWare weight(float weight){
        Preconditions.checkArgument(weight > 0f, "Weight should be larger than 0. Input: {}", weight);
        this.weight = weight;
        return this;
    }

    public PotentialWare experience(int experience){
        experienceMin = experience;
        experienceMax = experience;
        return this;
    }

    public PotentialWare experienceRange(int min, int max){
        Preconditions.checkArgument(min <= max, "Experience min value should not be larger than max value.");
        experienceMin = min;
        experienceMax = max;
        return this;
    }

    public PotentialWare addRequestedItem(@NotNull PotentialWareItemInfo requestedItem){
        requestedItems.add(requestedItem);
        return this;
    }

    public PotentialWare addPaymentItem(@NotNull PotentialWareItemInfo paymentItem){
        paymentItems.add(paymentItem);
        return this;
    }
}

package io.github.mortuusars.wares.core.ware;

import com.google.common.base.Preconditions;
import io.github.mortuusars.wares.lib.enums.Rarity;
import io.github.mortuusars.wares.core.ware.item.PotentialWareItemInfo;
import io.github.mortuusars.wares.core.types.IntegerRange;
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
    public String buyer = "";
    public Rarity rarity = Rarity.COMMON;
    public float weight = 1f;
    public List<PotentialWareItemInfo> requestedItems = new ArrayList<>();
    public List<PotentialWareItemInfo> paymentItems = new ArrayList<>();
    public IntegerRange deliveryTime = IntegerRange.ZERO;
    public IntegerRange deliveryDays = IntegerRange.ZERO;
    public IntegerRange experience = IntegerRange.ZERO;

//    public Ware toFixedWare(){
//        return new Ware()
//            .title(title)
//            .description(description)
//            .buyer(buyer)
////            .experience(getExperience())
//            .setRequestedItems(requestedItems.stream().map(PotentialWareItemInfo::toFixed).collect(Collectors.toList()))
//            .setPaymentItems(paymentItems.stream().map(PotentialWareItemInfo::toFixed).collect(Collectors.toList()));
//    }

//    public int getExperience(){
//        return experience.getRandom(new Random());
//    }
//
//    public int getExperience(Random random){
//        return experience.getRandom(random);
//    }

    public PotentialWare title(String title){
        this.title = title;
        return this;
    }

    public PotentialWare description(String description){
        this.description = description;
        return this;
    }

    public PotentialWare buyer(String buyer){
        this.buyer = buyer;
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

//    public PotentialWare experience(int experience){
//        this.experience = new IntegerRange(experience, experience);
//        return this;
//    }
//
//    public PotentialWare experienceRange(int min, int max){
//        this.experience = new IntegerRange(min, max);
//        return this;
//    }
//
//    public PotentialWare deliveryTimeRange(int min, int max){
//        this.deliveryTime = new IntegerRange(min, max);
//        return this;
//    }
//
//    public PotentialWare deliveryDaysRange(int min, int max){
//        this.deliveryDays = new IntegerRange(min, max);
//        return this;
//    }

    public PotentialWare addRequestedItem(@NotNull PotentialWareItemInfo requestedItem){
        requestedItems.add(requestedItem);
        return this;
    }

    public PotentialWare addPaymentItem(@NotNull PotentialWareItemInfo paymentItem){
        paymentItems.add(paymentItem);
        return this;
    }
}

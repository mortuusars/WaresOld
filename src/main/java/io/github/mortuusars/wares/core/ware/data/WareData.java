package io.github.mortuusars.wares.core.ware.data;


import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.lib.enums.Rarity;

import java.util.List;

/*
Represents a potential ware.
 */
public record WareData(WareDescription description, Rarity rarity, int weight,
                       List<WareItem> requestedItems, List<WareItem> paymentItems,
                       Either<Integer, IntegerRange> experience, DeliveryTimeData deliveryTime) {

    public static final Codec<WareData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WareDescription.CODEC.optionalFieldOf("description", WareDescription.EMPTY).forGetter(WareData::description),
            Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(WareData::rarity),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(WareData::weight),
            WareItem.CODEC.listOf().fieldOf("requestedItems").forGetter(WareData::requestedItems),
            WareItem.CODEC.listOf().fieldOf("paymentItems").forGetter(WareData::paymentItems),
            Codec.either(Codec.intRange(0, Integer.MAX_VALUE), IntegerRange.CODEC).optionalFieldOf("experience", Either.left(0)).forGetter(WareData::experience),
            DeliveryTimeData.CODEC.optionalFieldOf("deliveryTime", DeliveryTimeData.nextMorning()).forGetter(WareData::deliveryTime)
    ).apply(instance, WareData::new));
}

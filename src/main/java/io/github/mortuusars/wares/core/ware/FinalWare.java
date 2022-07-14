package io.github.mortuusars.wares.core.ware;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.ware.item.WareItem;
import io.github.mortuusars.wares.lib.enums.Rarity;

import java.util.List;

public record FinalWare(String title, String description, String buyer, Rarity rarity,
                        List<WareItem> requestedItems, List<WareItem> paymentItems,
                        int experience, DeliveryTime deliveryTime) {

    public static final Codec<FinalWare> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("title").orElse(null).forGetter(FinalWare::title),
                Codec.STRING.fieldOf("description").orElse(null).forGetter(FinalWare::description),
                Codec.STRING.fieldOf("buyer").orElse(null).forGetter(FinalWare::buyer),
                Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(FinalWare::rarity),
                WareItem.CODEC.listOf().fieldOf("requestedItems").forGetter(FinalWare::requestedItems),
                WareItem.CODEC.listOf().fieldOf("paymentItems").forGetter(FinalWare::paymentItems),
                Codec.INT.fieldOf("experience").forGetter(FinalWare::experience),
                DeliveryTime.CODEC.fieldOf("deliveryTime").forGetter(FinalWare::deliveryTime))
            .apply(instance, FinalWare::new));

}

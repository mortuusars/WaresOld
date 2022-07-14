package io.github.mortuusars.wares.core.ware;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.core.ware.item.WareItem;
import io.github.mortuusars.wares.lib.enums.Rarity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record Ware(WareDescription description, Rarity rarity,
                        List<WareItem> requestedItems, List<ItemStack> paymentItems,
                        int experience, DeliveryTime deliveryTime) {

    public static final Codec<Ware> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    WareDescription.CODEC.optionalFieldOf("description", WareDescription.EMPTY).forGetter(Ware::description),
                    Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(Ware::rarity),
                    WareItem.CODEC.listOf().fieldOf("requestedItems").forGetter(Ware::requestedItems),
                    ItemStack.CODEC.listOf().fieldOf("paymentItems").forGetter(Ware::paymentItems),
                    Codec.INT.fieldOf("experience").forGetter(Ware::experience),
                    DeliveryTime.CODEC.fieldOf("deliveryTime").forGetter(Ware::deliveryTime))
            .apply(instance, Ware::new));

}

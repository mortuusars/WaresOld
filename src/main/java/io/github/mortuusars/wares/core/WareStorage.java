package io.github.mortuusars.wares.core;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;

import java.util.Optional;

public class WareStorage {
    private NonNullList<Ware> _wares = NonNullList.create();

    public ImmutableList<Ware> getAllWares(){
        return ImmutableList.<Ware>builder().addAll(_wares).build();
    }

    public Optional<Ware> getRandomWare(){

        // Roll for rarity

        // Weighted random

        return Optional.empty();
    }

    public void loadWares(){
        NonNullList<Ware> newWares = NonNullList.create();

        newWares.add(new Ware.Builder()
                .title("Test Ware")
                .description("Desc")
                .experience(2)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:apple")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:saplings")
                        .countRange(50, 204)
                        .chance(0.7f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:emerald")
                        .count(12)
                        .build())
                .build());

        newWares.add(new Ware.Builder()
                .title("Test Ware SECOND")
                .description("Desc 2")
                .experience(12)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:anvil")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:coals")
                        .countRange(50, 204)
                        .chance(0.2f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:gold_block")
                        .countRange(10, 30)
                        .build())
                .build());

        newWares.add(new Ware.Builder()
                .title("Test Ware SECOND")
                .description("Desc 2")
                .experience(0)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:anvil")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:coals")
                        .countRange(50, 204)
                        .chance(0.2f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:bread")
                        .countRange(1, 4)
                        .build())
                .build());

        newWares.add(new Ware.Builder()
                .title("FOUR")
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:anvil")
                        .countRange(2, 5)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:diamond")
                        .countRange(1, 2)
                        .build())
                .build());

        _wares = newWares;
    }

}

package io.github.mortuusars.wares.core.ware;

import com.google.common.collect.ImmutableList;
import io.github.mortuusars.wares.core.Rarity;
import io.github.mortuusars.wares.core.ware.item.PotentialWareItemInfo;
import net.minecraft.core.NonNullList;

import java.util.Optional;
import java.util.Random;

public class WareStorage {
    private NonNullList<PotentialWare> _wares = NonNullList.create();

    public ImmutableList<PotentialWare> getAllWares(){
        return ImmutableList.<PotentialWare>builder().addAll(_wares).build();
    }

    public Optional<Ware> getRandomWare(){

        //TODO Roll for rarity

        //TODO Weighted random

        if (_wares.size() > 0){
            PotentialWare wareDefinition = _wares.get(new Random().nextInt(0, _wares.size()));
            return Optional.ofNullable(wareDefinition.toFixedWare());
        }

        return Optional.empty();
    }

    public void loadWares(){
        NonNullList<PotentialWare> newWares = NonNullList.create();

        newWares.add(new PotentialWare()
                .title("Title 1")
                .description("Ware description example")
                .buyer("Edward Largenose")
                .experience(4)
                .weight(2f)
                .rarity(Rarity.UNCOMMON)
                .addRequestedItem(new PotentialWareItemInfo().item("minecraft:apple").countRange(8, 12))
                .addRequestedItem(new PotentialWareItemInfo().item("minecraft:sweet_berries").countRange(4, 10))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:diamond").count(4)));

        newWares.add(new PotentialWare()
                .title("Title 2")
                .experience(1)
                .weight(4f)
                .rarity(Rarity.RARE)
                .addRequestedItem(new PotentialWareItemInfo().item("minecraft:anvil").countRange(8, 12))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:iron_ingot").countRange(28, 32))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:apple").countRange(28, 32))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:book").countRange(28, 32))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:chain").countRange(28, 32)));

        newWares.add(new PotentialWare()
                .title("Title 4")
                .experience(2)
                .weight(4f)
                .rarity(Rarity.UNCOMMON)
                .addRequestedItem(new PotentialWareItemInfo().item("minecraft:grindstone").count(1))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:leather").countRange(4, 6)));

        newWares.add(new PotentialWare()
                .title("Title asdasd")
                .experience(2)
                .weight(8f)
                .rarity(Rarity.COMMON)
                .addRequestedItem(new PotentialWareItemInfo().item("minecraft:grindstone").count(1))
                .addPaymentItem(new PotentialWareItemInfo().item("minecraft:leather").countRange(154, 187)));

        for (int i = 0; i < 16; i++) {
            newWares.get(2).addPaymentItem(new PotentialWareItemInfo().item("minecraft:gold_ingot").count(13));
        }

        _wares = newWares;
    }
}

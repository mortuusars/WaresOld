package io.github.mortuusars.wares.core;

import io.github.mortuusars.wares.core.ware.Ware;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * Represents a successful shipment that is waiting to be delivered.
 */
public class Delivery {
    public Ware ware;
    public NonNullList<ItemStack> excessItems = NonNullList.create();
    public int deliveryTime = 0;
    public int deliveryDay = 0;
    public int shippingDay = 0;

    public Delivery(Ware ware) {
        this.ware = ware;
    }
}

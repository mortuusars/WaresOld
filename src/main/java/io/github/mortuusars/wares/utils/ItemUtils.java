package io.github.mortuusars.wares.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {
    public static int getMaxStackSize(Item item){
        return new ItemStack(item).getMaxStackSize();
    }
}

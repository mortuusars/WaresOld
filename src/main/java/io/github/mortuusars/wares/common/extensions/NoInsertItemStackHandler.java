package io.github.mortuusars.wares.common.extensions;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class NoInsertItemStackHandler extends ItemStackHandler {

    public NoInsertItemStackHandler(int slots){
        super(slots);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }
}

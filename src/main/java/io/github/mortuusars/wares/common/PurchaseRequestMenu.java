package io.github.mortuusars.wares.common;

import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.setup.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PurchaseRequestMenu extends AbstractContainerMenu {

    public final Ware ware;

    public PurchaseRequestMenu(int containerId, Inventory playerInventory, Ware ware) {
        super(ModContainers.PURCHASE_REQUEST.get(), containerId);

        if (ware == null)
            throw new IllegalArgumentException("Failed to read Ware from buffer.");

        this.ware = ware;
     }

    public static PurchaseRequestMenu fromBuffer(int containerID, Inventory playerInventory, FriendlyByteBuf buffer){
        Optional<Ware> wareOptional = Ware.readFromNBT(buffer.readAnySizeNbt());
        if (wareOptional.isEmpty())
            throw new IllegalArgumentException("Failed to read Ware from NBT. " + buffer.readAnySizeNbt());
        return new PurchaseRequestMenu(containerID, playerInventory, wareOptional.get());
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}

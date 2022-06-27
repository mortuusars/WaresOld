package io.github.mortuusars.wares.inventory.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class WaresAbstractContainerMenu extends AbstractContainerMenu {

    protected WaresAbstractContainerMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected void addPlayerInventory(Inventory playerInventory, int startIndex, int xPosition, int yPosition, int slotSize){
        for (int row = 0; row < 3; row++){
            for (int column = 0; column < 9; column++){
                addSlot(new Slot(playerInventory,
                        startIndex + (column + row * 9 + 9), xPosition + column * slotSize, yPosition + row * slotSize));
            }
        }
    }

    protected void addPlayerInventory(Inventory playerInventory, int startIndex, int yPosition){
        addPlayerInventory(playerInventory, startIndex, 8, yPosition, 18);
    }

    protected void addPlayerInventory(Inventory playerInventory, int yPosition){
        addPlayerInventory(playerInventory, 9, 8, yPosition, 18);
    }

    protected void addPlayerHotbar(Inventory playerInventory, int startIndex, int xPosition, int yPosition, int slotWidth){
        for (int i = 0; i < 9; i++){
            addSlot(new Slot(playerInventory, startIndex + i, xPosition + i * slotWidth, yPosition));
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory, int startIndex, int yPosition){
        addPlayerHotbar(playerInventory, startIndex, 8, yPosition, 18);
    }

    protected void addPlayerHotbar(Inventory playerInventory, int yPosition){
        addPlayerHotbar(playerInventory, 0, 8, yPosition, 18);
    }
}
